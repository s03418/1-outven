package com.example.outven.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Board;

public interface imageBoardRepository extends JpaRepository<Board, Integer> {
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByStartnumAndEndnum(@Param("startnum") int startnum, @Param("endnum") int endnum);

	// 카테고리 기능
	// detail_category=> null값이면
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board where board_category= :board_category " + "order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum ", nativeQuery = true)
	List<Board> findByBoard_categoryAndStartnumAndEndnum(@Param("board_category") String board_category,
			@Param("startnum") int startnum, @Param("endnum") int endnum);

	// detail_category=> null값이 아니면
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board where board_category = :board_category and "
			+ "(detail_category = :detail_category or :detail_category is null) " + "order by board_num desc ) tt) "
			+ "where rn >= :startnum and rn <= :endnum ", nativeQuery = true)
	List<Board> findByBoard_categoryAndDetail_cateogoryAndStartnumAndEndnum(
			@Param("board_category") String board_category, @Param("detail_category") String detail_category,
			@Param("startnum") int startnum, @Param("endnum") int endnum);

/////////////////////////////////////////////////////////////////////////////
	// 검색기능 :Paging 처리
	// 검색기능: 글제목
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board WHERE board_title like :keyword " + "order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByBoard_titleAndStartnumAndEndnum(@Param("keyword") String keyword,
			@Param("startnum") int startnum, @Param("endnum") int endnum);

	// 검색기능: 글내용
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board WHERE board_content like :keyword " + "order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByBoard_contentAndStartnumAndEndnum(@Param("keyword") String keyword,
			@Param("startnum") int startnum, @Param("endnum") int endnum);

	// 검색기능: 닉네임
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board WHERE nick_name like :keyword " + "order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByNick_nameAndStartnumAndEndnum(@Param("keyword") String keyword, @Param("startnum") int startnum,
			@Param("endnum") int endnum);

	// 검색기능: 글작성자(회원Id)
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ "(select * from board WHERE member_id like :keyword " + "order by board_num desc) tt) "
			+ "where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByMember_idAndStartnumAndEndnum(@Param("keyword") String keyword, @Param("startnum") int startnum,
			@Param("endnum") int endnum);

	// 검색기능: 글제목 + 글내용
	@Query(value = "select * from " + "(select rownum rn, tt.* from "
			+ " (select * from board WHERE board_title like :keyword " + " OR board_content like :keyword "
			+ " order by board_num desc) tt) " + " where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByKeywordAndStartnumAndEndnum(@Param("keyword") String keyword, @Param("startnum") int startnum,
			@Param("endnum") int endnum);

	////////////////////////////////////////////////////

	// board_title갯수만 검색
	@Query(value = "select count(*) from board " + "where board_title like :keyword", nativeQuery = true)
	int searchByBoard_title(@Param("keyword") String keyword);

	// board_content갯수만 검색
	@Query(value = "select count(*) from board " + "where board_content like :keyword", nativeQuery = true)
	int searchByBoard_content(@Param("keyword") String keyword);

	// nick_name갯수만 검색
	@Query(value = "select count(*) from board " + "where nick_name like :keyword", nativeQuery = true)
	int searchByNick_name(@Param("keyword") String keyword);

	// member_id갯수만 검색
	@Query(value = "select count(*) from board " + "where member_id like :keyword", nativeQuery = true)
	int searchByMember_id(@Param("keyword") String keyword);

	// keyword갯수만 검색
	@Query(value = "select count(*) from board " + "where board_title like :keyword "
			+ "or board_content like :keyword ", nativeQuery = true)
	int searchByKeyword(@Param("keyword") String keyword);
}
