package com.example.outven.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Board;
import com.example.outven.entity.Member;

// JpaRepository<관리대상 Entity, 대표값 자료형(primary Key)>
// JpaRepository를 상속받으면 자동적으로 bean 객체로 동작된다.
public interface AdminRepository extends JpaRepository<Member, String> {
	
	// 유저, 관리자 페이징 검색
	@Query(value="select * from (select rownum rn, tt.* from "
			+ "(select * from member where user_role = :role order by member_id) tt) "
			+ "where rn>=:startnum and rn<=:endnum", 
			nativeQuery = true)
	List<Member> findByRoleAndStartnumAndEndnum(
			@Param("role") String role,
			@Param("startnum") int startnum, 
			@Param("endnum") int endnum);
	
	// 블랙리스트 페이징 검색
	@Query(value="select * from (select rownum rn, tt.* from "
			+ "(select * from blacklist order by member_id) tt) "
			+ "where rn>=:startnum and rn<=:endnum", 
			nativeQuery = true)
	List<Member> findByStartnumAndEndnumBlacklist(
			@Param("startnum") int startnum, 
			@Param("endnum") int endnum);
	
	// 회원등급이 유저 or 관리자 데이터 숫자
	@Query(value = "select count(*) from member "
			 + "where user_role = :role", nativeQuery = true)
	int findByRole1(@Param("role") String role);
	
	// 회원등급이 유저 or 관리자 조회
	@Query(value = "select * from member "
			 + "where user_role = :role", nativeQuery = true)
	List<Member> findByRole2(@Param("role") String role);
}
