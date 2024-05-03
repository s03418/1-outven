package com.example.outven.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.outven.dao.AdminDAO;
import com.example.outven.dao.EmailDAO;
import com.example.outven.dao.MemberDAO;
import com.example.outven.dto.memberDTO;
import com.example.outven.entity.Blacklist;
import com.example.outven.entity.Member;

@Service
public class AdminService {

	@Autowired
	AdminDAO dao;
	
	// 관리자 로그인
	public Member Adminlogin(String id, String pw) {
		return dao.Adminlogin(id, pw);
	}

	// 회원 조회 페이징처리
	public List<Member> adminMemberCheck(int startnum, int endnum, String role) {
		return dao.adminMemberCheck(startnum, endnum, role);
	}
	
	// 블랙리스트 페이징처리
	public List<Member> adminBlacklistMemberCheck(int startnum, int endnum) {
		return dao.adminBlacklistMemberCheck(startnum, endnum);
	}
	
	// 블랙리스트 회원 조회 데이터 갯수
	public int BlacklistMemberCount() {
		return dao.BlacklistMemberCount();
	}
	
	// 회원 조회
	public List<Member> adminMemberCheckTab(String role) {
		return dao.adminMemberCheckTab(role);
	}
	
	// 회원 조회 데이터 갯수
	public int memberCount(String role) {
		return dao.memberCount(role);
	}
	
	// 관리자 임명
	public boolean appointAdmin(String id) {
		return dao.appointAdmin(id);
	}
	
	// 관리자 해임
	public boolean dismissAdmin(String id) {
		return dao.dismissAdmin(id);
	}

	// [관리자]회원 정보수정
	public boolean modifyMember(String id, String member_level, String member_exp) {
		return dao.modifyMember(id, member_level, member_exp);
	}
	
	// [관리자]회원 추방
	public boolean exileMember(String id) {
		return dao.exileMember(id);
	}
	
	// [관리자]블랙리스트 해제
	public boolean exileMemberCancel(String id) {
		return dao.exileMemberCancel(id);
	}

	// [관리자] 게시글 이동
	public boolean adminBoardMove(int board_num, String board_category, String detail_category) {
		return dao.adminBoardMove(board_num, board_category, detail_category);
	}
}
