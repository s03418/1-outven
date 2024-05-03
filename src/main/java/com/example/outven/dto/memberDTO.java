package com.example.outven.dto;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class memberDTO {
	private String member_id; 		// 아이디(고유값)
	private String membername;		// 이름
	private String nick_name; 		// 별명
	private String password1; 		// 패스워드
	private String email;			// 이메일
	private String phone; 			// 전화번호
	private String year;
	private String month;
	private String day;
	private String address1; 		// 주소1
	private String address2; 		// 주소1
	private String gender; 			// 성별(남자면 m 여자면 f)
	private String user_role;		// 관리자(admin) 유저(user)
	private Date jointime;			// 가입한 날짜
	private int member_level;       // 레벨
	private int member_exp;         // 경험치 바
}
