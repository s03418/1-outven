package com.example.outven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.outven.entity.Board;

//JpaRepository<관리대상 Entity, 대표값 자료형>
//JpaRepository를 상속받으면 자동적으로 bean 객체로 동작된다.
public interface BoardRepository extends JpaRepository<Board, Integer> {

	// paging 처리 query
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByStartnumAndEndnum(@Param("startnum") int startnum, @Param("endnum") int endnum);

	// board_category 페이징 검색
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board where board_category=:board_category order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByStartnumAndEndnum2(@Param("board_category") String board_category,
			@Param("startnum") int startnum, @Param("endnum") int endnum);

	// board_category갯수만 검색
	@Query(value = "select count(*) from (select * from board where board_category=:board_category order by board_num desc)", nativeQuery = true)
	int findByBoard_category(@Param("board_category") String board_category);

	// detail_category 페이징처리 검색
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board where board_category=:board_category and detail_category=:detail_category order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByStartnumAndEndnum3(@Param("board_category") String board_category,
			@Param("detail_category") String detail_category, @Param("startnum") int startnum,
			@Param("endnum") int endnum);
	
	// 추천게시판 페이징처리 (추천수 5이상만, 최신순)
	@Query(value = "select * from (select rownum rn, tt.* from "
			+ "(select * from board where board_recommend>=5 order by board_logtime desc) tt) "
			+ "where rn>= :startnum and rn<= :endnum", nativeQuery = true)
	List<Board> findByStartnumAndEndnumR(@Param("startnum") int startnum, @Param("endnum") int endnum);
	
	// 추천게시판 갯수 검색
	@Query(value = "select count(*) from board where board_recommend >= 5", nativeQuery = true)
	int findByRecommendCount();

	// detail_category갯수만 검색
	@Query(value = "select count(*) from (select * from board where board_category=:board_category and detail_category=:detail_category order by board_num desc)", nativeQuery = true)
	int findByDetail_category(@Param("board_category") String board_category,
			@Param("detail_category") String detail_category);

	// 추천 게시판 : 추천수가 일정이상 인 게시글만 리스트로 뽑아옴 board_recommend>=:5;
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board where board_recommend>=:board_recommend order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByboard_recommend(@Param("board_recommend") int board_recommend, @Param("startnum") int startnum,
			@Param("endnum") int endnum);

	// 신고 게시판 : 신고 횟수가 일정횟수 이상인 게시글만 리스트로 뽑아옴
	// board_report_count>=:board_report_count
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board where board_report_count>=:board_report_count order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> findByboard_report_count(@Param("board_report_count") int board_report_count, @Param("startnum") int startnum, @Param("endnum") int endnum);
	
	
	/////////////////////////////////////////////////////////////////////////////
	// 검색기능 :Paging 처리
	// 메인 검색 [제목]
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_title like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByBoard_titleAndStartAndEndnum(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum);
	
	
	// 게시판 내 검색기능: 글제목
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_category = :board_category and board_title like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum, @Param("board_category") String board_category);
	
	// 게시판 내 검색기능: 글내용
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_category = :board_category and board_content like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum, @Param("board_category") String board_category);
	
	// 게시판 내 검색기능: 닉네임
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_category = :board_category and nick_name like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByNick_nameAndStartnumAndEndnumAndBoardCategory(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum, @Param("board_category") String board_category);
	
	// 게시판 내 검색기능: 글작성자(회원Id)
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_category = :board_category and member_id like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByMember_idAndStartnumAndEndnumAndBoardCategory(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum, @Param("board_category") String board_category);
	
	// 게시판 내 검색기능: 글제목 + 글내용
	@Query(value = "select * from (select rownum rn, tt.* from (select * from board WHERE board_category = :board_category and board_title like :keyword OR board_category = :board_category and board_content like :keyword order by board_num desc) tt) where rn>=:startnum and rn<=:endnum", nativeQuery = true)
	List<Board> searchByKeywordAndStartnumAndEndnumAndBoardCategory(@Param("keyword") String keyword, @Param("startnum") int startnum, @Param("endnum") int endnum, @Param("board_category") String board_category);
	
	// keyword갯수만 검색
	@Query(value = "select count(*) from board " + "where board_title like :keyword "
			+ "or board_content like :keyword ", nativeQuery = true)
	int searchByKeyword(@Param("keyword") String keyword);
}
