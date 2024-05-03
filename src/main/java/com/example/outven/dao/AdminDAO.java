package com.example.outven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.entity.Blacklist;
import com.example.outven.entity.Board;
import com.example.outven.entity.Member;
import com.example.outven.repository.AdminRepository;
import com.example.outven.repository.BlacklistRepository;
import com.example.outven.repository.BoardRepository;
import com.example.outven.repository.MemberRepository;

@Repository
public class AdminDAO {

	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BlacklistRepository blacklistRepository;
	
	// 회원 조회 데이터 갯수
	public int memberCount(String role) {
		return adminRepository.findByRole1(role);
	}			
	
	// 회원 조회 페이징처리 포함
	public List<Member> adminMemberCheck(int startnum, int endnum, String role) {
		List<Member> member = 
				adminRepository.findByRoleAndStartnumAndEndnum(role, startnum, endnum);
		return member;
	}
	
	// 블랙리스트 조회 데이터 갯수
	public int BlacklistMemberCount() {
		return (int) blacklistRepository.count();
	}		
	
	// 블랙리스트 회원 조회 페이징처리 포함
	public List<Member> adminBlacklistMemberCheck(int startnum, int endnum) {
		List<Member> member = 
				adminRepository.findByStartnumAndEndnumBlacklist(startnum, endnum);
		return member;
	}
	
	// 회원관리 선택창 클릭시 db 조회
	public List<Member> adminMemberCheckTab(String role) {
		List<Member> member = 
				adminRepository.findByRole2(role);
		return member;
	}
	
	// 관리자 로그인
	public Member Adminlogin(String id, String pw) {
		Member member =  memberRepository.findByIdAndPassword(id, pw);
		if (member == null || !member.getUser_role().equals("admin")) {
			return null;
		}
		return member;
	}
	
	// 관리자 임명
	public boolean appointAdmin(String id) {
		Member member = memberRepository.findById(id).orElse(null);
		if (member != null) {
			member.setUser_role("admin");
		}
		return memberRepository.save(member) != null;
	}
	
	// 관리자 해임
	public boolean dismissAdmin(String id) {
		Member member = memberRepository.findById(id).orElse(null);
		if (member != null) {
			member.setUser_role("user");
		}
		return memberRepository.save(member) != null;
	}
	
	// [관리자]회원 정보수정
	public boolean modifyMember(String id, String member_level, String member_exp) {
		Member member = memberRepository.findById(id).orElse(null);
		if (member != null) {
			member.setMember_level(Integer.parseInt(member_level));
			member.setMember_exp(Integer.parseInt(member_exp));
		}
		return memberRepository.save(member) != null;
	}
	
	// [관리자]회원 추방
	public boolean exileMember(String id) {
		boolean result = false;
		Member member = memberRepository.findById(id).orElse(null);
		Blacklist blacklist = new Blacklist();
		
		// 블랙리스트로 등록
		if (member != null) {
			blacklist.setMember_id(member.getMember_id());
			blacklist.setMembername(member.getMembername());
			blacklist.setNick_name(member.getNick_name());
			blacklist.setUser_password(member.getUser_password());
			blacklist.setEmail(member.getEmail());
			blacklist.setPhone(member.getPhone());
			blacklist.setBirth(member.getBirth());
			blacklist.setAddress(member.getAddress());
			blacklist.setGender(member.getGender());
			blacklist.setUser_role(member.getUser_role());
			blacklist.setLogtime(member.getLogtime());
			blacklist.setJointime(member.getJointime());
			blacklist.setMember_level(member.getMember_level());
			blacklist.setMember_exp(member.getMember_exp());
		}
		blacklistRepository.save(blacklist);
		memberRepository.delete(member);
		
		if (memberRepository.findById(id) == null 
				&& blacklistRepository.findById(id) != null) {
			result = true;
		}
		
		return result;
	}
	
	// [관리자]블랙리스트 해제
	public boolean exileMemberCancel(String id) {
		boolean result = false;
		Blacklist blacklist = blacklistRepository.findById(id).orElse(null);
		Member member  = new Member();
		
		// 블랙리스트 해제
		if (blacklist != null) {
			member.setMember_id(blacklist.getMember_id());
			member.setMembername(blacklist.getMembername());
			member.setNick_name(blacklist.getNick_name());
			member.setUser_password(blacklist.getUser_password());
			member.setEmail(blacklist.getEmail());
			member.setPhone(blacklist.getPhone());
			member.setBirth(blacklist.getBirth());
			member.setAddress(blacklist.getAddress());
			member.setGender(blacklist.getGender());
			member.setUser_role(blacklist.getUser_role());
			member.setLogtime(blacklist.getLogtime());
			member.setJointime(blacklist.getJointime());
			member.setMember_level(blacklist.getMember_level());
			member.setMember_exp(blacklist.getMember_exp());
		}
		memberRepository.save(member);
		blacklistRepository.delete(blacklist);
		
		if (memberRepository.findById(id) != null 
				&& blacklistRepository.findById(id) == null) {
			result = true;
		}
		
		return result;
	}
	
	//[관리자] 게시물 이동
	public boolean adminBoardMove(int board_num, String board_category, String detail_category) {
		Board board = boardRepository.findById(board_num).orElse(null);
		board.setBoard_category(board_category);
		board.setDetail_category(detail_category);
		return boardRepository.save(board) != null;
	}
}
