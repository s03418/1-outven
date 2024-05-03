package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.repository.imageBoardRepository;
import com.example.outven.repository.BoardRepository;
import com.example.outven.repository.MemberRepository;

import jakarta.transaction.Transactional;

import com.example.outven.dto.BoardDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Member;

@Repository
public class BoardDAO {

	@Autowired
	private imageBoardRepository imageRepository;
	@Autowired
	private BoardRepository boardRepository;
	
	// 추천게시판 목록 보기 (추천수 5회이상만)
	public List<Board> recommendBoardCategoryList(int startnum, int endnum) {
		return boardRepository.findByStartnumAndEndnumR(startnum, endnum);
	}
	
	//  보드카테고리 목록갯수
	public int getRecommendBoardCount() {
		return boardRepository.findByRecommendCount();
	}
	
	// 목록보기
	public List<Board> boardList(int startnum, int endnum) {
		return boardRepository.findByStartnumAndEndnum(startnum, endnum);
	}
	// 보드카테고리 목록보기
	public List<Board> boardCategoryList(String board_category, int startnum, int endnum) {
		return boardRepository.findByStartnumAndEndnum2(board_category, startnum, endnum);
	}
	// 디테일 목록보기
	public List<Board> boardDetailList(String board_category, String detail_category, int startnum, int endnum) {
		return boardRepository.findByStartnumAndEndnum3(board_category, detail_category, startnum, endnum);
	}
	
	// 총데이터 갯수
	public int getCount() {
		return (int)boardRepository.count();
	}
	//  보드카테고리 목록갯수
	public int getBoardCount(String board_category) {
		return boardRepository.findByBoard_category(board_category);
	}
	// 디테일 목록갯수
	public int getDetailCount(String board_category, String detail_category) {
		return boardRepository.findByDetail_category(board_category, detail_category);
	}
	// 글저장하기 : insert
	public boolean insert(BoardDTO dto) {
		// dto를 entity로 변경
		Board board = dto.toEntity();
		// db에 글 저장
		Board board_insert =  boardRepository.save(board);
		boolean result = false;
		if(board_insert != null) result = true;
		return result;
	}
	// 데이터 수정 : update
	public boolean boardModify(BoardDTO dto) {
		boolean result = false;
		// 1. 수정할 대상 가져오기
		Board board = boardRepository.findById(dto.getBoard_num()).orElse(null);
		// 수정할 대상이 존재하면
		if(board != null) {
			// 2. 대상 수정하기
			board.setDetail_category(dto.getDetail_category());
			board.setBoard_title(dto.getBoard_title());
			board.setBoard_content(dto.getBoard_content());
			board.setBoard_img(dto.getBoard_img());
			// 3. 저장
			Board board_update = boardRepository.save(board);
			if(board_update != null) {
				result = true;
			} else {				
				result = false;
			}
		}
		return result;
	}
	
	// 데이터 삭제 : delete
	public boolean boardDelete(int board_num) {
		// 1. board_num 조회하여 삭제할 데이터 값 가져오기
		Board board = boardRepository.findById(board_num).orElse(null);
		// 1. 멤버에있는 아이디와 게시판에 작성한 사람의 아이디가 같으면서 board 값이 있을때
		if(board != null) {
			// 2. 삭제 
			boardRepository.delete(board); // 데이터 삭제
		}
		// 데이터가 있는지 없는지 확인하는 함수 boolean 반환
		// true : 삭제 실패, false : 삭제 성공
		return boardRepository.existsById(board_num);
	}
	
	// 상세보기
	public Board boardView(int board_num) {
		Board board = boardRepository.findById(board_num).orElse(null);
		return boardRepository.findById(board_num).orElse(null);
	}
	// 조회수 증가
	public Board updateHit(int board_num) {
		// 1. seq로 현재 데이터 얻어오기
		Board board = boardRepository.findById(board_num).orElse(null);		
		if(board != null) {
			// 2. 얻어온 데이터 수정 : 조회수 증가
			board.setBoard_hit(board.getBoard_hit()+1);
			// 3. 수정된 데이터 저장 
			board = boardRepository.save(board);
		} 
		
		return board;
	}
	
//////////////////////////////////////////////////////////
	// 메인 검색 [제목]
	public List<Board> searchByBoard_titleAndStartAndEndnum(String keyword, int startnum, int endnum) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByBoard_titleAndStartAndEndnum(keyword, startnum, endnum);
	}
	// 게시판 내 검색기능: 글제목
	public List<Board> searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}
	// 게시판 내 검색기능: 글내용
	public List<Board> searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}
	// 게시판 내 검색기능: 닉네임
	public List<Board> searchByNick_nameAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByNick_nameAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}
	// 게시판 내 검색기능: 글작성자(회원Id)
	public List<Board> searchByMember_idAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByMember_idAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}
	// 게시판 내 검색기능: 글제목 + 글내용
	public List<Board> searchByKeywordAndStartnumAndEndnumAndBoardCategory(String keyword, int startnum, int endnum, String board_category) {
		keyword = "%" + keyword + "%";
		return boardRepository.searchByKeywordAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
	}

////////////////////////
//검색어: title
	public int getTitleCount(String keyword) {
		return imageRepository.searchByBoard_title(keyword);
	}

//검색어: content
	public int getContentCount(String keyword) {
		return imageRepository.searchByBoard_content(keyword);
	}

//검색어: nickname
	public int getNickNameCount(String keyword) {
		return imageRepository.searchByNick_name(keyword);
	}

//검색어: memberid
	public int getMemberIdCount(String keyword) {
		return imageRepository.searchByMember_id(keyword);
	}

//검색어: keyword
	public int getKeywordCount(String keyword) {
		return imageRepository.searchByKeyword(keyword);
	}

	// 24/02/23 추가
	// 추천 값 변경 함수
	public boolean updateRecommned(int board_num) {
		boolean result = false;
		// 게시글 번호 값을 조회
		Board board = boardRepository.findById(board_num).orElse(null);
		Board board_update = null;
		// null 값이 아닐때
		if (board != null) {
			board.setBoard_recommend(board.getBoard_recommend() + 1);
			board_update = boardRepository.save(board);

			// null이 아닐때
			if (board_update != null) {
				result = true;
			}
		}

		return result;
	}

	// 24/02/23 추가
	// 신고 값 변경 함수
	public boolean updateReport(int board_num) {
		boolean result = false;
		// 게시글 번호 값을 조회
		Board board = boardRepository.findById(board_num).orElse(null);
		Board board_update = null;
		// null 값이 아닐때
		if (board != null) {
			board.setBoard_report_count(board.getBoard_report_count() + 1);
			board_update = boardRepository.save(board);
		}
		// null이 아닐때
		if (board_update != null) {
			result = true;
		}

		return result;
	}

}
