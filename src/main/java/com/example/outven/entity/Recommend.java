package com.example.outven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 추천 게시판
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommend {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
	generator = "RECOM_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "RECOM_SEQUENCE_GENERATOR", 
	sequenceName = "recom_num_seq", initialValue = 1, 
	allocationSize = 1)
	private int recom_num; // 추천 번호
	private int board_num; // 게시글 번호
	private String member_id; // 아이디
}
