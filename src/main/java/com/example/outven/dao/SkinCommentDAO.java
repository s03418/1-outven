package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Join_comskin;
import com.example.outven.entity.Skin_comment;
import com.example.outven.repository.SkinCommentRepository;

@Repository
public class SkinCommentDAO {
	@Autowired
	SkinCommentRepository skinCommentRepository;
	
	// 스킨 코드가 일치한 댓글들 10개 조회
	public List<Skin_comment>  skinCommentList(int startnum, int endnum, int skin_code){
		return skinCommentRepository.findAllBySkin_code(startnum, endnum, skin_code);
	}
	
	// 챔프 코드가 일치한 데이터의 수
	public int getCountChamp(int champ_code) {
		return skinCommentRepository.countByChamp_code(champ_code);
	}
	
	// 스킨 코드가 일치한 데이터의 수
	public int getCountSkin(int skin_code) {
		return skinCommentRepository.countBySkin_code(skin_code);
	}
	
	// 스킨 댓글 입력
	public boolean skinComWrite(Skin_comment comment) {
		System.out.println("write test");
		boolean result = false;
		// 값을 입력하고 입력 성공시 skin_re_ref에 insert 시퀀스 값을 업데이트
		Skin_comment skin_insert = skinCommentRepository.save(comment);
		if(skin_insert != null) {
			// 값이 있을 시 skin_re_ref 값에 시퀀스 값을 넣고 업데이트
			skin_insert.setSkin_re_ref(skin_insert.getSkin_com_num());
			Skin_comment skin_update = skinCommentRepository.save(skin_insert);
			if(skin_update != null) result = true;
		}
		
		return result;
	}
	// 댓글 수정
	
	// 댓글 삭제
	
	// 답글
	public boolean skinRecomment(Skin_comment comment) {
		
		// 진입 확인
		System.out.println("skinRecommnetTest!");
		boolean result = false;
		
		// 값이 왔는지 확인
		System.out.println("DAO test : " + comment.toString());
		int re_ref = comment.getSkin_re_ref();
		int re_lev = comment.getSkin_re_lev();
		int re_seq = comment.getSkin_re_seq();
		// 조건에 맞는 값 검색후 일치하면 값 변경
		int update = skinCommentRepository.updateSkinseq(re_ref, re_seq);
		System.out.println(update);
		System.out.println("-------------------");
		
		// 매개변수 skin_re_seq 와 skin_re_lev값을 1개 증가
		re_lev +=1;
		re_seq +=1;
		System.out.println("re_lev = " + re_lev);
		System.out.println("re_seq = " + re_seq);
		
		// 변경할 값 집어넣고 null 아니면 true 값을 반환
		comment.setSkin_re_lev(re_lev);
		comment.setSkin_re_seq(re_seq);
		
		Skin_comment ins_Skin_comment = skinCommentRepository.save(comment);
		if(ins_Skin_comment != null) {
			result = true;
		}
		
		return result;
	}
	
}
