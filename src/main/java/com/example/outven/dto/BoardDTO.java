package com.example.outven.dto;

import java.util.Date;

import com.example.outven.entity.Board;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BoardDTO {
	
    private int board_num;               	// 글번호(고유값)
    private String board_category;     		// 카테고리(신고)
    private String detail_category;    		// 카테고리(욕설)
    private String member_id;         		// 아이디(join 사용)
    private String nick_name;       		// 닉네임
    private String board_title;           	// 글제목
    private String board_content;   		// 글내용
    private String board_img;   			// 파일,이미지
    Date board_logtime;          			// 작성일
    private int board_hit;                	// 조회수(게시글 들어가면 올라가게끔)
    private int board_recommend;        	// 추천수(버튼 누르면 증가하게끔)
    private int board_report_count;   		// 신고횟수
	
	
	public Board toEntity() {
    	return new Board(board_num,board_category,detail_category,member_id,nick_name,board_title,board_content,board_img,board_logtime,board_hit,board_recommend,board_report_count);    	
    }

}
