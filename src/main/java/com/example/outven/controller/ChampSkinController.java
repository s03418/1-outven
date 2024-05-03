package com.example.outven.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.outven.entity.Champion;
import com.example.outven.entity.Champion_comment;
import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Champ_skin;
import com.example.outven.entity.Champskinrate;
import com.example.outven.entity.Join_comskin;
import com.example.outven.entity.Member;
import com.example.outven.entity.Skin_comment;
import com.example.outven.service.ChampSkinService;
import com.example.outven.service.ChampionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import netscape.javascript.JSObject;

@Controller
public class ChampSkinController {

	// 챔프 스킨 갤러리 컨트롤러

	@Autowired
	ChampSkinService champSkinService;

	// 챔피언 리스트
	@GetMapping("/champ_skin/champSkinList")
	public String champSkinList(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		List<Champion> champList = champSkinService.champList();

		// 2. 데이터 공유
		
		// 24-02-21 : 세션 추가
		// 세션 공유
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);
		model.addAttribute("champList", champList);
		// 3. view 파일 처리

		return "/champ_skin/champSkinList";
	}

	// 챔피언 스킨 화면
	@GetMapping("/champ_skin/champSkinView")
	public String champSkinView(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		int champ_code = Integer.parseInt(request.getParameter("champ_code"));
		int skin_code = 0;
		if (request.getParameter("skin_code") != null) {
			skin_code = Integer.parseInt(request.getParameter("skin_code"));
		}
		boolean commentView = true;
		if(skin_code == 0) commentView = false;
		Champ_skin detail_view = null;
		// 챔피언 댓글 창
		// list 반환(champ_code 값이 일치한 값만)
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		boolean listExist = false; // list 값이 null 일때 챔피언 코드 일치한 스킨 댓글
		boolean skinExist = false; // 챔피언코드와 스킨 코드가 일치한 댓글
		// 1-1) 목록보기 : 10개씩
		int endnum = pg * 10;
		int startnum = endnum - 9;
		int totalA = 0;
		
		// db
		List<Champ_skin> champ_skin_list = champSkinService.champSkinList(champ_code);
		// skin_code 값이 있을때
		if (skin_code > 0) {
			// System.out.println(skin_code + " + " + champ_code);
			detail_view = champSkinService.champSkinView(champ_code, skin_code);
			// System.out.println(detail_view.toString());
			// 댓글 입력 가능한 댓글리스트
			
			// skin_code에 글이 있는지 확인 0개면 없음
			int skinCount = champSkinService.getCountSkin(skin_code);
			// System.out.println("skinCount : " + skinCount);
			// 값이 있으면
			if(skinCount >0) {
				// System.out.println("test3");
				List<Skin_comment> skinCom = champSkinService.skinCommentList(startnum, endnum, skin_code);
				// 출력 확인
				System.out.println(skinCom);
				// boolean com_judge comment_judge에 있는 문자열 값을 boolean 값으로 변경해 저장할변수
				for(int i =0; i<skinCom.size(); i++) {
					if(skinCom.get(i).getSkin_re_lev() > 0) {
					skinCom.get(i).setCom_judge(true);
					}else {
						skinCom.get(i).setCom_judge(false);
					}
				}
				
				totalA = champSkinService.getCountSkin(skin_code);
				// 스킨 댓글 리스트
				model.addAttribute("skinCom", skinCom);
				skinExist = true;
			}
			
			// 스킨 상세보기 모델
			model.addAttribute("detail_view", detail_view);
			
		} else {
			// 챔피언 코드가 일치한 댓글을 찾기 댓글리스트가 나오고 댓글 번호순대로 출력
			// System.out.println("test1");
			// System.out.println(champ_code);
			// skin_code = 1;
			// champ_code 글이 있는지 확인 0개면 없음
			int champCount = champSkinService.getCountChamp(champ_code);
			// System.out.println(champCount);
			if (champCount > 0) {
				// System.out.println("test2");
				List<Join_comskin> champCom = champSkinService.champSkinCommentList(startnum, endnum, champ_code);
				// System.out.println(champCom);
				totalA = champSkinService.getCountChamp(champ_code); // 총 데이터 개수
				listExist = true;
				commentView = false;
				// 챔피언 댓글
				model.addAttribute("champCom", champCom);
			}
			

		}

		// 1-2) 페이징 처리
		int totalP = (totalA + 9) / 10; // 총 페이지수

		int startPage = (pg - 1) / 5 * 5 + 1;
		int endPage = startPage + 4;
		if (endPage > totalP)
			endPage = totalP;
		
		// System.out.println("totalA : " + totalA);
		// System.out.println("totalP : " + totalP);
		
		// System.out.println("startPage : " + startPage);
		// System.out.println("endPage : " + endPage);
		
		
		// 페이징 변화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for (int i = startPage; i <= endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if (pg == i)
				pageDTO.setCurrent(true);

			pageList.add(pageDTO);
		}
		
		model.addAttribute("pageList", pageList);
		if (startPage > 5) model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP) model.addAttribute("nextPage", endPage + 1);
		// System.out.println(champ_skin_list);

		// db
		// champ_code 글이 있는지 확인 0개면 없음

		// 2. 데이터 공유
		// 24-02-21 : 세션 추가
		// 세션 공유
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);
		
		// 챔피언 스킨 리스트
		model.addAttribute("champ_skin_list", champ_skin_list);
		
		// 댓글
		// System.out.println("listExist : " + listExist);
		// System.out.println("skinExist : " + skinExist);
		model.addAttribute("pg",pg);
		model.addAttribute("listExist", listExist);
		model.addAttribute("skinExist", skinExist);
		model.addAttribute("champ_code", champ_code);
		model.addAttribute("skin_code", skin_code);
		model.addAttribute("commentView", commentView);
		// 3. view 파일 처리
		return "/champ_skin/champSkinView";
	}

	// 쳄피언 스킨 평점
	@GetMapping("/champ_skin/skinRateUpdate")
	public String skinRateUpdate(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		int champ_code = Integer.parseInt(request.getParameter("champ_code"));
		int skin_code = Integer.parseInt(request.getParameter("skin_code"));
		int rate = Integer.parseInt(request.getParameter("rate"));

		// System.out.println("champ_code = " + champ_code);
		// System.out.println("skin_code = " + skin_code);
		// System.out.println("rate = " + rate);
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		// 스킨 평점 입력하기
		Champskinrate skinRate = new Champskinrate();
		skinRate.setChamp_code(champ_code);
		skinRate.setSkin_code(skin_code);
		skinRate.setMember_id(member.getMember_id());
		skinRate.setRate(rate);
		// System.out.println(skinRate.toString());
		champSkinService.skinRateWrite(skinRate);

		boolean result = false;

		// 평점 평균 구하기
		double avg = champSkinService.skinRateAvg(champ_code, skin_code);
		avg = Double.parseDouble(String.format("%.2f", avg));
		// System.out.println(avg);

		// 평점 평균을 업데이트
		Champ_skin champskin = champSkinService.champSkinView(champ_code, skin_code);
		// System.out.println();

		result = champSkinService.skinRateUpdate(champskin, avg);
		// System.out.println(result);

		// 2. 데이터 공유
		model.addAttribute("result", result);
		model.addAttribute("champ_code", champ_code);
		model.addAttribute("skin_code", skin_code);

		// 3. view 처리값 리턴
		return "/champ_skin/skinRateUpdate";

	}
	// 챔피언 스킨 댓글 입력
	@PostMapping("/champ_skin/skinComWrite")
	public String skinComWrite(Model model, HttpServletRequest request, Skin_comment comment) {
		// 1. 데이터 처리
		int pg = Integer.parseInt(request.getParameter("pg"));
		// System.out.println("test!!");
		// System.out.println(pg);
		// System.out.println("champ_code : " + skin_comment.getChamp_code());
		// System.out.println("skin_code : " + skin_comment.getSkin_code());
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		comment.setMember_id(member.getMember_id());
		comment.setNick_name(member.getNick_name());
		comment.setComment_logtime(new Date());
		// db
		boolean result = champSkinService.skinComWrite(comment);
		// System.out.println(result);
		
		// 2. 데이터 공유
		model.addAttribute("pg", pg);
		model.addAttribute("result", result);
		model.addAttribute("champ_code", comment.getChamp_code());
		model.addAttribute("skin_code", comment.getSkin_code());
		
		
		return "/champ_skin/skinComWrite";
	}
	
	// 답글
	// /champ_skin/recommnt
	@PostMapping("/champ_skin/recomment")
	public String recommnt(Model model, HttpServletRequest request, Skin_comment comment) {
		// 1. 데이터 처리
		// System.out.println("recomment Test");
		int pg = Integer.parseInt(request.getParameter("pg"));
		
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("member");
		comment.setMember_id(member.getMember_id());
		comment.setNick_name(member.getNick_name());
		comment.setComment_logtime(new Date());
		
		// System.out.println(comment.toString());
		
		//db 
		boolean result = champSkinService.skinRecomment(comment);
		// System.out.println("컨트롤러 result : " + result);
		
		// 2. 데이터 공유
		model.addAttribute("pg", pg);
		model.addAttribute("result", result);
		model.addAttribute("champ_code", comment.getChamp_code());
		model.addAttribute("skin_code", comment.getSkin_code());
		
		// 3. View 파일 처리
		return "/champ_skin/recomment";
	}
	
	// 중복 평점 확인
	@PostMapping("/checkSkinRate")
	@ResponseBody
	public boolean checkSkinRate(@RequestParam("champ_code") int champ_code,
								@RequestParam("skin_code") int skin_code,
			  					@RequestParam("member_id") String member_id) {
		boolean result = champSkinService.skinRateCheck(champ_code, skin_code, member_id);
		
		// System.out.println(result);
		
		return result;
	}
	

}
