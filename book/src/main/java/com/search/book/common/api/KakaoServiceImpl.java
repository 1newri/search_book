package com.search.book.common.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.search.book.common.api.kakao.KakaoService;
import com.search.book.common.util.UtilHttp;
import com.search.book.common.util.UtilNull;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KakaoServiceImpl implements KakaoService{
	
	@Value("#{prop['sns.kakao.restapi.key']}")
	private String kakaoAppRestKey = "7e52605f7a63904f04e30d89cd0db8ba";
	

	@Value("#{prop['app.url']}")
	private String appUrl = "https://www.cybercoex.co.kr";  //last slash remove
	
	@Override
	public String loginOauthUrl() {

		String kakaoApi = "https://kauth.kakao.com/oauth/authorize"; 
		Map<String, String> p = new HashMap<String, String>(); 
		p.put("client_id", kakaoAppRestKey); 
		//p.put("redirect_uri", kakaoRedirectdUri); 
		p.put("redirect_uri", getKakaoRedirectUri());
		p.put("response_type", "code"); 
		
		String fullUrl = UtilHttp.buildUrl(kakaoApi, p); 
		
		log.debug("카카오로그인 코드받기 인증시작 url:{}", fullUrl);
		
		return fullUrl;
	}

	/**
	 * 카카오 인증과정을 시작할 리다이렉트 uri를 리턴함. 
	 * @return
	 */
	private String getKakaoRedirectUri() {
		
		return appUrl + "/sns/kakao/oauth";
	}

	@Override
	public String userAccessTokenWithCode(String codeAuthorized) {
		
		String kakaoApi = "https://kauth.kakao.com/oauth/token"; 
		Map<String, String> p = new HashMap<String, String>();
		p.put("grant_type", "authorization_code"); //고정 
		p.put("client_id", kakaoAppRestKey); 
		p.put("redirect_uri", getKakaoRedirectUri()); 
		p.put("code", codeAuthorized); 
		
		//POST 방식으로 전달해야 하네... 
		//String fullUrl = UtilHttp.buildUrl(kakaoApi, p); 
		//log.info("POST방식으로 전달해야 하네...:{}", fullUrl);
		log.debug("==카카오 사용자토큰 받기, url:{}, 파라미터:{}", kakaoApi, p);
		Map<String, Object> re = UtilHttp.postMap(kakaoApi, p); 
		
		//{access_token=_TJz7FjVOvcHGDrQCPzVWjVvrCSd9YFXy0iYQgo8BVUAAAFo5ii1iw, token_type=bearer, refresh_token=r-7hJL954P6e7jU6WWKGttVjcvoicEft1jcaZwo8BVUAAAFo5ii1iA, expires_in=21599, scope=account_email profile, refresh_token_expires_in=2591999}
		log.debug("==카카오 사용자토큰 받기 결과:{}", re);
		
		String accessToken = (String) re.get("access_token");   //사용자 토큰
		
		return accessToken;
	}

	@Override
	public Map<String, Object> userInfoMap(String userAccessToken) {
		String kakaoApi = "https://kapi.kakao.com/v2/user/me";
		
		Map<String, String> p = new HashMap<String, String>();
		p.put("Authorization", "Bearer " + userAccessToken); 
		
		//POST 방식으로 전달해야 하네... 
		//String fullUrl = UtilHttp.buildUrl(kakaoApi, p); 
		//log.info("POST방식으로 전달해야 하네...:{}", fullUrl);
		
		//{id=1026258673, properties={nickname=cybercoexinfo}, kakao_account={has_email=true, is_email_valid=true, is_email_verified=true, email=cybercoex@gmail.com, has_age_range=false, has_birthday=false, has_gender=false}}
		Map<String, Object> re = UtilHttp.postHeader(kakaoApi, p); 
		log.debug("카카오사용자정보:{}", re);
		
		return re;
	}
	

	@Override
	public SnsProfileDto userInfo(String userAccessToken) {

		Map<String, Object> re = userInfoMap(userAccessToken); 
		
		
		SnsProfileDto profile = convertUserInfo(re);
		if ( null == profile )   return profile; 
		
		profile.setUserAccessToken(userAccessToken); 
		
		return profile;
	}

	/**
	 * 회원정보 Map을 Dto로 변환처리함. 
	 * @param re
	 * @return
	 */
	private SnsProfileDto convertUserInfo(Map<String, Object> re) {
		if (  null == re )   return null; 
		
		log.debug("카카오회원프로파일:{}", re);
		// {resultcode=00, message=success, response={id=25275368, age=40-49, gender=M, email=xxx@naver.com, name=xxxx, birthday=10-20}}
		//{id=1026258673, properties={nickname=cybercoexinfo}, kakao_account={has_email=true, is_email_valid=true, is_email_verified=true, email=cybercoex@gmail.com, has_age_range=false, has_birthday=false, has_gender=false}}
		//{id=1026258673, properties={nickname=cybercoexinfo}, kakao_account={has_email=true, is_email_valid=true, is_email_verified=true, email=cybercoex@gmail.com, has_age_range=false, has_birthday=false, has_gender=false}}
		Object id = re.get("id"); 
		
		String snsId = id.toString(); // 00:정상
		if (UtilNull.isEmpty(snsId)  ) {
			log.warn("카카오 회원번호가 존재하지 않습니다."); 
			return null; 
		}

		SnsProfileDto p = SnsProfileDto.builder().
						snsType("kako")
						.snsIdSeq(snsId)
						.build();
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> res = (LinkedHashMap<String, Object>) re.get("properties");
		p.setNickName((String)res.get("nickname"));
 
		
		log.info("==== kakao_account:{}", re.get("kakao_account") );
		
		@SuppressWarnings("unchecked")
		Map<String, Object> account = (Map<String, Object>) re.get("kakao_account");
		log.info("==카카오회원정보 상세:{}", account);   //카카오는 이름이 없는듯...
		log.warn("==카카오 회원정보에는 이름, 나이대역,성별 정보가 없는듯함....");
		
		p.setEmail( (String) account.get("email"));	//이메일
		
		
		//이메일 인증여부 확인
		boolean emailVerified = (Boolean) account.get("is_email_verified");
		p.setEmailVerified(emailVerified);
		
		
		//has_age_range=false  
		
		//has_gender=false
		/*
		p.setAgeRage( res.get("age"));	// 나이범위 
		p.setGender( res.get("gender"));	//성별 M F
		
		p.setName( res.get("name"));  //이름
		
		*/
		
		return p;
	}

	@Override
	public Map<String, Object> userFriends(String userAccessToken) {

		String kakaoApi = "https://kapi.kakao.com/v1/friends?limit=3";
		
		Map<String, String> p = new HashMap<String, String>();
		p.put("Authorization", "Bearer " + userAccessToken); 
		
		Map<String, Object> re = UtilHttp.postHeader(kakaoApi, p); 
		log.info("카카오사용자친구:{}", re);
		
		return re;
	}


}
