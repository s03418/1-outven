package com.example.outven.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Boardreport;
import com.example.outven.repository.BoardreportRepository;

@Repository
public class BoardreportDAO {
	@Autowired
	BoardreportRepository boardreportRepository;
	
	// 신고 입력하기
	public boolean reportWrite(Boardreport boardreport) {
		boolean result = false;
		System.out.println("DAO Report 접근확인");
		System.out.println(boardreport.toString());
		
		Boardreport board_insert = boardreportRepository.save(boardreport);
		
		if(board_insert != null) {
			result = true;
		}
		
		return result;
	}
	// 중복값 있는지 검사
	public boolean reportCheck(int board_num, String member_id) {
	boolean result = false;
		// 출력 확인
		System.out.println("recomCheck 접근 테스트");
		
		System.out.println("board_num : " + board_num + " member_id : " + member_id);
		
		Boardreport reCheck = boardreportRepository.findByBoardnumAndMemberid(board_num, member_id);
		
		
		if(reCheck != null) {
			result = true;
		}
		
		return result;
	}
}
