package com.example.outven.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Member;
import com.example.outven.service.AdminService;
import com.example.outven.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	BoardService boardService;
	
	// 관리자용 메인 홈페이지로 이동
	@GetMapping("/admin/index")
	public String index(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		List<Board> recommend_board = boardService.recommendBoardCategoryList(1, 8);	// 추천게시판
		List<Board> art_board = boardService.boardCategoryList("이미지게시판", 1, 4);
		List<Board> patch_board = boardService.boardCategoryList("패치 & 정보 게시판", 1, 10);
		List<Board> tip_board = boardService.boardCategoryList("팁 & 공략 게시판", 1, 10);
		List<Board> position_board = boardService.boardCategoryList("포지션 게시판", 1, 10);
		List<Board> acc_board = boardService.boardCategoryList("사건 & 사고 게시판", 1, 10);
		
		model.addAttribute("recommend_board", recommend_board);
		model.addAttribute("art_board", art_board);
		model.addAttribute("patch_board", patch_board);
		model.addAttribute("tip_board", tip_board);
		model.addAttribute("position_board", position_board);
		model.addAttribute("acc_board", acc_board);
		
		return "/admin/main/adminindex";
	}
	
	// 관리자 로그인 창 이동
	@GetMapping("/adminLoginForm")
	public String adminForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);

		return "admin/member/adminLoginForm";
	}
	
	// 관리자 로그인
	@PostMapping("/admin/login")
	public String admin(HttpServletRequest request, Model model) {
		String id = request.getParameter("username");
		String pw = request.getParameter("password");
		boolean result = false;
		Member member = adminService.Adminlogin(id, pw); 
		// 관리자 로그인 성공
		if (member != null) {
			result = true;
			HttpSession session = request.getSession();
			session.setAttribute("member", member);
		}
		model.addAttribute("result", result);
		
		return "admin/member/adminLogin";
	}
	
	// 관리자 회원 관리 이동
	@GetMapping("/admin/memberCheck")
	public String adminMemberCheck(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		String role = request.getParameter("role");

		int pg = 1;
		if(request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		// 목록 : 1페이지당 30개씩
		int endnum = pg * 30;
		int startnum = endnum - 29;
		List<Member> list = null;
		int totalA = 0;
		if (role.equals("blacklist")) {
			list = adminService.adminBlacklistMemberCheck(startnum, endnum);
			totalA = adminService.BlacklistMemberCount();
		} else {
			list = adminService.adminMemberCheck(startnum, endnum, role);
			totalA = adminService.memberCount(role);
		}
						
		// 전체목록 페이징 처리
		int totalP = (totalA + 29) / 30;	// 총 페이지수
		int startPage = (pg-1) / 5*5+1;
		int endPage = startPage + 4;
		if(endPage > totalP) endPage = totalP;
		
		// 페이징 번화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for(int i=startPage; i<=endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if(pg==i) pageDTO.setCurrent(true);
			
			pageList.add(pageDTO);
		}
		
		// 2. 데이터 공유		
		model.addAttribute("pageList", pageList);
		if(startPage > 10) model.addAttribute("previousPage", startPage-1);
		if(endPage < totalP) model.addAttribute("nextPage", endPage+1);
		model.addAttribute("pg", pg);
		model.addAttribute("list", list);
		model.addAttribute("role", role);
		model.addAttribute("totalA", totalA);
		model.addAttribute("member", session.getAttribute("member"));
	
		
		return "/admin/member/adminMemberCheck";
	}
		
	// [관리자 회원등급 수정, 경험치 수정, 관리자 임명]
	// 관리자 임명
	@PostMapping(value = "/admin/appointAdmin", consumes = "application/json")
	@ResponseBody
	public boolean appointAdmin(@RequestBody List<Map<String, String>> selectedMembers) {
	    for (Map<String, String> member : selectedMembers) {
	        String memberId = member.get("member_id");
	        boolean result = adminService.appointAdmin(memberId);
	        if (!result) {
	            return false;
	        }
	    }
	    return true;
	}

	// 관리자 해임
	@PostMapping(value = "/admin/dismissAdmin", consumes = "application/json")
	@ResponseBody
	public boolean dismissAdmin(@RequestBody List<Map<String, String>> selectedMembers) {
	    for (Map<String, String> member : selectedMembers) {
	        String memberId = member.get("member_id");
	        boolean result = adminService.dismissAdmin(memberId);
	        if (!result) {
	            return false;
	        }
	    }
	    return true;
	}
	
	// 회원 수정
	@PostMapping(value = "/admin/modifyMember", consumes = "application/json")
	@ResponseBody
	public boolean modifyMember(@RequestBody List<Map<String, String>> selectedMembers) {
	    for (Map<String, String> member : selectedMembers) {
	        String memberId = member.get("member_id");
	        String memberLevel = member.get("member_level");
	        String memberExp = member.get("member_exp");
	        
	        boolean result = adminService.modifyMember(memberId, memberLevel, memberExp);
	        if (!result) {
	            return false;
	        }
	    }
	    return true;
	}
	
	// 회원 추방 & 블랙리스트 등록
	@PostMapping(value = "/admin/exileMember", consumes = "application/json")
	@ResponseBody
	public boolean exileMember(@RequestBody List<Map<String, String>> selectedMembers) {
		boolean result = false;
		for (Map<String, String> member : selectedMembers) {
	        String memberId = member.get("member_id");
	        result = adminService.exileMember(memberId);
	    }
	    return result;
	}
	
	// 블랙리스트 해제
	@PostMapping(value = "/admin/exileMemberCancel", consumes = "application/json")
	@ResponseBody
	public boolean exileMemberCancel(@RequestBody List<Map<String, String>> selectedMembers) {
		boolean result = false;
		for (Map<String, String> member : selectedMembers) {
	        String memberId = member.get("member_id");
	        result = adminService.exileMemberCancel(memberId);
	    }
	    return result;
	}
	
	// 관리자 게시물 이동
	
	
	// 관리자 게시물 삭제
	
}
