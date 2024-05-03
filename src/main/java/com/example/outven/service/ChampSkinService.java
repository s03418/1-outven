package com.example.outven.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.outven.dao.ChampSkinDAO;
import com.example.outven.dao.ChampSkinRateDAO;
import com.example.outven.dao.ChampionDAO;
import com.example.outven.dao.JoinComSkinDAO;
import com.example.outven.dao.SkinCommentDAO;
import com.example.outven.entity.Champ_skin;
import com.example.outven.entity.Champion;
import com.example.outven.entity.Champskinrate;
import com.example.outven.entity.Join_comskin;
import com.example.outven.entity.Skin_comment;

// 챔피언 스킨
@Service
public class ChampSkinService {
	@Autowired
	private ChampionDAO champ_dao;

	@Autowired
	private ChampSkinDAO skinDAO;

	@Autowired
	private ChampSkinRateDAO skinRateDAO;

	@Autowired
	private SkinCommentDAO skinCommentDAO;
	
	@Autowired
	private JoinComSkinDAO joinDAO;

	// 목록보기
	public List<Champion> champList() {
		return champ_dao.champList();
	}

///////////////////////////////////

//챔피언 스킨

//특정 챔피언의 스킨 리스트
	public List<Champ_skin> champSkinList(int champ_code) {
		return skinDAO.champSkinList(champ_code);
	}

//챔피언 상세보기
	public Champ_skin champSkinView(int champ_code, int skin_code) {
		return skinDAO.champSkinView(champ_code, skin_code);
	}

//챔피언 스킨 평점 업데이트
	public boolean skinRateUpdate(Champ_skin champskin, double avg) {
		return skinDAO.skinRateUpdate(champskin, avg);
	}

//스킨 평점 입력
	public boolean skinRateWrite(Champskinrate champ_skin_rate) {
		return skinRateDAO.skinRateWrite(champ_skin_rate);
	}

//스킨 평점 평균 구하는 함수
	public double skinRateAvg(int champ_code, int skin_code) {
		return skinRateDAO.skinRateAvg(champ_code, skin_code);
	}
	
	// 평점 주었는지 안주었는지 확인하는 함수
	public boolean skinRateCheck(int champ_code, int skin_code, String member_id) {
		return skinRateDAO.skinRateCheck(champ_code, skin_code, member_id);
	}

	///////////////////////////////////////////

	//챔피언 스킨 댓글

	// 챔피언 코드가 일치한 댓글들 10개 조회
	public List<Join_comskin> champSkinCommentList(int startnum, int endnum, int champ_code) {
		return joinDAO.champSkinCommentList(startnum, endnum, champ_code);
	}

	//스킨 코드가 일치한 댓글들 10개 조회
	public List<Skin_comment> skinCommentList(int startnum, int endnum, int skin_code) {
		return skinCommentDAO.skinCommentList(startnum, endnum, skin_code);
	}
	
	// 챔프 코드가 일치한 데이터의 수
	public int getCountChamp(int champ_code) {
		return skinCommentDAO.getCountChamp(champ_code);
	}
	
	// 스킨 코드가 일치한 데이터의 수
	public int getCountSkin(int skin_code) {
		return skinCommentDAO.getCountSkin(skin_code);
	}
	
	// 스킨 댓글 입력
	public boolean skinComWrite(Skin_comment comment) {
		return skinCommentDAO.skinComWrite(comment);
	}
	
	// 답글
	public boolean skinRecomment(Skin_comment comment) {
		return skinCommentDAO.skinRecomment(comment);
	}
	

}
