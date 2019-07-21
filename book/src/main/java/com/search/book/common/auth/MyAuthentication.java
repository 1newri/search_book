package com.search.book.common.auth;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.search.book.member.entity.Member;

public class MyAuthentication extends UsernamePasswordAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	Member member;
	
	public MyAuthentication(String userId, String password, List<GrantedAuthority> authList, Member member) {
		super(userId, password, authList);
		this.member = member;
	}

}
