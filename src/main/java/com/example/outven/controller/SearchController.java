package com.example.outven.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Member;
import com.example.outven.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
public class SearchController {
	
	@Autowired
	BoardService boardService;
	@Autowired
	ApplicationContext applicationContext;
	
	// 메인화면 검색
	@GetMapping("/mainSearch")
	public String BoardSearch(HttpServletRequest request, Model model) {
		String keyword = request.getParameter("keyword");

		// 전체목록보기 : 20개씩
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;
		int totalA = 0;

		List<Board> list = boardService.searchByBoard_titleAndStartAndEndnum(keyword, startnum, endnum);

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
		model.addAttribute("list", list);
		model.addAttribute("totalA", totalA);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);

		return "/board/boardMainSearchList";
	}

	@GetMapping("/board/boardMainSearchView")
	public String boardMainSearchView(Model model, HttpServletRequest request) {
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
		return "/board/boardMainSearchView";
	}
	

}
