package com.example.outven.dao;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.dto.memberDTO;
import com.example.outven.email.IdsearchMail;
import com.example.outven.email.PwsearchMail;
import com.example.outven.entity.Blacklist;
import com.example.outven.entity.Member;
import com.example.outven.repository.AdminRepository;
import com.example.outven.repository.BlacklistRepository;
import com.example.outven.repository.MemberRepository;

import jakarta.mail.MessagingException;

@Repository
public class MemberDAO {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	BlacklistRepository blacklistRepository;
	
	@Autowired
	IdsearchMail IdsearchMail;
	
	@Autowired
	PwsearchMail PwsearchMail;
	
	// 아이디 찾기 인증절차
	public String idsearch(String name, String email) {
		String result = null;
		Member member = memberRepository.findByNameEmail(name, email);
		
		// 아이디가 존재할 시
		if (member != null) {
			result = member.getMember_id();
		}
		return result;
	}
	
	// 비밀번호 재설정 하기 전 인증절차
	public boolean pwsearch(String id, String email) {
		boolean result = false;
		Member member = memberRepository.findByIdAndEmail(id, email);
		
		if (member != null) {
			result = true;
		}
		return result;
	}
	
	// 비밀번호 재설정
	public boolean pwReset(String id, String password) {
		Member member = memberRepository.findById(id).orElse(null);
		member.setUser_password(password);
				
		return memberRepository.save(member) != null;
	}
	
	// 회원정보 수정
	public boolean memberModify(String id, Member member) {
		Member ori_member = new Member(); // 기존 DB에 저장되어있는 멤버 정보
		// [바꿀 수 있는 항목]
		// 1. 비밀번호
		// 2. 이름
		// 3. 이메일
		// 4. 전화번호
		// 5. 주소

		ori_member = memberRepository.findById(id).orElse(null);
		ori_member.setUser_password(member.getUser_password());
		ori_member.setMembername(member.getMembername());
		ori_member.setEmail(member.getEmail());
		ori_member.setPhone(member.getPhone());
		ori_member.setAddress(member.getAddress());
		
		// 업데이트
		return memberRepository.save(ori_member) != null;
	}
	
	// 이메일 중복 판별
	public boolean checkEmail(String email) {
		boolean result = false;
		Member member = memberRepository.findByEmail(email);
		if (member != null) {
			result = true;
		}
		return result;
	}
	
	// 중복된 아이디 판별
	public boolean checkId(String id) {
		return memberRepository.existsById(id);
	}
	
	// 중복된 넥네임 판별
	public boolean checkNick(String nick) {
		boolean result = false;
		Member member = memberRepository.findByNick(nick);
		if (member != null) {
			result = true;
		}
		return result;
	}
	
	// 회원가입
	public boolean joinMember(memberDTO member) {
		Member m = new Member();
		m.setMember_id(member.getMember_id());
		m.setMembername(member.getMembername());
		m.setNick_name(member.getNick_name());
		m.setUser_password(member.getPassword1());
		m.setEmail(member.getEmail());
		m.setPhone(member.getPhone());
		
		// 2000-1-1 => 2000-01-01 형태
		m.setBirth(member.getYear() + "-" +
		String.format("%02d", Integer.parseInt(member.getMonth())) + "-" +
		String.format("%02d", Integer.parseInt(member.getDay())));
		
		m.setAddress(member.getAddress1() + " " + member.getAddress2());
		m.setGender(member.getGender());
		m.setUser_role("user");
		m.setJointime(new Date());
		m.setLogtime(new Date());
		
		return memberRepository.save(m) != null;
	}
	
	// 로그인
	public Object[] login(String id, String pw) {
		Member member = memberRepository.findByIdAndPassword(id, pw);
		// 1. 마지막 로그인 날짜와 오늘날짜가 다를경우 경험치 +10
		// 2. 경험치가 일정량이상(100)일경우 레벨업증가, 경험치 0으로 초기화
		
		// 로그인 실패
		if (member == null || member.getUser_role().equals("admin")) {
			return null;
		}
		
		Object[] values = new Object[2];
		
		// Calendar 객체를 생성하여 두 날짜의 년, 월, 일이 같은지 확인
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        
        Calendar otherCal = Calendar.getInstance();
        otherCal.setTime(member.getLogtime());
        
        boolean isSameDate = currentCal.get(Calendar.YEAR) == otherCal.get(Calendar.YEAR) &&
                             currentCal.get(Calendar.MONTH) == otherCal.get(Calendar.MONTH) &&
                             currentCal.get(Calendar.DAY_OF_MONTH) == otherCal.get(Calendar.DAY_OF_MONTH);
		
		// 날짜가 다를 경우, 경험치 +10
		if (!isSameDate) {
			member.setMember_exp(member.getMember_exp() + 10);
		}
		
		// 경험치가 100이상일 경우 레벨업, 경험치 0으로 초기화
		if (member.getMember_exp() >= 100) {
			member.setMember_level(member.getMember_level() + 1);
			member.setMember_exp(0);
		}
		
		// 마지막 로그인 날짜 현재 날짜로 수정
		member.setLogtime(new Date());
		
		memberRepository.save(member);
		
	    values[0] = member;
	    values[1] = isSameDate;

        
		return values;
	}
	
	// 회원 탈퇴
	public boolean memberLeave(String id, String password) {
		boolean result = false;
		Member member = memberRepository.findById(id).orElse(null);
		if (member != null) {
			memberRepository.delete(member);
		}
		
		// 삭제 확인
		member = memberRepository.findById(id).orElse(null);
		if (member == null) {
			result = true;
		}
		
		return result; 
	}
	
	// 블랙리스트 조회
	public boolean blacklistSearch(String id) {
		Blacklist blacklist = blacklistRepository.findById(id).orElse(null);
		if (blacklist != null) {
			return true;
		}
		return false;
	}
}
