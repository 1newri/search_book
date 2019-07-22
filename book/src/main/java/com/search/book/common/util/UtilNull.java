package com.search.book.common.util;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UtilNull {


	private static final Logger log = LoggerFactory.getLogger(UtilNull.class);
	

	/**
	 * null 또는 공백(isEmpty) 체크 결과를 리턴함. 
	 * @param request
	 */
	public static boolean isEmpty(Object obj)  {

		
		if ( obj == null ) return true; 
		
		//String 또는 Collection isempty
		if ( obj instanceof String ) {
			return ( (String)obj).isEmpty(); 
		} else if ( obj instanceof Collection<?>) {
			return  ( (Collection<?>)obj).isEmpty(); 
		} else {
			log.debug("isEmpty Not Check object type : {}", obj.getClass());
		}
		
		return false; 
	}


	/**
	 * null 일경우 공백을 리턴함. 
	 * @param orgString
	 * @return
	 */
	public static String nvl(String orgString) {
		
		String defaultString = ""; 
		
		return nvl(orgString, defaultString);
	}
	/**
	 * null 일경우 대체문자를 리턴함. 
	 * @param orgString
	 * @param defaultString
	 * @return
	 */
	public static String nvl(String orgString, String defaultString) {
		
		
		return  (null == orgString ) ? defaultString : orgString ; 
	}

}
