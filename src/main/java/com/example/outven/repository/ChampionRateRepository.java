package com.example.outven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Championrate;

public interface ChampionRateRepository extends JpaRepository<Championrate, Integer> {
	// 특정 챔피언 점수의 합
	@Query(value = "select sum(rate) from championRate where champ_code = :champ_code",
					nativeQuery = true)
	int sumRateByChampCode(@Param("champ_code")int champ_code);
	
	// 특정 챔피언의 수
	@Query(value = "select count(*) from championRate where champ_code = :champ_code",
					nativeQuery = true)
	int countRateByChampCode(@Param("champ_code")int champ_code);
	
	// 평점 주웠는지 유무 확인(조회가 되면 중복됨)
	@Query(value = "select * from championRate where champ_code = :champ_code and member_id = :member_id",
						nativeQuery = true)
	Championrate findByChampcodeAndMemberid(@Param("champ_code")int champ_code,
											@Param("member_id")String member_id);
}
