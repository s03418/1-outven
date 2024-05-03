package com.example.outven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 스킨평점

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champskinrate {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		generator = "SKINRATE_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "SKINRATE_SEQUENCE_GENERATOR",
	sequenceName = "Champ_skin_rate", initialValue = 1,
	allocationSize = 1)
	private int rate_skin_num;		
	private int skin_code;			   // 스킨 코드
	private int champ_code;            // 챔피언 번호(현재 페이지의 챔프 코드 값 가져오기)
	private String member_id;            //아이디(로그인 아이디 세션 가져와서 평점 줬는지 안주었는지)
    private int rate;                  //점수
}
