package com.example.outven.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Recommend;
import com.example.outven.repository.RecommendRepository;

@Repository
public class RecommendDAO {
	@Autowired
	RecommendRepository recommendRepository;
	
	// 추천 테이블 삽입
	public boolean insertRecomment(Recommend recommend) {
		boolean result = false;
		System.out.println("recommend 진입 테스트");
		System.out.println(recommend.toString());
		Recommend ins_recommend = recommendRepository.save(recommend);
		
		if(ins_recommend != null) {
			result = true;
		}
		
		return result;
	}
	
	// 추천 주었는지 확인
	public boolean recomCheck(int board_num, String member_id) {
		boolean result = false;
		// 출력 확인
		System.out.println("recomCheck 접근 테스트");
		
		System.out.println("board_num : " + board_num + " member_id : " + member_id);
		
		Recommend reCheck = recommendRepository.findByBoardnumAndMemberid(board_num, member_id);
		
		if(reCheck != null) {
			result = true;
		}
		
		return result;
	}
}
