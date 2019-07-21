package com.search.book.member.service;

import java.util.List;

import com.search.book.member.entity.Member;

public interface MemberService {

	public Member getMemberByUserName(String userId);

	public List<Member> findAllUser();

	public Member saveMember(Member member);

}
