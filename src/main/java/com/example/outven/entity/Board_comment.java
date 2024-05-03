package com.example.outven.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board_comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "COMMENT_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "COMMENT_SEQUENCE_GENERATOR",
	sequenceName = "comment_num_seq", initialValue = 1,
	allocationSize = 1)
	private int comment_num; 			// 댓글 번호
	private int board_num; 				// 게시글 번호
	private String member_id; 			// 아이디
	private String nick_name; 			// 별명
	private String comment_content; 	// 댓글 내용
	@Temporal(TemporalType.DATE)
	private Date comment_logtime;		// 게시일
	private int com_re_ref; 			// 회원 그룹 번호(답글용)
	private int com_re_lev; 			// 회원 단계
	private int com_re_seq; 			// 회원 글순서
	@Transient
	private boolean com_judge;			// 답글 구별용
	@Transient
	private boolean match_id;			// 삭제 버튼 구별용
}
