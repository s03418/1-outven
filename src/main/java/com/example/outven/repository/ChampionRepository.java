package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.outven.entity.Champion;

public interface ChampionRepository extends JpaRepository<Champion, Integer> {
	@Query(value = "select * from champion order by champ_code",
			nativeQuery = true)
	List<Champion> findAllOrderByChamp_Code();
}
