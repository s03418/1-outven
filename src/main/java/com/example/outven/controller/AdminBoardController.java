package com.example.outven.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.outven.dto.BoardDTO;
import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Boardreport;
import com.example.outven.entity.Member;
import com.example.outven.entity.Recommend;
import com.example.outven.service.AdminService;
import com.example.outven.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
public class AdminBoardController {

	@Autowired
	BoardService boardService;
	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	AdminService adminService;

	@GetMapping("/board/admin_reporter_news_BoardList")
	public String reporter_news_BoardList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		String board_category = "리포터 뉴스 게시판";
		String detail_category = request.getParameter("detail_category");
		// 전체목록보기 : 20개씩
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;
		List<Board> list = null;
		int totalA = 0;
		if (detail_category != null) {
			list = boardService.boardDetailList(board_category, detail_category, startnum, endnum);
			totalA = boardService.getDetailCount(board_category, detail_category);
			model.addAttribute("list", list);
			model.addAttribute("detail_category", detail_category);
			model.addAttribute("totalA", totalA);
		} else {
			list = boardService.boardCategoryList(board_category, startnum, endnum);
			totalA = boardService.getBoardCount(board_category);
			model.addAttribute("list", list);
			model.addAttribute("totalA", totalA);
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

		// 2. 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);

		boolean is_login = false; // 로그인 상태 저장
		if (session.getAttribute("memId") != null)
			is_login = true;

		model.addAttribute("is_login", is_login);
		return "/admin/board/admin_reporter_news_BoardList";
	}

	@GetMapping("/board/admin_reporter_news_BoardView")
	public String reporter_news_BoardView(Model model, HttpServletRequest request) {
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
		return "/admin/board/admin_reporter_news_BoardView";
	}
	
	
	@GetMapping("/board/admin_tipBoardList")
	public String admin_tipBoardList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		String board_category = "팁 & 공략 게시판";
		String detail_category = request.getParameter("detail_category");
		// 전체목록보기 : 20개씩
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;
		List<Board> list = null;
		int totalA = 0;
		if (detail_category != null) {
			list = boardService.boardDetailList(board_category, detail_category, startnum, endnum);
			totalA = boardService.getDetailCount(board_category, detail_category);
			model.addAttribute("list", list);
			model.addAttribute("detail_category", detail_category);
			model.addAttribute("totalA", totalA);
		} else {
			list = boardService.boardCategoryList(board_category, startnum, endnum);
			totalA = boardService.getBoardCount(board_category);
			model.addAttribute("list", list);
			model.addAttribute("totalA", totalA);
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

		// 2. 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);

		boolean is_login = false; // 로그인 상태 저장
		if (session.getAttribute("memId") != null)
			is_login = true;

		model.addAttribute("is_login", is_login);
		return "/admin/board/admin_tipBoardoardList";
	}

	@GetMapping("/board/admin_tipBoardoardView")
	public String admin_tipBoardView(Model model, HttpServletRequest request) {
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
		return "/admin/board/admin_tipBoardoardView";
	}
	
	
	
	
	
	// [관리자] 글이동
	@PostMapping(value = "/admin/boardMove", consumes = "application/json")
	@ResponseBody
	public boolean adminBoardMove(@RequestBody List<Map<String, String>> selectedBoards) {
		boolean result = false;
		for (Map<String, String> board : selectedBoards) {
	        int boardnum = Integer.parseInt(board.get("board_num"));
			String board_category = board.get("board_category");
			String detail_category = board.get("detail_category");
	        adminService.adminBoardMove(boardnum, board_category, detail_category);
	    }
	    return result;
	}
	
	// [관리자] 글삭제
	@PostMapping(value = "/admin/boardDelete", consumes = "application/json")
	@ResponseBody
	public boolean adminBoardDelete(@RequestBody List<Map<String, String>> selectedBoards) {
		boolean result = false;
		for (Map<String, String> board : selectedBoards) {
	        int boardnum = Integer.parseInt(board.get("board_num"));
	        boardService.boardDelete(boardnum);
	    }
	    return result;
	}
}
