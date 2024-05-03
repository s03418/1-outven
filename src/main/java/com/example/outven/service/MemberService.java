package com.example.outven.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.outven.dao.EmailDAO;
import com.example.outven.dao.MemberDAO;
import com.example.outven.dto.memberDTO;
import com.example.outven.entity.Member;

@Service
public class MemberService {

	@Autowired
	private MemberDAO mdao;

	@Autowired
	private EmailDAO edao;

	// 회원가입 코드 이메일 전송
	public String mailCode(String email) {
		return edao.mailCode(email);
	}

	// 아이디 찾기 코드 이메일 전송
	public String idsearchMail(String name, String email) {
		return edao.idsearchMail(name, email);
	}

	// 비밀번호 찾기 코드 이메일 전송
	public String pwsearchMail(String id, String email) {
		return edao.pwsearchMail(id, email);
	}

	// 회원정보 이메일 변경시 이메일 전송
	public String ModifyMail(String id, String email) {
		return edao.ModifyMail(id, email);
	}

	// 아이디 찾기 인증절차
	public String idsearch(String name, String email) {
		return mdao.idsearch(name, email);
	}

	// 비밀번호 재설정 하기 전 인증절차
	public boolean pwsearch(String id, String email) {
		return mdao.pwsearch(id, email);
	}

	// 비밀번호 재설정
	public boolean pwReset(String id, String password) {
		return mdao.pwReset(id, password);
	}

	// 회원정보 수정
	public boolean memberModify(String id, Member member) {
		return mdao.memberModify(id, member);
	}

	// 회원가입
	public boolean joinMember(memberDTO member) {
		return mdao.joinMember(member);
	}

	// 로그인
	public Object[] login(String id, String pw) {
		return mdao.login(id, pw);
	}
	
	// 이메일 중복 판별
	public boolean checkEmail(String email) {
		return mdao.checkEmail(email);
	}

	// 중복된 아이디 판별
	public boolean checkId(String id) {
		return mdao.checkId(id);
	}

	// 중복된 닉네임 판별
	public boolean checkNick(@RequestParam("nick") String nick) throws Exception {
		return mdao.checkNick(nick);
	}
	
	// 회원 탈퇴
	public boolean memberLeave(String id, String password) {
		return mdao.memberLeave(id, password);
	}
	
	// 블랙리스트 조회
	public boolean blacklistSearch(String id) {
		// true면 blacklist
		return mdao.blacklistSearch(id);
	}
}
