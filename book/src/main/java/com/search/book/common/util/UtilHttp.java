package com.search.book.common.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class UtilHttp {
	
	private static Logger log = LoggerFactory.getLogger(UtilHttp.class);

	
	/**
	 * fromHttpUrl 에 queryParam 를 추가함. 
	 * @param baseUrl
	 * @param queryParam
	 * @return
	 */
	public static String buildUrl(String baseUrl, Map<String, String> queryParam) {
		
		String url = baseUrl.trim(); 
		
		if ( queryParam == null )  return url; 
		
		StringBuffer sb = new StringBuffer(); 
		for(Entry<String, String> p : queryParam.entrySet()) {
			
			if ( sb.length() > 0 ) {
				sb.append("&"); 
			}
			sb.append(p.getKey()).append("=").append(p.getValue());
		}
		
		url += "?" + sb.toString(); 
		log.debug("URL:{}", url);
		
		return url; 
		
	}


	/**
	 * GET 방식으로 접속한 결과를 리턴함. 
	 * @param baseUrl
	 * @param param
	 * @return
	 */
	public static Map<String, Object> getMap(String baseUrl, Map<String, String> param) {
		
		RestTemplate rest = new RestTemplate(); 
		
		String url = buildUrl(baseUrl, param); 
		
		log.debug("HTTP GET 시작 url:{}", url);
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> reMap = rest.getForEntity(url, Map.class);
		
		log.debug("GET결과 status:{}, body:{}", reMap.getStatusCode(), reMap.getBody());
		
		
		@SuppressWarnings("unchecked")
		Map<String, Object> mapResult =  reMap.getBody();
		
		return mapResult;
	}
	
	/**
	 * 
	 * url 을 POST방식으로 접속하여 결과를 리턴함. 
	 * 참고 https://aramk.tistory.com/33  
	 * 
	 * @param baseUrl
	 * @param param
	 * @return
	 */
	public static Map<String, Object> postMap(String baseUrl, Map<String, String> param) {
		
		
		MultiValueMap<String, String> parameters = convert(param); 
		
		HttpHeaders headers = new HttpHeaders(); 
		//headers.add("x-waple-authorization",  "API키값");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);

		
		return postMap(baseUrl, request);
	}
	

	private static Map<String, Object> postMap(String baseUrl, HttpEntity<MultiValueMap<String, String>> httpEntityWithHeader) {

		RestTemplate rest = new RestTemplate(); 
		
		log.debug("HTTP POST 시작 url:{}, body:{}", httpEntityWithHeader.getBody());
		@SuppressWarnings("unchecked")
		Map<String, Object> result = rest.postForObject(baseUrl, httpEntityWithHeader, Map.class);
		log.debug("POST 처리결과:{}", result);
		
		return result;
	}


	/**
	 * header에 파라미터를 추가해서 POST 결과를 리턴함. 
	 * @param baseUrl
	 * @param param
	 * @return
	 */
	public static Map<String, Object> postHeader(String baseUrl, Map<String, String> param) {
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>(); 
		
		HttpHeaders headers = new HttpHeaders(); 
		//headers.add("x-waple-authorization",  "API키값");
		for(Entry<String, String> m : param.entrySet()) {
			headers.add(m.getKey(), m.getValue());
		}
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameters, headers);
		
		log.debug("HTTP POST header:{}", request.getHeaders().toSingleValueMap());
		
		return postMap(baseUrl, request); 
	}

	
	/**
	 * map을 http 요청처리를 위하여 MultiValueMap 변경함. 
	 * @param p
	 * @return
	 */
	private static MultiValueMap<String, String> convert(Map<String, String> p) {
		
		MultiValueMap<String, String> mp = new LinkedMultiValueMap<String, String>();

		for(Entry<String, String> m : p.entrySet()) {
			mp.add(m.getKey(), m.getValue()) ;
		}
		
		
		return mp;
	}


	/**
	 * request 의 쿼리파라미터를 MAP형식으로 변경하여 리턴함. 
	 * @param params
	 * @return
	 */
	public static Map<String, String> paramMap(String params) {
		log.info("파라미터:{}", params);
		Map<String, String> p = new HashMap<String, String>(); 
		if ( null == params || params.isEmpty() )  return p ; 
		
		String[] sp = params.split("&"); 
		for(String s : sp ) {
			String[] ss = s.split("=");
			p.put(ss[0], ss[1]); 
		}
		
		return p;
	}


	/**
	 * request 정보를 문자열로 변환함. 디버깅 용도로만 사용하세요..
	 * @param request
	 * @return
	 */
	public static Object simpleString(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer(); 

		sb.append("== getRequestURL:").append(request.getRequestURL()).append("\n");
		sb.append("== RequestURI:").append(request.getRequestURI()).append("\n");
		
		
		
		sb.append("==getAttributeNames()"); 
		Enumeration<?> x = request.getAttributeNames();
		while(x.hasMoreElements()) {
			Object obj = x.nextElement();
			sb.append(obj).append(":").append(request.getAttribute(obj.toString())).append("\n");
		}

		sb.append("==getHeaderNames()"); 
		Enumeration<?> h = request.getHeaderNames(); 
		while(h.hasMoreElements()) {
			Object obj = h.nextElement();
			sb.append(obj).append(":").append(request.getAttribute(obj.toString())).append("\n");
		}

		sb.append("== getParameterMap  ==").append("\n");
		@SuppressWarnings("unchecked")
		Map<String, String[]> map = request.getParameterMap();  
		sb.append(",").append("Parameter Size:").append(map.size());
		sb.append("\n"); 

		for (Entry<String, String[]> s : map.entrySet()) {
			sb.append(s.getKey() + "=" + Arrays.toString(s.getValue()));
			sb.append("\n"); 
		}

		sb.append("== getQueryString:").append(request.getQueryString()).append("\n");
		
		return sb.toString(); 
	}


	
}
