package com.example.outven.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.outven.dao.EmailDAO;
import com.example.outven.dao.MemberDAO;
import com.example.outven.service.MemberService;

@Controller
// 회원가입 창 컨트롤러
public class EmailController {

	@Autowired
	MemberService memberService;

	// [회원가입]코드 이메일 전송
	@PostMapping("/mailCode")
	@ResponseBody
	public String mailCode(@RequestParam("email") String email) {
		String code = memberService.mailCode(email);
		return code;
	}
	
	// [아이디]코드 이메일 전송
	@PostMapping("/IDsearchMail")
	@ResponseBody
	public String IDsearchMail(
					@RequestParam("name") String name,
					@RequestParam("email") String email) {
		String code = memberService.idsearchMail(name, email);
		// System.out.println("아이디 찾기 인증코드 : " + code);
		return code;
	}
	
	// [비밀번호 찾기]코드 이메일 전송
	@PostMapping("/PWsearchMail")
	@ResponseBody
	public String PWsearchMail(
					 @RequestParam("id") String id,
			  		 @RequestParam("email") String email) {
		String code = memberService.pwsearchMail(id, email);
		// System.out.println("비밀번호 찾기 인증코드 : " + code);
		return code;
	}
	
	// [회원정보수정]이메일 변경시 코드 전송
	@PostMapping("/ModifyMail")
	@ResponseBody
	public String ModifyMail(
					 @RequestParam("id") String id,
			  		 @RequestParam("email") String email) {
		String code = memberService.ModifyMail(id, email);
		// System.out.println("회원정보 이메일변경 인증코드 : " + code);
		return code;
	}
	
	// 이메일 중복 판별
	@PostMapping("/checkEmail")
	@ResponseBody
	public boolean checkEmail(@RequestParam("email") String email) throws Exception {
		return memberService.checkEmail(email);
	}
	
	// 중복된 아이디 판별
	@PostMapping("/checkId")
	@ResponseBody
	public boolean checkId(@RequestParam("id") String id) throws Exception {
		// 아이디중복 & 블랙리스트 검색
		boolean checkId = memberService.checkId(id);
		boolean blacklist = memberService.blacklistSearch(id);
		if (blacklist == false && checkId == false) {
			return false;
		} else {
			return true;
		}
	}
	
	// 중복된 닉네임 판별
	@PostMapping("/checkNick")
	@ResponseBody
	public boolean checkNick(@RequestParam("nick") String nick) throws Exception {
		return memberService.checkNick(nick);
	}
}
