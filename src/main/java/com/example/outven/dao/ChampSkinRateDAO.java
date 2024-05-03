package com.example.outven.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Champskinrate;
import com.example.outven.repository.ChampSkinRateRepository;


@Repository
public class ChampSkinRateDAO {
	@Autowired
	ChampSkinRateRepository champSkinRateRepository;

	// 스킨 평점 입력
	public boolean skinRateWrite(Champskinrate champskinrate) {
		// System.out.println("test1 : " + champskinrate.toString());
		boolean result = false;
		Champskinrate skinRate_insert = champSkinRateRepository.save(champskinrate);
		// System.out.println("test2 : " + skinRate_insert.toString());
		if (skinRate_insert != null) {
			result = true;
		}
		return result;
	}
	
	// 평점 평균 구하는 함수
	public double skinRateAvg(int champ_code, int skin_code) {
		double avg = 0;
		System.out.println("test");
		System.out.println(champ_code + ", " + skin_code);
		// 일치하는 챔프 평점의 합
		int sum = champSkinRateRepository.sumRateByChampCode(champ_code, skin_code);
		int count = champSkinRateRepository.countRateByChampCode(champ_code, skin_code);
		avg = (double)sum/count;
		return avg;
	}
	
	// 평점 주었는지 안주었는지 확인하는 함수
	public boolean skinRateCheck(int champ_code, int skin_code, String member_id) {
		boolean result = false;
		System.out.println("skinRateCheckDAO 진입 테스트");
		
		Champskinrate ratecheck = champSkinRateRepository.findByChampcodeAndSkincodeAndMemberid(champ_code, skin_code, member_id);
		
		// 값이 있을때
		if(ratecheck != null) {
			result = true;
		}
		return result;
	}
	
}
