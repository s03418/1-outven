package com.example.outven.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.outven.dao.ChampionCommentDAO;
import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Champion;
import com.example.outven.entity.Champion_comment;
import com.example.outven.entity.Championrate;
import com.example.outven.entity.Member;
import com.example.outven.service.ChampionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ChampionController {

	// 챔피언 정보 컨트롤러

	@Autowired
	ChampionService championService;

	// 챔피언 리스트 출력되게끔
	@GetMapping("/champ_board/championList")
	public String championList(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		List<Champion> champList = championService.champList();

//		for(int i =0; i<champList.size(); i++) {
//			// 챔프이름이 6자 이상이면 5글자 까지 나오게 자름
//			if(champList.get(i).getChamp_name().length()>5) {
//			champList.get(i).setChamp_name(champList.get(i).getChamp_name().substring(0, 5));
//			System.out.println((champList.get(i).getChamp_name()));
//			}
//		}
		// json 객체로 만듦

		// json 데이터를 브라우저로 전송
		// champlist.

		// 2. 데이터 공유
		model.addAttribute("champList", champList);
		
		// 24-02-21 : 세션 추가
		// 세션 공유
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);

		// 3. view 파일 처리
		return "/champ_board/championList";
	}

	// 챔피언 상세보기 + 댓글 목록
	@GetMapping("/champ_board/championView")
	public String championView(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		// 아이디 값을 가져옴
		int champ_code = Integer.parseInt(request.getParameter("champ_code"));
		boolean listExist = false; // list 값이 null 일때
		// db
		// 챔피언 상세보기
		Champion champion = championService.championView(champ_code);

		// 챔피언 댓글 창
		// list 반환(champ_code 값이 일치한 값만)
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}

		// 1-1) 목록보기 : 10개씩
		int endnum = pg * 10;
		int startnum = endnum - 9;
		// db
		// champ_code 글이 있는지 확인 0개면 없음
		int listCount = championService.getCount(champ_code);
		if (listCount > 0) {
			List<Champion_comment> champCom = championService.champCommentList(startnum, endnum, champ_code);
			System.out.println("champCom : " + champCom.get(0));
			// boolean com_judge comment_judge에 있는 문자열 값을 boolean 값으로 변경해 저장할변수

			for (int i = 0; i < champCom.size(); i++) {
				if(champCom.get(i).getCom_re_lev() >0) {
				champCom.get(i).setCom_judge(true);
				}else {
					champCom.get(i).setCom_judge(false);
				}
			}

			System.out.println(champCom.get(0).toString());
//		System.out.println("pg = " + pg + ", champ_code = " +champ_code);
//		System.out.println(champCom);

			// 1-2) 페이징 처리
			int totalA = championService.getCount(champ_code); // 총 데이터 개수
			int totalP = (totalA + 9) / 10; // 총 페이지수

			int startPage = (pg - 1) / 5 * 5 + 1;
			int endPage = startPage + 4;
			if (endPage > totalP)
				endPage = totalP;

			// 페이징 변화와 현재 페이지 정보를 리스트에 저장
			List<PageDTO> pageList = new ArrayList<>();
			for (int i = startPage; i <= endPage; i++) {
				PageDTO pageDTO = new PageDTO();
				pageDTO.setPage(i);
				if (pg == i)
					pageDTO.setCurrent(true);

				pageList.add(pageDTO);
			}
			listExist = true;
			// 챔피언 댓글
			model.addAttribute("champCom", champCom);
			model.addAttribute("pageList", pageList);

			if (startPage > 5)
				model.addAttribute("previousPage", startPage - 1);
			if (endPage < totalP)
				model.addAttribute("nextPage", endPage + 1);
		}
		
		// 2. 데이터 공유
		
		// 24-02-21 : 세션 추가
		// 세션 공유
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);
	
		

		// 챔피언 정보
		model.addAttribute("champion", champion);

		// 댓글
		model.addAttribute("pg", pg);
		model.addAttribute("listExist", listExist);

		// 로그인 유무
		// 3. view 파일 처리

		return "/champ_board/championView";
	}

	// 챔피언 평점 업데이트
	@GetMapping("/champ_board/championRateUpdate")
	public String championRateUpdate(Model model, HttpServletRequest request) {
		System.out.println("test222");
		// 1. 데이터 처리
		int champ_code = Integer.parseInt(request.getParameter("champ_code"));
		int rate = Integer.parseInt(request.getParameter("rate"));
		boolean result = false;
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		// db
		// 평점 입력하기
		Championrate championRate = new Championrate();
		championRate.setChamp_code(champ_code);
		championRate.setRate(rate);
		championRate.setMember_id(member.getMember_id());
		// System.out.println(championRate.toString());
		championService.champRateWrite(championRate);

		// 평점 평균 구하기
		double avg = championService.champRateAvg(champ_code);
		avg = Double.parseDouble(String.format("%.2f", avg));
		System.out.println(avg);

		// 평점 평균을 업데이트
		result = championService.champRateUpdate(champ_code, avg);
		System.out.println(result);

		model.addAttribute("result", result);
		model.addAttribute("champ_code", champ_code);
		return "/champ_board/championRateUpdate";
	}

	// 댓글 작성하기
	@PostMapping("/champ_board/commentWrite")
	public String commentWrite(Model model, HttpServletRequest request, Champion_comment comment) {
		// 1. 데이터 처리
		int pg = Integer.parseInt(request.getParameter("pg"));
		int champ_code = Integer.parseInt(request.getParameter("champ_code"));
		
		HttpSession session = request.getSession();
		// 세션안에 있는 아이디, 닉네임 가져옴
		Member member = (Member) session.getAttribute("member");
		String member_id = member.getMember_id();
		String nick_name = member.getNick_name();
		
		comment.setChamp_code(champ_code);
		comment.setMember_id(member_id);
		comment.setNick_name(nick_name);
		comment.setComment_logtime(new Date());
		boolean result = championService.commentWrite(comment);
		System.out.println(result);
		// 2. 데이터 공유

		model.addAttribute("result", result);
		model.addAttribute("champ_code", champ_code);

		// 3. view 파일 처리
		return "/champ_board/commentWrite";
	}

	// 답글 작성
	@PostMapping("/champ_board/recomment")
	public String recomment(Model model, HttpServletRequest request, Champion_comment comment) {
		// 1. 데이터 처리
		int pg = Integer.parseInt(request.getParameter("pg"));
		int champ_code = comment.getChamp_code();
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		comment.setMember_id(member.getMember_id());
		comment.setNick_name(member.getNick_name());
		comment.setComment_logtime(new Date());

		System.out.println(comment.toString());

		// db 처리
		boolean result = championService.recomment(comment);
		System.out.println(result);

		// 2. 데이터 공유
		model.addAttribute("pg", pg);
		model.addAttribute("result", result);
		model.addAttribute("champ_code", champ_code);
		// 3.
		return "/champ_board/recomment"; // "/champ_board/recomment";
	}
	
	// 중복 평점 확인
	@PostMapping("/checkRate")
	@ResponseBody
	public boolean checkRate (@RequestParam("champ_code") int champ_code,
							  @RequestParam("member_id") String member_id) {
		
//		System.out.println("checkRate Test");
//		System.out.println("champ_code : " + champ_code + " member_id : " + member_id);
	
		boolean result = championService.champRateCheck(champ_code, member_id);
		
		System.out.println(result);
		
		return result;
	}
	
}
