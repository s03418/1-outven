package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Champion_comment;
import com.example.outven.repository.ChampionCommentRepository;

@Repository
public class ChampionCommentDAO {
	@Autowired
	ChampionCommentRepository commentRepository;
	// 챔피언 댓글 DAO
	
	// 댓글 리스트 :챔프 코드와 일치한 댓글들을 10개 조회
	public List<Champion_comment> champCommentList(int startnum, int endnum, int champ_code){
		return commentRepository.findAllByChamp_Code(startnum, endnum, champ_code);
	}
	
	// 챔프 코드와 일치한 데이터수를 구함
	public int getCount(int champ_code) {
		return commentRepository.countByChamp_code(champ_code);
	}
	// 댓글 입력
	public boolean commentWrite(Champion_comment comment) {
		// System.out.println("test");
		boolean result = false;
		// 한번 값을 입력하고 입력성공시 com_re_ref에 insert 시퀀스 값을 업데이트
		Champion_comment comment_insert = commentRepository.save(comment);
		if(comment_insert != null ) {
			// 값이 있을 시 com_re_ref 값에 시퀀스 값을 넣고 업데이트
			comment_insert.setCom_re_ref(comment_insert.getChamp_com_num());
			Champion_comment comment_update = commentRepository.save(comment_insert);
			if(comment_update != null) result = true;
		}
		
		return result;
	}
	// 댓글 수정
	
	// 댓글 삭제
	
	// 답글
	public boolean recomment(Champion_comment comment) {
		// System.out.println(commentRepository);
		// System.out.println("test");
		boolean result = false;
		// db에 com_re_ref 값이 일치한 값들을 가져옴
//		List<Champion_comment> comList = commentRepository.findAllByCom_re_ref(comment.getCom_re_ref());
//		// null이 아닐때 반복문 돌리고 db안에의 com_re_seq가 매개변수 com_re_seq보다 크면 com_re_seq값을 1씩 증가(update)함
//		if(comList != null) {
//			System.out.println("test2");
//			for(int i =0; i<comList.size(); i++) {
//				if(comList.get(i).getCom_re_seq() > comment.getCom_re_seq()) {
//					
//				}
//			}
//		}
		// System.out.println(comment.toString());
		
		int re_ref = comment.getCom_re_ref();
		int re_lev = comment.getCom_re_lev();
		int re_seq = comment.getCom_re_seq();
		// System.out.println("re_lev = " + re_lev);
		// System.out.println("re_seq = " + re_seq);
		
		// 조건에 맞는 값 검색후 일치하면 값 변경
		int update = commentRepository.updateComreseq(re_ref, re_seq);
		// System.out.println(update);
		// System.out.println(result);
		// System.out.println("------");
		// 매개변수 com_re_seq 와 com_re_lev값을 1개 증가
		re_lev +=1;
		re_seq +=1;
		// System.out.println("re_lev = " + re_lev);
		// System.out.println("re_seq = " + re_seq);
		
		// 변경할 값 집어넣고 null 아니면 true 값을 반환
		comment.setCom_re_lev(re_lev);
		comment.setCom_re_seq(re_seq);
		
		// comment.toString();
		
		Champion_comment ins_comment = commentRepository.save(comment);
		if(ins_comment != null) {
			result = true;
		}
		return result;
	}
}
