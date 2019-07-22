package com.search.book.common.api.naver;

import java.util.Map;

import com.search.book.common.api.SnsProfileDto;

public interface NaverService {

	/**
	 * 네이버 로그인 인증을 시작하기 위한 url을 리턴함. 
	 * @param stateCode
	 * @return
	 */
	String loginOauthUrl(String stateCode);

	/**
	 * 로그인 인증후 전달받은 code로 사용자 토큰을 구함.
	 * @param code
	 * @return
	 */
	String userAccessTokenWithCode(String code, String state);

	

	/**
	 * 회원정보를 리턴함.
	 * @param userAccessToken
	 * @return
	 */
	SnsProfileDto userInfo(String userAccessToken);
	/**
	 * accessToken을 이용하여 사용자정보(프로필)을 가져옴. 
	 * @param userAccessToken
	 * @return
	 */
	Map<String, Object> userInfoMap(String userAccessToken);

	

	/**
	 * 해킹방지를 위한 난수코드 
	 * @return
	 */
	String getStateCode();



}
