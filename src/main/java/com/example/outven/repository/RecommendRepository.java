package com.example.outven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {
	// 추천 유무 확인 값이 있으면 이미 준 것
	@Query(value = "select * from recommend where board_num = :board_num and member_id = :member_id",
			nativeQuery = true)
	Recommend findByBoardnumAndMemberid(
			@Param("board_num")int board_num,
			@Param("member_id")String member_id);
	
}
