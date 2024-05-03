package com.example.outven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Champskinrate;

public interface ChampSkinRateRepository extends JpaRepository<Champskinrate, Integer> {
	// 특정 챔피언 점수의 합
	@Query(value = "select sum(rate) from Champskinrate where champ_code = :champ_code and skin_code = :skin_code",
					nativeQuery = true)
	int sumRateByChampCode(@Param("champ_code")int champ_code,
							@Param("skin_code")int skin_code);
	
	// 특정 챔피언의 수
	@Query(value = "select count(*) from Champskinrate where champ_code = :champ_code and skin_code = :skin_code",
					nativeQuery = true)
	int countRateByChampCode(@Param("champ_code")int champ_code,
							@Param("skin_code")int skin_code);
	
	// 평점 주었는지 유무 확인(조회되면 중복됨)
	@Query(value = "select * from Champskinrate where champ_code = :champ_code and skin_code = :skin_code and member_id = :member_id",
				nativeQuery = true)
	Champskinrate findByChampcodeAndSkincodeAndMemberid(
			@Param("champ_code")int champ_code,
			@Param("skin_code")int skin_code,
			@Param("member_id")String member_id);
}
