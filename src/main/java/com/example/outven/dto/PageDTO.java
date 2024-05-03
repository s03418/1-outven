package com.example.outven.dto;

import lombok.Data;

@Data
public class PageDTO {
	private int page;			// 페이지
	private boolean current;	// 현재페이지
}
