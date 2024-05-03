package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Skin_comment;

import jakarta.transaction.Transactional;

public interface SkinCommentRepository extends JpaRepository<Skin_comment, Integer> {	
	// 스킨 코드가 일치한 댓글들 보기
	@Query(value = "select * from (select rownum rn, tt.* from"
			+ "(select * from skin_comment where skin_code = :skin_code order by skin_re_ref desc, skin_re_seq asc) tt)"
			+ "where rn>= :startnum and rn<= :endnum",
				nativeQuery = true)
	List<Skin_comment> findAllBySkin_code(
			@Param("startnum")int startnum,
			@Param("endnum")int endnum,
			@Param("skin_code")int skin_code);
	
	// 챔피언 코드와 일치한 댓글수
	@Query(value = "select count(*) as cnt from skin_comment where champ_code = :champ_code",
					nativeQuery = true)
	int countByChamp_code(@Param("champ_code")int champ_code);
	
	// 스킨 코드와 일치한 댓글수
	@Query(value = "select count(*) as cnt from skin_comment where skin_code = :skin_code",
				nativeQuery = true)
	int countBySkin_code(@Param("skin_code")int skin_code);
	
	
	// insert, update, delete를 @Query에서 사용시 주의점(@Transactional, @Modifying 어노테이션 적용
	@Transactional
	@Modifying
	@Query(value = "update skin_comment set skin_re_seq= skin_re_seq+1"
			+ "where skin_re_ref= :skin_re_ref and skin_re_seq > :skin_re_seq ",
			nativeQuery = true)
	int updateSkinseq(
			@Param("skin_re_ref")int skin_re_ref,
			@Param("skin_re_seq")int skin_re_seq);
	
}	
