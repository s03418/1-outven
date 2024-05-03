package com.example.outven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champion {
	@Id
	 private int champ_code; 	// 챔피언 코드
	 private String champ_name;         	// 챔피언 이름
	 private String champ_nick_name;    	// 챔피언 이명(수식어)
	 private String champ_position;     	// 챔피언 포지션(전사, 마법사, 암살자, etc...)
	 private double champ_rate;           // 챔피언 평점(ex 3.xx) 조건 0이면 그대로 그보다 크면 더하고 0.5 곱하기
	 private String champ_image;          // 챔피언 이미지(픽창 이미지)
	 private String champ_content;    	// 챔피언 설명
	 private int rp;                // 실제재화(캐쉬)
	 private int be;                // 인게임 재화
}
