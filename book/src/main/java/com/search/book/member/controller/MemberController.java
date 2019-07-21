package com.search.book.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.search.book.member.entity.Member;
import com.search.book.member.repo.MemberJpaRepo;
import com.search.book.member.service.MemberService;

@RestController
@RequestMapping(value = "/user")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	
	@GetMapping("/member")
	public List<Member> findAllUser(){
		return memberService.findAllUser();
	}
	
	
	/**
	 * TODO
	 * 회원가입 정보 받아서 저장시키기
	 */
	@PostMapping("/member")
	public Member save() {

		/*
		 * User 정보 비밀번호 암호화 처리 
		 * 암호화는 어떻게 할것인지..!? 
		 */
		Member member = Member.builder()
				.id("nuri@naver.com")
				.password("1234")
				.build();
		
		return memberService.saveMember(member);
	}
	
}
