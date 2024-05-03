package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Board_comment;

import jakarta.transaction.Transactional;

public interface BoardCommentRepository extends JpaRepository<Board_comment, Integer>{
	// 게시글 번호와 일치한 댓글 보기
	@Query(value = "select * from (select rownum rn, tt.* from"
			+ "	(select * from board_comment where board_num = :board_num order by com_re_ref asc, com_re_seq asc) tt)"
			+ "	where rn>= :startnum and rn<= :endnum",
			 nativeQuery = true)
	List<Board_comment> findAllByBoard_num(
			@Param("startnum")int startnum,
			@Param("endnum")int endnum,
			@Param("board_num")int board_num);
	
	// 게시글 번호와 일치한 댓글수
	@Query(value = "select count(*) as cnt from board_comment where board_num = :board_num",
			nativeQuery = true)
	int countByBoard_num(@Param("board_num")int board_num);
	
	// insert, update, delete를 @Query에서 사용시 주의점(@Transactional, @Modifying 어노테이션 적용
	@Transactional
	@Modifying
	@Query(value = "update board_comment set com_re_seq= com_re_seq+1"
			+ "where com_re_ref= :com_re_ref and com_re_seq > :com_re_seq ",
			nativeQuery = true)
	int updateReseq(
			@Param("com_re_ref")int com_re_ref,
			@Param("com_re_seq")int com_re_seq);
	

}
