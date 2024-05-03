package com.example.outven.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boardreport {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "REPORT_SEQUENCE_GENERATOR")
	@SequenceGenerator(name = "REPORT_SEQUENCE_GENERATOR",
	sequenceName = "report_num_seq", initialValue = 1,
	allocationSize = 1)
	private int report_num;      // 신고 번호
	private String member_id;    //신고한 사람의 id
	private int board_num;       // 신고된 글의 번호
	private int report_category; // 신고사유(숫자 처리)
}
