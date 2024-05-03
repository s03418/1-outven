package com.example.outven.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.outven.dto.BoardDTO;
import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Board_comment;
import com.example.outven.entity.Boardreport;
import com.example.outven.entity.Member;
import com.example.outven.entity.Recommend;
import com.example.outven.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class RecomBoardController {
	
	@Autowired
	BoardService boardService;
	@Autowired
	ApplicationContext applicationContext;

	@GetMapping("/board/recommend_BoardList")
	public String recommend_BoardList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 전체목록보기 : 20개씩
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;
		List<Board> list = boardService.recommendBoardCategoryList(startnum, endnum);
		int totalA = boardService.getRecommendBoardCount();
		
		// 전체목록 페이징 처리
		int totalP = (totalA + 19) / 20; // 총 페이지수
		int startPage = (pg - 1) / 10 * 10 + 1;
		int endPage = startPage + 9;
		if (endPage > totalP)
			endPage = totalP;

		// 페이징 번화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for (int i = startPage; i <= endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if (pg == i)
				pageDTO.setCurrent(true);

			pageList.add(pageDTO);
		}

		// 2. 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);
		model.addAttribute("list", list);

		boolean is_login = false; // 로그인 상태 저장
		if (session.getAttribute("memId") != null)
			is_login = true;

		model.addAttribute("is_login", is_login);
		return "/board/recommend_BoardList";
	}

	@GetMapping("/board/recommend_BoardView")
	public String recommend_BoardView(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		// db
		boardService.updateHit(board_num); // 조회수 증가
		Board board = boardService.boardView(board_num); // 상세보기 데이터

		// 로그인한 사람과 글쓴사람이 같은지 검사
		boolean isMemId = false;

		if (session.getAttribute("member") != null) {
			Member member = (Member) session.getAttribute("member");
			if (board.getMember_id().equals(member.getMember_id())) {
				isMemId = true;
			}
		}
		// 2. 데이터 공유
		model.addAttribute("board", board);
		model.addAttribute("pg", pg);
		model.addAttribute("board_num", board_num);
		model.addAttribute("isMemId", isMemId);
		// view처리
		
		// 24/02/27 댓글 처리 추가
		boolean listExist = false;
		int comPg = 1;
		if (request.getParameter("comPg") != null) {
			comPg = Integer.parseInt(request.getParameter("comPg"));
		}
		// System.out.println(comPg);
		// System.out.println(board_num);
		// 1-1) 목록보기 : 10개씩
		int endnum = comPg * 10;
		int startnum = endnum - 9;
		// db
		// champ_code 글이 있는지 확인 0개면 없음
		int listCount = boardService.getCount(board_num);
		if (listCount > 0) {
			List<Board_comment> boardCom = boardService.boardCommentList(startnum, endnum, board_num);
			// System.out.println("boardCom : " + boardCom.get(0));
			// 답글 구별용
			for (int i = 0; i < boardCom.size(); i++) {
				if(boardCom.get(i).getCom_re_lev() >0) {
					boardCom.get(i).setCom_judge(true);
				}else {
					boardCom.get(i).setCom_judge(false);
				}
			}
			// 삭제 버튼 구별용
			for  (int i = 0; i < boardCom.size(); i++) {
				if (session.getAttribute("member") != null) {
					Member member = (Member) session.getAttribute("member");
//							System.out.println("현재 접속 id : " + member.getMember_id());
//							System.out.println("게시글 작성자의 id : " + boardCom.get(i).getMember_id() );
					if(boardCom.get(i).getMember_id().equals(member.getMember_id())) {
						boardCom.get(i).setMatch_id(true);
					}else {
						boardCom.get(i).setMatch_id(false);
					}
				}
			}
			
			// System.out.println(boardCom.get(0));
			// 1-2) 페이징 처리
			int totalA = boardService.getCount(board_num); // 총 데이터 개수
			int totalP = (totalA + 9) / 10; // 총 페이지수

			int startPage = (comPg - 1) / 5 * 5 + 1;
			int endPage = startPage + 4;
			if (endPage > totalP) endPage = totalP;

			// 페이징 변화와 현재 페이지 정보를 리스트에 저장
			List<PageDTO> pageList = new ArrayList<>();
			for (int i = startPage; i <= endPage; i++) {
					PageDTO pageDTO = new PageDTO();
					pageDTO.setPage(i);
				if (comPg == i)
					pageDTO.setCurrent(true);

					pageList.add(pageDTO);
				}
				listExist = true;
				// 챔피언 댓글
				model.addAttribute("boardCom", boardCom);
				model.addAttribute("pageList", pageList);

				if (startPage > 5)
					model.addAttribute("previousPage", startPage - 1);
				if (endPage < totalP)
					model.addAttribute("nextPage", endPage + 1);
					
		}
		// 2. 데이터 공유
		// 챔피언 정보

		// 댓글
		model.addAttribute("comPg", comPg);
		model.addAttribute("listExist", listExist);
		
		return "/board/recommend_BoardView";
	}

	// 추천게시판 검색
	@GetMapping("/recomSearch")
	public String recomSearch(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		String board_category = "추천 게시판";
		// 데이터 처리
		String name = request.getParameter("name"); // 검색어 분류
		String keyword = request.getParameter("keyword"); // 검색어

		// 페이지 처리
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;

		// 검색 결과 및 페이징 처리
		List<Board> list = null;
		int totalA = 0;

		// 검색 처리
		if (name == null || keyword == null) {
		// 적절한 에러 처리 또는 기본 동작 설정
			return "error";
		}

		// DB 조회
		if (board_category.equals("추천 게시판")) {
			if (name.equals("subject")) {
				list = boardService.searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getTitleCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("content")) {
				list = boardService.searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getContentCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("nicname")) {
				list = boardService.searchByNick_nameAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getNickNameCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("category")) {
				list = boardService.searchByMember_idAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getMemberIdCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("subjcont")) {
				list = boardService.searchByKeywordAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getKeywordCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else {
			// 유효하지 않은 검색어 분류일 경우 적절한 에러 처리
				list = boardService.boardCategoryList(board_category, startnum, endnum);
				totalA = boardService.getBoardCount(board_category);
			}
		} else {
			list = boardService.boardCategoryList(board_category, startnum, endnum);
			totalA = boardService.getBoardCount(board_category);
		}
		// 전체목록 페이징 처리
		int totalP = (totalA + 19) / 20; // 총 페이지수
		int startPage = (pg - 1) / 10 * 10 + 1;
		int endPage = startPage + 9;
		if (endPage > totalP)
			endPage = totalP;

		// 페이징 번화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for (int i = startPage; i <= endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if (pg == i)
				pageDTO.setCurrent(true);

			pageList.add(pageDTO);
		}

		// 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);
		model.addAttribute("list", list);

		// view 처리 파일 공유
		return "/board/recommend_BoardList";
	}


	

}
