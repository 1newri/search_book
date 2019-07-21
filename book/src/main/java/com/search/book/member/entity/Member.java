package com.search.book.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long msrl;
	
	@Column(nullable = false, unique = true, length = 30)
	private String id;
	
	/*
	  TODO
	  비밀번호 암호와 처리 되어야함
	 */
	@Column(nullable = false, length = 100)
	private String password; 
	
}
