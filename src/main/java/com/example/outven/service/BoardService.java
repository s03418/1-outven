package com.example.outven.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.outven.dao.BoardCommentDAO;
import com.example.outven.dao.BoardDAO;
import com.example.outven.dao.BoardreportDAO;
import com.example.outven.dao.RecommendDAO;
import com.example.outven.dto.BoardDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Board_comment;
import com.example.outven.entity.Boardreport;
import com.example.outven.entity.Recommend;

@Service
public class BoardService {

	@Autowired
	private BoardDAO dao;

	// 24/02/23 추가
	@Autowired
	private RecommendDAO recom_dao;

	// 24/02/23 추가
	@Autowired
	private BoardreportDAO report_dao;

	// 24/02/27 추가
	@Autowired
	private BoardCommentDAO comment_dao;

	// 목록보기
	public List<Board> boardList(int startnum, int endnum) {
		return dao.boardList(startnum, endnum);
	}

	// 카테고리 목록보기
	public List<Board> boardCategoryList(String board_category, int startnum, int endnum) {
		return dao.boardCategoryList(board_category, startnum, endnum);
	}
	
	// 추천게시판 목록보기
	public List<Board> recommendBoardCategoryList(int startnum, int endnum) {
		return dao.recommendBoardCategoryList(startnum, endnum);
	}
	
	// 추천게시판 갯수
	public int getRecommendBoardCount() {
		return (int) dao.getRecommendBoardCount();
	}
	
	// 디테일 목록보기
	public List<Board> boardDetailList(String board_category, String detail_category, int startnum, int endnum) {
		return dao.boardDetailList(board_category, detail_category, startnum, endnum);
	}

	// 총데이터 갯수
	public int getCount() {
		return (int) dao.getCount();
	}

	// 보드카테고리 목록갯수
	public int getBoardCount(String board_category) {
		return dao.getBoardCount(board_category);
	}

	// 디테일 목록갯수
	public int getDetailCount(String board_category, String detail_category) {
		return dao.getDetailCount(board_category, detail_category);
	}

	// 글저장하기 : insert
	public boolean insert(BoardDTO dto) {
		return dao.insert(dto);
	}

	// 상세보기
	public Board boardView(int board_num) {
		return dao.boardView(board_num);
	}

	// 조회수 증가
	public Board updateHit(int board_num) {
		return dao.updateHit(board_num);
	}

	// 데이터 삭제
	public boolean boardDelete(int board_num) {
		return dao.boardDelete(board_num);
	}

	// 데이터 수정
	public boolean boardModify(BoardDTO dto) {
		return dao.boardModify(dto);
	}

//////////////////////////////////////////////////////
	// 메인 검색 [제목]
	public List<Board> searchByBoard_titleAndStartAndEndnum(String keyword, int startnum, int endnum) {
		keyword = "%" + keyword + "%";
		return dao.searchByBoard_titleAndStartAndEndnum(keyword, startnum, endnum);
	}

	// 게시판 내 검색기능: 글제목
	public List<Board> searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum,
			String board_category) {
		keyword = "%" + keyword + "%";
		return dao.searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

	// 게시판 내 검색기능: 글내용
	public List<Board> searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum,
			int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return dao.searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

	// 게시판 내 검색기능: 닉네임
	public List<Board> searchByNick_nameAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum,
			String board_category) {
		keyword = "%" + keyword + "%";
		return dao.searchByNick_nameAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

	// 게시판 내 검색기능: 글작성자(회원Id)
	public List<Board> searchByMember_idAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum,
			String board_category) {
		keyword = "%" + keyword + "%";
		return dao.searchByMember_idAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

	// 게시판 내 검색기능: 글제목 + 글내용
	public List<Board> searchByKeywordAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum,
			String board_category) {
		return dao.searchByKeywordAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

///////////////////////////////////////////////////
//검색어: 제목수
	public int getTitleCount(String keyword) {
		return dao.getTitleCount(keyword);
	}

//검색어: 내용수
	public int getContentCount(String keyword) {
		return dao.getContentCount(keyword);
	}

//검색어: 닉네임 수
	public int getNickNameCount(String keyword) {
		return dao.getNickNameCount(keyword);
	}

//검색어: 멤버수
	public int getMemberIdCount(String keyword) {
		return dao.getMemberIdCount(keyword);
	}

	// 검색어: 제목 + 내용(키워드수)
	public int getKeywordCount(String keyword) {
		return dao.getKeywordCount(keyword);
	}

	// 24/02/23 추가
	// 추천 테이블 삽입
	public boolean insertRecomment(Recommend recommend) {
		return recom_dao.insertRecomment(recommend);
	}

	// 24/02/23 추가
	// 추천 값 변경 함수
	public boolean updateRecommned(int board_num) {
		return dao.updateRecommned(board_num);
	}

	// 24/02/23 추가
	// 추천 주었는지 확인
	public boolean recomCheck(int board_num, String member_id) {
		return recom_dao.recomCheck(board_num, member_id);
	}

	// 24/02/23 추가
	// 신고 입력하기
	public boolean reportWrite(Boardreport boardreport) {
		return report_dao.reportWrite(boardreport);
	}

	// 24/02/23 추가
	// 신고 값 변경 함수
	public boolean updateReport(int board_num) {
		return dao.updateReport(board_num);
	}

	// 24/02/23 추가
	// 중복값 있는지 검사
	public boolean reportCheck(int board_num, String member_id) {
		return report_dao.reportCheck(board_num, member_id);
	}

	// 24/02/27 추가
	// 댓글 리스트 : 게시판 번호와 일치한 댓글 10개 조회
	public List<Board_comment> boardCommentList(int startnum, int endnum, int board_num) {
		return comment_dao.boardCommentList(startnum, endnum, board_num);
	}

	// 24/02/27 추가
	// 게시판 번호와 일치한 데이터 수를 구함
	public int getCount(int board_num) {
		return comment_dao.getCount(board_num);
	}

	// 24/02/27 추가
	// 댓글 입력
	public boolean commentWrite(Board_comment comment) {
		return comment_dao.commentWrite(comment);
	}

	// 24/02/27 추가
	// 답글
	public boolean recomment(Board_comment comment) {
		return comment_dao.recomment(comment);
	}

	// 24/02/27 추가
	// 댓글 삭제 처리
	public boolean commentDelete(int comment_num) {
		return comment_dao.commentDelete(comment_num);
	}

}
