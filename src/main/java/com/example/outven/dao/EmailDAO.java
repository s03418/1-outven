package com.example.outven.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.outven.email.RegisterMail;
import com.example.outven.email.IdsearchMail;
import com.example.outven.email.ModifyMail;
import com.example.outven.email.PwsearchMail;

import jakarta.mail.MessagingException;

@Repository
// 가입 기능
public class EmailDAO {
	@Autowired
	RegisterMail registerMail;
	@Autowired
	IdsearchMail IdsearchMail;
	@Autowired
	PwsearchMail PwsearchMail;
	@Autowired
	ModifyMail modifyMail;

	// 회원가입 코드 이메일 전송
	public String mailCode(String email) {
		String code = null;
		try {
			code = registerMail.sendSimpleMessage(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		// System.out.println("인증코드 : " + code);
		return code;
	}

	// 아이디 찾기 코드 이메일 전송
	public String idsearchMail(String name, String email) {
		String code = null;
		try {
			code = IdsearchMail.sendSimpleMessage(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return code;
	}

	// 비밀번호 찾기 코드 이메일 전송
	public String pwsearchMail(String id, String email) {
		String code = null;
		try {
			code = PwsearchMail.sendSimpleMessage(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return code;
	}

	// 회원정보 이메일 변경시 이메일 전송
	public String ModifyMail(String id, String email) {
		String code = null;
		try {
			code = modifyMail.sendSimpleMessage(email);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return code;
	}
}
