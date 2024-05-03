package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Champion_comment;

import jakarta.transaction.Transactional;

public interface ChampionCommentRepository extends JpaRepository<Champion_comment, Integer> {
	// 챔피언 코드가 일치한 댓글들 보기
	@Query(value = "select * from (select rownum rn, tt.* from"
			+ "(select * from champion_comment where champ_code = :champ_code order by com_re_ref desc, com_re_seq asc) tt)"
			+ "where rn>= :startnum and rn<= :endnum",
				nativeQuery = true)
	List<Champion_comment> findAllByChamp_Code(
			@Param("startnum")int startnum,
			@Param("endnum")int endnum,
			@Param("champ_code")int champ_code);
	// 챔피언 코드와 일치한 댓글수
	@Query(value = "select count(*) as cnt from champion_comment where champ_code = :champ_code",
				nativeQuery = true)
	int countByChamp_code(@Param("champ_code")int champ_code);
	
	// com_re_ref 일치한 데이터들
//	@Query(value = "select * from champion_comment where com_re_ref = :com_re_ref",
//				nativeQuery = true)
//	List<Champion_comment> findByComreref(
//			@Param("com_re_ref")int com_re_ref);
	
	// insert, update, delete를 @Query에서 사용시 주의점(@Transactional, @Modifying 어노테이션 적용
	@Transactional
	@Modifying
	@Query(value = "update champion_comment set com_re_seq = com_re_seq+1"
			+ "where com_re_ref= :com_re_ref and com_re_seq > :com_re_seq ",
			nativeQuery = true)
	int updateComreseq(
			@Param("com_re_ref")int com_re_ref,
			@Param("com_re_seq")int com_re_seq);
	
	// 조건에 com_re_ref가 일치하고 com_re_seq가 매개변수보다 클때 맞는 값 출력
//	@Query(value = "select * from champion_comment where com_re_ref= :com_re_ref and com_re_seq > :com_re_seq",
//			nativeQuery = true)
//	List<Champion_comment> findAllByCom_re_ref(
//			@Param("com_re_ref")int com_re_ref,
//			@Param("com_re_seq")int com_re_seq);
}
