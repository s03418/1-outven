package com.example.outven.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Championrate;
import com.example.outven.repository.ChampionRateRepository;

// 챔피언 평점 DAO
@Repository
public class ChampionRateDAO {
	@Autowired
	ChampionRateRepository championRateRepository;
	
	// 평점 입력
	public boolean champRateWrite(Championrate championRate) {
		System.out.println("test1 : " + championRate.toString());
		boolean result = false;
		Championrate champRate_insert = championRateRepository.save(championRate);
		System.out.println("test2 : " + champRate_insert.toString());
		if(champRate_insert != null) {
			result = true;
		}
		return result;
	}
	
	// 평점 평균 구하는 함수
	public double champRateAvg(int champ_code) {
		System.out.println("test");
		System.out.println(champ_code);
		// 일치하는 챔프 평점의 합
		int sum = championRateRepository.sumRateByChampCode(champ_code);
		System.out.println(sum);
		// 일치하는 챔프 평점 인원
		int count = championRateRepository.countRateByChampCode(champ_code);
		System.out.println(count);
		double avg = (double)sum/count;
		
		return avg;
	}
	
	// 평점 주었는지 안주었는지 확인하는 함수
	public boolean champRateCheck(int champ_code, String member_id) {
		boolean result = false;
		// System.out.println("ratecheckDAO 진입 테스트");
		
		Championrate ratecheck = championRateRepository.findByChampcodeAndMemberid(champ_code, member_id);
		
		// 값이 있을때 
		if(ratecheck != null) {
			result = true;
		}
		
		return result;
	}
	
	
}
