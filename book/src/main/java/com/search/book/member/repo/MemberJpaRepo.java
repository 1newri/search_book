package com.search.book.member.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.search.book.member.entity.Member;

@Repository
public interface MemberJpaRepo extends JpaRepository<Member, Long>{

	Member findById(String id);

}
 