package com.search.book.member.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.search.book.member.entity.Member;
import com.search.book.member.repo.MemberJpaRepo;
import com.search.book.member.service.MemberService;

@Service
public class MemberSerivceImpl implements MemberService{

	@Autowired
	MemberJpaRepo memberJpaRepo;
	
	@Override
	public Member getMemberByUserName(String userId) {
		return memberJpaRepo.findById(userId);
	}

	@Override
	public List<Member> findAllUser() {
		return memberJpaRepo.findAll();
	}

	@Override
	public Member saveMember(Member member) {
		return memberJpaRepo.save(member);
	}

}
