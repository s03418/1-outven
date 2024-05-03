package com.example.outven.entity;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Board {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "BOARD_SEQUENCE_GENERATOR")
	@SequenceGenerator(name="BOARD_SEQUENCE_GENERATOR",	sequenceName = "board_num", initialValue = 1, allocationSize = 1)
	@Id
    private int board_num;              	// 글번호(고유값)
    private String board_category;     		// 카테고리(신고)
    private String detail_category;    		// 카테고리(욕설)
    private String member_id;         		// 아이디(join 사용)
    private String nick_name;       		// 닉네임
    private String board_title;         	// 글제목
    private String board_content;   		// 글내용
    private String board_img;   			// 파일,이미지
    @Temporal(TemporalType.DATE)
    Date board_logtime;          			// 작성일
    private int board_hit;              	// 조회수(게시글 들어가면 올라가게끔)
    @ColumnDefault("0")
    private int board_recommend;        	// 추천수(버튼 누르면 증가하게끔)
    private int board_report_count;   		// 신고횟수
    
	
	
	
}
