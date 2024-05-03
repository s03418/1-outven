package com.example.outven.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Member;

// JpaRepository<관리대상 Entity, 대표값 자료형(primary Key)>
// JpaRepository를 상속받으면 자동적으로 bean 객체로 동작된다.
public interface MemberRepository extends JpaRepository<Member, String> {
	
	// 이메일 인증 중복 검사
	@Query(value = "select * from member "
				 + "where email = :email", nativeQuery = true)
	Member findByEmail(@Param("email") String email);
	
	// 해당 아이디와 이메일로 가입된 멤버가 있는지 검색
	@Query(value = "select * from member "
				 + "where member_id = :id and email = :email", nativeQuery = true)
	Member findByIdAndEmail(@Param("id") String id,
							@Param("email") String email);
	
	// 해당 닉네임이 있는지 검색 (닉네임 중복 검사)
	@Query(value = "select * from member "
			 + "where nick_name = :nick", nativeQuery = true)
	Member findByNick(@Param("nick") String nick);

	// 아이디 이메일 맞는 유저 검색
	@Query(value = "select * from member "
			 + "where membername = :name and email = :email", nativeQuery = true)
	Member findByNameEmail(@Param("name") String name,
						   @Param("email") String email);
	
	// 아이디 비밀번호 맞는 유저 검색
	@Query(value = "select * from member "
			 + "where member_id = :id and user_password  = :password", nativeQuery = true)
	Member findByIdAndPassword(@Param("id") String id,
						   	   @Param("password") String password);
}
