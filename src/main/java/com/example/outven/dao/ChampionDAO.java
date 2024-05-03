package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Champion;
import com.example.outven.entity.Championrate;
import com.example.outven.repository.ChampionRepository;

@Repository
public class ChampionDAO {
	@Autowired
	ChampionRepository championRepository;
	
	// 챔피언 목록
	public List<Champion> champList(){
		return championRepository.findAllOrderByChamp_Code();
	}
	
	// 챔피언 상세 보기
	public Champion championView(int champ_code) {
		return championRepository.findById(champ_code).orElse(null);
	}
	
	// 챔피언 평점 업데이트
	public boolean champRateUpdate(int champ_code, double avg) {
		boolean result = false;
		
		// 일치한 챔피언 코드를 찾음
		Champion champion = championRepository.findById(champ_code).orElse(null);
		Champion champion_update = null;
		
		// 값이 존재할시
		if(champion != null) {
			// 값 변경
			champion.setChamp_rate(avg);
			
			// 값 저장
			champion_update = championRepository.save(champion);
			
			if(champion_update != null) {
				result = true;
			}
		}else {
			System.out.println("변경 실패");
		}
		
		return result;
	}
	
	
}
