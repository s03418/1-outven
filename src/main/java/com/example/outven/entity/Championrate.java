package com.example.outven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 평점
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Championrate {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "RATE_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "RATE_SEQUENCE_GENERATOR",
	sequenceName = "rate_seq", initialValue = 1,
	allocationSize = 1)
	private int rate_num;
	private int champ_code; 	// 챔피언 코드
	private String member_id;		// 유저 id
	private int rate;			// 챔피언 평점
	
}	
