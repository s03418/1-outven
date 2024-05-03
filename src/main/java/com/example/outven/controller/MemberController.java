package com.example.outven.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.outven.dao.MemberDAO;
import com.example.outven.dto.memberDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Member;
import com.example.outven.repository.MemberRepository;
import com.example.outven.service.BoardService;
import com.example.outven.service.MemberService;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BoardService boardService;
		
	// 로그인
	@PostMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		String pw = request.getParameter("password");

		// 로그인 결과
		boolean result = false;
		
		// 출석 체크 유무(false면 exp+10증가)
		// 마지막 로그인 날짜와 오늘 날짜 비교(년,월,일)
		boolean attendance = true;
		Member member = new Member();
		
//		Object[] values = new Object[2];
//	    values[0] = member;
//	    values[1] = isSameDate;
		
		Object[] values = new Object[2];
		values = memberService.login(id, pw);
		
		// 로그인 실패 시
		if (values == null) {
			model.addAttribute("result", result);
			model.addAttribute("attendance", attendance);
			return "member/login";
		} else {	// 로그인 성공
			member = (Member) values[0];
			attendance = (boolean) values[1];
			result = true;
			HttpSession session = request.getSession();
			session.setAttribute("member", member);
			model.addAttribute("result", result);
			model.addAttribute("attendance", attendance);
		}

		return "member/login";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();

		return "member/logout";
	}
	
	// 로그인 창 이동
	@GetMapping("/loginForm")
	public String loginForm() {
		return "member/loginForm";
	}

	// 회원가입 창 이동
	@GetMapping("/joinForm")
	public String writeForm() {
		return "member/joinForm";
	}
	
	// ID/PW 선택창 이동
	@GetMapping("/membersearch")
	public String memberSearch() {
		return "member/memberSearch";
	}
	
	// ID 찾기창 이동
	@GetMapping("/idsearchForm")
	public String idsearchForm() {
		return "member/idsearchForm";
	}
	
	// PW 찾기창 이동
	@GetMapping("/pwsearchForm")
	public String pwsearchForm() {
		return "member/pwsearchForm";
	}
	
	// 회원정보수정창 이동
	@GetMapping("/modifyForm")
	public String modifyForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		model.addAttribute("member", member);
		
		return "member/modifyForm";
	}
	
	// 회원정보수정
	@PostMapping("/modify")
	public String modify(HttpServletRequest request, Model model) {
		
		// 기존DB에 저장되어있는 엔티티를 불러오기 위한 id 변수
		String id = request.getParameter("member_id");
		
		// 변경가능한 값들
		String pwd = request.getParameter("password1");
		String name = request.getParameter("membername");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		
		// 주소 변경 시
		if (address == null) {
			address = request.getParameter("address1") + " " +
					  request.getParameter("address2");
		}
		
		Member member = new Member();
		member.setUser_password(pwd);
		member.setEmail(email);
		member.setPhone(phone);
		member.setAddress(address);
		member.setMembername(name);

		boolean result = memberService.memberModify(id, member);
		// 정상적으로 수정 시, 로그 아웃 처리
		if(result) {
			HttpSession session = request.getSession();
			session.invalidate();
		}
		
		model.addAttribute("result", result);
		
		return "member/modify";
	}
	
	// 가입
	@PostMapping("/join")
	public String join(memberDTO member, HttpServletRequest request) {
		boolean result = memberService.joinMember(member);
		if (result) {
			return "member/joinOk";
		}
		return "member/joinFail";
	}
	
	// 아이디 찾기
	@PostMapping("/idsearch")
	public String idsearch(HttpServletRequest request, Model model) {
		String name = request.getParameter("membername");
		String email = request.getParameter("email");
		String id = memberService.idsearch(name, email);

		if (id != null) {
			model.addAttribute("id", id);
			return "member/idFind";
		}
		return "member/idNotFind";
	}
	
	// 비밀번호 찾기
	@PostMapping("/pwsearch")
	public String pwsearch(HttpServletRequest request, Model model) {
		String id = request.getParameter("member_id");
		String email = request.getParameter("email");
		
		boolean result = memberService.pwsearch(id, email);
		
		if (result) {
			model.addAttribute("id", id);
			return "member/pwResetForm";
		}
		return "member/pwResetFail";
	}
	
	// 비밀번호 재설정
	@PostMapping("/pwReset")
	public String pwreset(HttpServletRequest request, Model model) {
		String id = request.getParameter("member_id");
		String password = request.getParameter("password1");
		
		boolean result = memberService.pwReset(id, password);
		
		if (result) {
			return "member/pwResetOk";
		}
		return "member/pwResetNo";
	}	
	
	// 회원탈퇴창 이동
	@GetMapping("/memberLeaveForm")
	public String memberLeaveForm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		model.addAttribute("member", member);
		
		return "member/memberLeaveForm";
	}
	
	// 회원탈퇴
	@PostMapping("/memberLeave")
	public String memberLeave(HttpServletRequest request, Model model) {
		String id = request.getParameter("member_id");
		String password = request.getParameter("password1");
		
		boolean result = memberService.memberLeave(id, password);
		if (result) {
			// 회원삭제 성공 시, 세션 종료
			HttpSession session = request.getSession();
			session.invalidate();
		}
		
		model.addAttribute("result", result);
		
		return "member/memberLeave";
	}
}
