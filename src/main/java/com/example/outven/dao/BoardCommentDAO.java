package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Board_comment;
import com.example.outven.repository.BoardCommentRepository;

@Repository
public class BoardCommentDAO {
	@Autowired
	BoardCommentRepository boardCommentRepository;
	
	// 게시판 댓글 DAO
	// 댓글 리스트 : 게시판 번호와 일치한 댓글 10개 조회
	public List<Board_comment> boardCommentList(int startnum, int endnum, int board_num){
		return boardCommentRepository.findAllByBoard_num(startnum, endnum, board_num);
	}
	
	// 게시판 번호와 일치한 데이터 수를 구함
	public int getCount(int board_num) {
		return boardCommentRepository.countByBoard_num(board_num);
	}
	
	// 댓글 입력
	public boolean commentWrite(Board_comment comment) {
		System.out.println("commentWrite test!");
		boolean result = false;
		
		Board_comment comment_insert = boardCommentRepository.save(comment);
		if(comment_insert != null) {
			// 값이 있을 시 com_re_ref 값에 시퀀스 값을 넣고 업데이트
			comment_insert.setCom_re_ref(comment_insert.getComment_num());
			Board_comment comment_update = boardCommentRepository.save(comment_insert);
			if(comment_update != null) result = true;
		}
		
		return result;
	}
	
	// 답글
	public boolean recomment(Board_comment comment) {
		boolean result = false;
		System.out.println("recomment 진입 테스트");
		
		int re_ref = comment.getCom_re_ref();
		int re_lev = comment.getCom_re_lev();
		int re_seq = comment.getCom_re_seq();
		
		// 조건에 맞는 값 검색후 일치하면 값 변경
		int update = boardCommentRepository.updateReseq(re_ref, re_seq);
		
		// 매개변수 com_re_seq 와 com_re_lev값을 1개 증가
		re_lev +=1;
		re_seq +=1;
		
		// 변경할 값 집어넣고 null 아니면 true 값을 반환
		comment.setCom_re_lev(re_lev);
		comment.setCom_re_seq(re_seq);
		
		Board_comment ins_comment = boardCommentRepository.save(comment);
		if(ins_comment != null) {
			result = true;
		}
		
		return result;
	}
	
	// 댓글 삭제 처리
	public boolean commentDelete(int comment_num) {
		// 데이터 있는지 조회
		System.out.println("진입 확인");
		Board_comment comment = boardCommentRepository.findById(comment_num).orElse(null);
		System.out.println(comment.toString());
		if(comment != null) {
			// 삭제
			boardCommentRepository.delete(comment);
		}
		
		
		return boardCommentRepository.existsById(comment_num);
	}
}
