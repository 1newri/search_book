package com.search.book.common.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SnsProfileDto {
	private String userAccessToken; 
	private String snsType;  //naver, google, kakao, facebook
	private String snsIdSeq; 
	private String name; 
	private String email; 
	private boolean emailVerified;    //verified
	private String ageRage;   //40-49
	private String gender; 
	private String nickName;  //별명 카카오개발시 추가함. 2019.02.28
}
