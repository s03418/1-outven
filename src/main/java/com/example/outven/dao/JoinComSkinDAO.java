package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Join_comskin;
import com.example.outven.repository.JoinComSkinRepository;

@Repository
public class JoinComSkinDAO {
	@Autowired
	JoinComSkinRepository comSkinRepository;

	// 챔피언 코드가 일치한 댓글들 10개 조회
	public List<Join_comskin> champSkinCommentList(int startnum, int endnum, int champ_code) {
		return comSkinRepository.findAllByChamp_CodeI(startnum, endnum, champ_code);
	}
}
