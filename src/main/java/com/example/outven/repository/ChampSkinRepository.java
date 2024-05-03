package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Champ_skin;


public interface ChampSkinRepository extends JpaRepository<Champ_skin, Integer> {
	// 챔피언 코드가 일치한 데이터를 구함
	@Query(value = "select * from champ_skin where champ_code = :champ_code",
			nativeQuery = true)
	List<Champ_skin> findAllByChamp_Code(@Param("champ_code")int champ_code);
	
	// 챔피언 코드와 스킨 코드의 값이 일치한 데이터를 구함
	@Query(value = "select * from champ_skin where champ_code = :champ_code and skin_code = :skin_code",
			nativeQuery = true)
	Champ_skin findByChampcodeAndSkincode(
			@Param("champ_code")int champ_code,
			@Param("skin_code")int skin_code);
}
