package com.example.outven.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champ_skin {

	// 챔피언 스킨
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "SKIN_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "SKIN_SEQUENCE_GENERATOR",
	sequenceName = "champ_sk_seq", initialValue = 1,
	allocationSize = 1)
	private int skin_code; 				// 스킨 번호(고유값)
	private int champ_code; 			// 챔피언 코드
	private String champ_skin_name; 	// 스킨 이름
	private double champ_skin_rate;	 	// 챔프 스킨 평점
	private String champ_icon_img; 		// 챔프 이미지 (아이콘)
	private String champ_skin_img; 		// 챔프 스킨 이미지
	private int skin_rp; 				// 스킨 가격
	@Temporal(TemporalType.DATE)
	private Date skin_logtime;          // 생성일

}
