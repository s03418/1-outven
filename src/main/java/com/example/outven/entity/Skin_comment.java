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
public class Skin_comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "SKCOMMENT_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "SKCOMMENT_SEQUENCE_GENERATOR",
	sequenceName = "skin_com_seq", initialValue = 1,
	allocationSize = 1)
	private int skin_com_num;       // 챔피언 댓글 번호
	private int champ_code;             // 챔피언 코드
	private int skin_code;            //스킨 번호
    private String member_id;                    // ID
    private String nick_name;          // 회원 별명
    private String champskin_com;              //회원 댓글
    @Temporal(TemporalType.DATE)
    private Date comment_logtime;
    private int skin_re_ref;                     // 회원 그룹 번호(답글용)
    private int skin_re_lev;                    // 회원 단계
    private int skin_re_seq;                       // 회원 글순서
    @Transient
    private boolean com_judge; 		// 답글 판단 저장용
 
}
