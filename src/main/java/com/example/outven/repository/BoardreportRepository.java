package com.example.outven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Boardreport;

public interface BoardreportRepository extends JpaRepository<Boardreport, Integer> {
	// 아이디와 게시글 번호가 일치한 값 조회
	@Query(value = "select * from Boardreport where board_num = :board_num and member_id = :member_id",
			nativeQuery = true)
	Boardreport findByBoardnumAndMemberid(
			@Param("board_num")int board_num,
			@Param("member_id")String member_id);
}
