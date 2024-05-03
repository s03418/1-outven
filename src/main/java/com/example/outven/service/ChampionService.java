package com.example.outven.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.outven.dao.ChampionCommentDAO;
import com.example.outven.dao.ChampionDAO;
import com.example.outven.dao.ChampionRateDAO;
import com.example.outven.entity.Champion;
import com.example.outven.entity.Champion_comment;
import com.example.outven.entity.Championrate;

// 챔피언 정보 서비스
@Service
public class ChampionService {

	@Autowired
	private ChampionDAO champ_dao;

	@Autowired
	private ChampionRateDAO champRate_dao;

	@Autowired
	private ChampionCommentDAO commentDAO;

	// champ_dao 부분
	
	// 목록보기
	public List<Champion> champList() {
		return champ_dao.champList();
	}

	// 상세보기
	public Champion championView(int champion_code) {
		return champ_dao.championView(champion_code);
	}
	
	// 챔피언 평점 업데이트
	public boolean champRateUpdate(int champ_code, double avg) {
		return champ_dao.champRateUpdate(champ_code, avg);
	}

	
	// champRate_dao 부분
	
	// 평점 입력
	public boolean champRateWrite(Championrate championRate) {
		return champRate_dao.champRateWrite(championRate);
	}

	// 평점 평균 구하기
	public double champRateAvg(int champ_code) {
		return champRate_dao.champRateAvg(champ_code);
	}

	// 평점 주었는지 안주었는지 확인하는 함수
	public boolean champRateCheck(int champ_code, String member_id) {
		return champRate_dao.champRateCheck(champ_code, member_id);
	}

	
	// commentDAO 부분
	
	// 댓글 리스트 :챔프 코드와 일치한 댓글들을 10개 조회
	public List<Champion_comment> champCommentList(int startnum, int endnum, int champ_code) {
		return commentDAO.champCommentList(startnum, endnum, champ_code);
	}

	// 챔프 코드와 일치한 데이터수를 구함
	public int getCount(int champ_code) {
		return commentDAO.getCount(champ_code);
	}

	// 댓글 입력
	public boolean commentWrite(Champion_comment comment) {
		return commentDAO.commentWrite(comment);
	}

	// 답글
	public boolean recomment(Champion_comment comment) {
		return commentDAO.recomment(comment);
	}

}
