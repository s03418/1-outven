package com.example.outven.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Blacklist;
import com.example.outven.entity.Member;

// JpaRepository<관리대상 Entity, 대표값 자료형(primary Key)>
// JpaRepository를 상속받으면 자동적으로 bean 객체로 동작된다.
public interface BlacklistRepository extends JpaRepository<Blacklist, String> {
	
}
