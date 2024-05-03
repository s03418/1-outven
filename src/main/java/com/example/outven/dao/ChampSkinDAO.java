package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Champ_skin;
import com.example.outven.repository.ChampSkinRepository;

@Repository
public class ChampSkinDAO {
	@Autowired
	ChampSkinRepository champSkinRepository;
	
	// 특정 챔피언의 스킨 리스트
	public List<Champ_skin> champSkinList(int champ_code){
		return champSkinRepository.findAllByChamp_Code(champ_code);
	}
	
	// 챔피언 상세보기
	public Champ_skin champSkinView(int champ_code, int skin_code) {
		return champSkinRepository.findByChampcodeAndSkincode(champ_code, skin_code);
	}
	
	// 챔피언 스킨 평점 업데이트
	public boolean skinRateUpdate(Champ_skin champskin, double avg) {
		boolean result = false;
		
		// 챔피언 코드와 스킨 코드가 일치한 값을 찾음
		System.out.println(champskin.toString());
		Champ_skin champskin_update = null;
		
		// 값이 존재할시
		if(champskin != null) {
			// 값변경
			champskin.setChamp_skin_rate(avg);
			System.out.println(champskin.toString());
			// 값저장
			champskin_update = champSkinRepository.save(champskin);
			System.out.println(champskin_update.toString());
			
			if(champskin_update != null) {
				result = true;
			}
			
		}
		return result;
	}
	
}
