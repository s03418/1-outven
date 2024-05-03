package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Join_comskin;

public interface JoinComSkinRepository extends JpaRepository<Join_comskin, Integer> {
	// 챔피언 코드가 일치한 댓글 + 아이콘들 보기
	@Query(value = "select * from (select rownum rn, tt.* from "
			+ "(select a.skin_com_num, a.champ_code, a.skin_code, a.member_id, a.nick_name, a.champskin_com , a.comment_logtime, b.champ_icon_img, b.champ_skin_rate, b.champ_skin_name "
			+ "from skin_comment a inner join champ_skin b on "
			+ "a.skin_code = b.skin_code where a.champ_code = :champ_code order by a.skin_com_num desc) tt) "
			+ "where rn>= :startnum and rn<= :endnum",
				nativeQuery = true)
	List<Join_comskin> findAllByChamp_CodeI(
			@Param("startnum")int startnum,
			@Param("endnum")int endnum,
			@Param("champ_code")int champ_code);

}
