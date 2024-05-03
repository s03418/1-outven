package com.example.outven.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Join_comskin {
	@Id
	private int skin_com_num;        // 챔피언 댓글 번호
	private int champ_code;            //챔피언 코드
	private int skin_code;               // 스킨 번호
	private String member_id;                   // ID
    private String nick_name;             // 회원 별명
    private String champskin_com;             //회원 댓글
    @Temporal(TemporalType.DATE)
    private Date comment_logtime;
    private String champ_icon_img;
    private double champ_skin_rate;
    private String champ_skin_name;
}
