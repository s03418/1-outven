package com.example.outven.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.outven.dto.BoardDTO;
import com.example.outven.dto.PageDTO;
import com.example.outven.entity.Board;
import com.example.outven.entity.Board_comment;
import com.example.outven.entity.Boardreport;
import com.example.outven.entity.Member;
import com.example.outven.entity.Recommend;
import com.example.outven.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
public class NewsBoardController {

	@Autowired
	BoardService boardService;
	@Autowired
	ApplicationContext applicationContext;
	
	// 메인 홈페이지로 이동
	@GetMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		List<Board> recommend_board = boardService.recommendBoardCategoryList(1, 8);	// 추천게시판
		List<Board> art_board = boardService.boardCategoryList("이미지게시판", 1, 4);
		List<Board> patch_board = boardService.boardCategoryList("패치 & 정보 게시판", 1, 10);
		List<Board> tip_board = boardService.boardCategoryList("팁 & 공략 게시판", 1, 10);
		List<Board> position_board = boardService.boardCategoryList("포지션 게시판", 1, 10);
		List<Board> acc_board = boardService.boardCategoryList("사건 & 사고 게시판", 1, 10);
		
		model.addAttribute("recommend_board", recommend_board);
		model.addAttribute("art_board", art_board);
		model.addAttribute("patch_board", patch_board);
		model.addAttribute("tip_board", tip_board);
		model.addAttribute("position_board", position_board);
		model.addAttribute("acc_board", acc_board);
		
		return "main/index";
	}

	@GetMapping("/board/reporter_news_BoardList")
	public String reporter_news_BoardList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		String board_category = "리포터 뉴스 게시판";
		String detail_category = request.getParameter("detail_category");
		// 전체목록보기 : 20개씩
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;
		List<Board> list = null;
		int totalA = 0;
		if (detail_category != null) {
			list = boardService.boardDetailList(board_category, detail_category, startnum, endnum);
			totalA = boardService.getDetailCount(board_category, detail_category);
			model.addAttribute("list", list);
			model.addAttribute("detail_category", detail_category);
			model.addAttribute("totalA", totalA);
		} else {
			list = boardService.boardCategoryList(board_category, startnum, endnum);
			totalA = boardService.getBoardCount(board_category);
			model.addAttribute("list", list);
			model.addAttribute("totalA", totalA);
		}
		
		// 전체목록 페이징 처리
		int totalP = (totalA + 19) / 20; // 총 페이지수
		int startPage = (pg - 1) / 10 * 10 + 1;
		int endPage = startPage + 9;
		if (endPage > totalP)
			endPage = totalP;

		// 페이징 번화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for (int i = startPage; i <= endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if (pg == i)
				pageDTO.setCurrent(true);

			pageList.add(pageDTO);
		}

		// 2. 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);

		boolean is_login = false; // 로그인 상태 저장
		if (session.getAttribute("memId") != null)
			is_login = true;

		model.addAttribute("is_login", is_login);
		return "/board/reporter_news_BoardList";
	}

	@GetMapping("/board/reporter_news_BoardView")
	public String reporter_news_BoardView(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		// db
		boardService.updateHit(board_num); // 조회수 증가
		Board board = boardService.boardView(board_num); // 상세보기 데이터

		// 로그인한 사람과 글쓴사람이 같은지 검사
		boolean isMemId = false;

		if (session.getAttribute("member") != null) {
			Member member = (Member) session.getAttribute("member");
			if (board.getMember_id().equals(member.getMember_id())) {
				isMemId = true;
			}
		}
		// 2. 데이터 공유
		model.addAttribute("board", board);
		model.addAttribute("pg", pg);
		model.addAttribute("board_num", board_num);
		model.addAttribute("isMemId", isMemId);
		// view처리
		
		// 24/02/27 댓글 처리 추가
		boolean listExist = false;
		int comPg = 1;
		if (request.getParameter("comPg") != null) {
			comPg = Integer.parseInt(request.getParameter("comPg"));
		}
		// System.out.println(comPg);
		// System.out.println(board_num);
		// 1-1) 목록보기 : 10개씩
		int endnum = comPg * 10;
		int startnum = endnum - 9;
		// db
		// champ_code 글이 있는지 확인 0개면 없음
		int listCount = boardService.getCount(board_num);
		if (listCount > 0) {
			List<Board_comment> boardCom = boardService.boardCommentList(startnum, endnum, board_num);
			// System.out.println("boardCom : " + boardCom.get(0));
			// 답글 구별용
			for (int i = 0; i < boardCom.size(); i++) {
				if(boardCom.get(i).getCom_re_lev() >0) {
					boardCom.get(i).setCom_judge(true);
				}else {
					boardCom.get(i).setCom_judge(false);
				}
			}
			// 삭제 버튼 구별용
			for  (int i = 0; i < boardCom.size(); i++) {
				if (session.getAttribute("member") != null) {
					Member member = (Member) session.getAttribute("member");
//					System.out.println("현재 접속 id : " + member.getMember_id());
//					System.out.println("게시글 작성자의 id : " + boardCom.get(i).getMember_id() );
					if(boardCom.get(i).getMember_id().equals(member.getMember_id())) {
						boardCom.get(i).setMatch_id(true);
					}else {
						boardCom.get(i).setMatch_id(false);
					}
				}
			}
			
			// System.out.println(boardCom.get(0));
			// 1-2) 페이징 처리
			int totalA = boardService.getCount(board_num); // 총 데이터 개수
			int totalP = (totalA + 9) / 10; // 총 페이지수

			int startPage = (comPg - 1) / 5 * 5 + 1;
			int endPage = startPage + 4;
			if (endPage > totalP) endPage = totalP;

			// 페이징 변화와 현재 페이지 정보를 리스트에 저장
			List<PageDTO> pageList = new ArrayList<>();
			for (int i = startPage; i <= endPage; i++) {
					PageDTO pageDTO = new PageDTO();
					pageDTO.setPage(i);
				if (comPg == i)
					pageDTO.setCurrent(true);

					pageList.add(pageDTO);
				}
				listExist = true;
				// 챔피언 댓글
				model.addAttribute("boardCom", boardCom);
				model.addAttribute("pageList", pageList);

				if (startPage > 5)
					model.addAttribute("previousPage", startPage - 1);
				if (endPage < totalP)
					model.addAttribute("nextPage", endPage + 1);
					
		}
		// 2. 데이터 공유
		// 챔피언 정보

		// 댓글
		model.addAttribute("comPg", comPg);
		model.addAttribute("listExist", listExist);
		
		return "/board/reporter_news_BoardView";
	}

	@GetMapping("/board/reporter_news_BoardWriteForm")
	public String reporter_news_BoardWriteForm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		return "/board/reporter_news_BoardWriteForm";

	}

	@PostMapping("/board/reporter_news_BoardWrite")
	public String reporter_news_BoardWrite(Member member, BoardDTO dto, Model model, HttpServletRequest request,
			@RequestParam("img") MultipartFile multipartFile) {
		// 1. 데이터 처리
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("pg", session.getAttribute("pg"));
		String reporter_news_Board = "리포터 뉴스 게시판";
		dto.setBoard_category(reporter_news_Board);
		dto.setMember_id((String) request.getParameter("member_id"));
		dto.setNick_name((String) request.getParameter("nick_name"));
		dto.setDetail_category((String) request.getParameter("detail_category"));
		dto.setBoard_title((String) request.getParameter("board_title"));
		dto.setBoard_content((String) request.getParameter("board_content"));
		dto.setBoard_logtime(new Date());
		
		String filePath = "static/storage";
		String fileName = "";

		
		// 파일이 없으면 저장 안됨
		if (multipartFile.isEmpty() == false) {
		    // 업로드된 파일 저장
		    try {
				// 파일을 이름으로 저장
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String formattedDate = dateFormat.format(new Date());
				fileName = formattedDate + "_" + multipartFile.getOriginalFilename();
		    	File file = new File(filePath, fileName);
		        // 디렉토리가 존재하지 않으면 생성
		        if (!file.getParentFile().exists()) {
		            file.getParentFile().mkdirs();
		        }
		        // copy(입력 스트림 객체, 출력 스트림 객체)
		        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		// dto에 파일명 저장
		dto.setBoard_img(fileName);

		boolean result = boardService.insert(dto);

		// 2. 데이터 공유
		model.addAttribute("result", result);
		model.addAttribute("pg", 1);
		return "/board/reporter_news_BoardWrite";
	}

	@GetMapping("/board/reporter_news_BoardModifyForm")
	public String reporter_news_BoardModifyForm(BoardDTO dto, Model model, HttpServletRequest request) {
		// 1. 데이터 처리하기
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		Board board = boardService.boardView(board_num);
		String detail_category = board.getDetail_category();
		// db
		
		String noimg = "no";
		if(board.getBoard_img() == null) {
			model.addAttribute("noimg", noimg);
		}
		
		// 2. 데이터 공유하기
		model.addAttribute("board", board);
		model.addAttribute("pg", pg);
		model.addAttribute("detail_category", detail_category);

		return "/board/reporter_news_BoardModifyForm";
	}

	@PostMapping("/board/reporter_news_BoardModify")
	public String reporter_news_BoardModify(BoardDTO dto, Model model, 
			HttpServletRequest request, @RequestParam(name = "img1", required = false) String multipartFile1,
										@RequestParam(name = "img2", required = false) MultipartFile multipartFile2) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		// 1. 데이터 처리하기
		
		// multi1은 기존에 저장되어있는 이미지
		// multi2는 새로 저장하는 이미지
		
		if (multipartFile2.isEmpty()) {	// 이미지 수정 없을 때 그대로 저장
			Board board = boardService.boardView(board_num);
			dto.setBoard_img(board.getBoard_img());
		} else { 	// 이미지 수정할 때
			// 파일을 저장할 경로 - 클래스패스의 루트부터 지정
			String filePath = "static/storage";
			// 파일을 이름으로 저장
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String formattedDate = dateFormat.format(new Date());
			String fileName = formattedDate + "_" + multipartFile2.getOriginalFilename();

			// 파일이 없으면 저장 안됨
			if (!fileName.equals("")) {
			    // 업로드된 파일 저장
			    try {
			        // 클래스패스 상의 실제 경로 가져오기
			        String rootPath = getClass().getClassLoader().getResource("").getFile();
			        File file = new File(rootPath + filePath, fileName);

			        // 디렉토리가 존재하지 않으면 생성
			        if (!file.getParentFile().exists()) {
			            file.getParentFile().mkdirs();
			        }

			        // copy(입력 스트림 객체, 출력 스트림 객체)
			        FileCopyUtils.copy(multipartFile2.getInputStream(), new FileOutputStream(file));
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
			// dto에 파일명 저장
			dto.setBoard_img(fileName);
		} 
	
		// db
		boolean result = boardService.boardModify(dto);
		// 2. 데이터 공유하기
		model.addAttribute("result", result);
		model.addAttribute("board_num", board_num);
		model.addAttribute("pg", pg);

		return "/board/reporter_news_BoardModify";
	}

	// 데이터 삭제
	@GetMapping("/board/reporter_news_BoardDelete")
	public String reporter_news_BoardDelete(Model model, HttpServletRequest request) {
		// 1. 데이터 처리
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		// db

		boolean result = boardService.boardDelete(board_num);

		// 2. 데이터 공유

		model.addAttribute("pg", pg);
		model.addAttribute("result", result);

		// 3. view 처리 파일 선택
		return "/board/reporter_news_BoardDelete";
	}
		
	// 리포터 뉴스 검색
	@GetMapping("/newsSearch")
	public String newsSearch(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		String board_category = "리포터 뉴스 게시판";
		// 데이터 처리
		String name = request.getParameter("name"); // 검색어 분류
		String keyword = request.getParameter("keyword"); // 검색어

		// 페이지 처리
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 4;
		int startnum = endnum - 3;

		// 검색 결과 및 페이징 처리
		List<Board> list = null;
		int totalA = 0;

		// 검색 처리
		if (name == null || keyword == null) {
		// 적절한 에러 처리 또는 기본 동작 설정
			return "error";
		}

		// DB 조회
		if (board_category.equals("리포터 뉴스 게시판")) {
			if (name.equals("subject")) {
				list = boardService.searchByBoard_titleAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getTitleCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("content")) {
				list = boardService.searchByBoard_contentAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getContentCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("nicname")) {
				list = boardService.searchByNick_nameAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getNickNameCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("category")) {
				list = boardService.searchByMember_idAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getMemberIdCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else if (name.equals("subjcont")) {
				list = boardService.searchByKeywordAndStartnumAndEndnumAndBoardCategory(keyword, startnum, endnum, board_category);
				totalA = boardService.getKeywordCount(keyword); // 수정: 리턴된 리스트의 사이즈 사용
			} else {
			// 유효하지 않은 검색어 분류일 경우 적절한 에러 처리
				list = boardService.boardCategoryList(board_category, startnum, endnum);
				totalA = boardService.getBoardCount(board_category);
			}
		} else {
			list = boardService.boardCategoryList(board_category, startnum, endnum);
			totalA = boardService.getBoardCount(board_category);
		}
		// 전체목록 페이징 처리
		int totalP = (totalA + 3) / 4; // 총 페이지수
		int startPage = (pg - 1) / 10 * 10 + 1;
		int endPage = startPage + 9;
		if (endPage > totalP)
			endPage = totalP;

		// 페이징 번화와 현재 페이지 정보를 리스트에 저장
		List<PageDTO> pageList = new ArrayList<>();
		for (int i = startPage; i <= endPage; i++) {
			PageDTO pageDTO = new PageDTO();
			pageDTO.setPage(i);
			if (pg == i)
				pageDTO.setCurrent(true);

			pageList.add(pageDTO);
		}

		// 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);
		model.addAttribute("list", list);
		model.addAttribute("pg", 1);

		// view 처리 파일 공유
		return "/board/reporter_news_BoardList";
	}

	// 24/02/23 추천 컨트롤러(추가)
	@GetMapping("/board/recommend")
	public String recommend(HttpServletRequest request, Model model, Recommend recommend) {
		// 추천 테이블에 값을 삽입하고 이후 board 테이블에서 board_num을 조회해 값을 불러오고 추천수의 값을 수정
		// 출력 확인
		boolean result = false;
		System.out.println("recommend Test");
		int pg = Integer.parseInt(request.getParameter("pg"));
		// 추천 테이블 입력
		// 데이터 처리

		System.out.println("board_num : " + recommend.getBoard_num() + " member_id : " + recommend.getMember_id());
		System.out.println(recommend.toString());
		// db
		boolean reCheck = boardService.insertRecomment(recommend);
		// True일시 입력 성공 false일시 입력 실패
		System.out.println(reCheck);

		// reCheck 값이 참일때
		if (reCheck) {
			result = boardService.updateRecommned(recommend.getBoard_num());
		}

		// 데이터 공유
		model.addAttribute("result", result);
		model.addAttribute("board_num", recommend.getBoard_num());
		model.addAttribute("pg", pg);

		// 뷰 파일 처리
		return "/board/recommend";
	}

	// 24/02/23 ajax처리
	@PostMapping("/checkRecommend")
	@ResponseBody
	public boolean checkRecommend(@RequestParam("board_num") int board_num,
			@RequestParam("member_id") String member_id) {
		System.out.println("checkRecommend 진입 확인");
		System.out.println("board_num : " + board_num + " member_id : " + member_id);

		boolean result = boardService.recomCheck(board_num, member_id);
		System.out.println(result);

		return result;
	}

	// 24/02/23 게시판 신고 기능
	@GetMapping("/reportForm")
	public String reportForm(HttpServletRequest request, Model model) {
		// 데이터 처리
		String member_id = request.getParameter("member_id");
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int pg = Integer.parseInt(request.getParameter("pg"));
		System.out.println("member_id = " + member_id + " board_num : " + board_num + " pg : " + pg);
		Board board = boardService.boardView(board_num);
		// 데이터 공유
		model.addAttribute("member_id", member_id);
		model.addAttribute("board_num", board_num);
		model.addAttribute("board", board);
		model.addAttribute("pg", pg);

		return "/board/reportForm";
	}

	// 24/02/23 추가
	@PostMapping("/report")
	@ResponseBody
	public boolean report(@ModelAttribute Boardreport boardreport) {
		System.out.println("report 접근 테스트");
		System.out.println(boardreport.toString());
		// 신고하기
		boolean result = false;
		boolean ins_result = boardService.reportWrite(boardreport);

		// 입력성공시
		if (ins_result) {
			result = boardService.updateReport(boardreport.getBoard_num());
		}

		return result;
	}

	// 24/02/26 신고 중복 확인

	@PostMapping("/checkReport")
	@ResponseBody
	public boolean checkReport(@RequestParam("board_num") int board_num, @RequestParam("member_id") String member_id) {

		boolean result = false;
		// System.out.println("checkReport 진입 확인");
		// System.out.println("board_num : " + board_num + " member_id : " + member_id);

		result = boardService.reportCheck(board_num, member_id);
		System.out.println(result);

		return result;
	}

	// 24/02/27 게시판 댓글 작성
	@PostMapping("/board/commentWrite")
	public String commentWrite(HttpServletRequest request, Model model, Board_comment comment) {
		// 1. 데이터 처리
		System.out.println("진입 확인");
		int pg = Integer.parseInt(request.getParameter("comPg"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		String member_id = member.getMember_id();
		String nick_name = member.getNick_name();

		comment.setMember_id(member_id);
		comment.setNick_name(nick_name);
		comment.setComment_logtime(new Date());
		System.out.println(comment.toString());

		boolean result = boardService.commentWrite(comment);
		System.out.println(result);

		// 2. 데이터 공유
		model.addAttribute("result", result);
		model.addAttribute("board_num", board_num);
		model.addAttribute("pg", pg);

		return "/board/commentWrite";
	}

	// 24/02/27 대댓글 기능
	// /board/recomment
	@PostMapping("/board/recomment")
	public String recommentWrite(HttpServletRequest request, Model model, Board_comment comment) {
		// 1. 데이터 처리
		int pg = Integer.parseInt(request.getParameter("comPg"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));

		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		String member_id = member.getMember_id();
		String nick_name = member.getNick_name();

		comment.setMember_id(member_id);
		comment.setNick_name(nick_name);
		comment.setComment_logtime(new Date());
		System.out.println("recommnet write 진입 확인");
		System.out.println(comment.toString());

		// db
		boolean result = boardService.recomment(comment);
		System.out.println("result = " + result);
		// 2. 데이터 공유
		model.addAttribute("pg", pg);
		model.addAttribute("result", result);
		model.addAttribute("board_num", board_num);

		// 3. view 파일 처리
		return "/board/recomment";
	}

	// 24/02/27 댓글 삭제 기능
	// /board/commentDelete
	@GetMapping("/commentDelete")
	public String commentDelete(HttpServletRequest request, Model model) {
		// 1. 데이터 처리
		System.out.println("delete 접근 테스트");
		int comment_num = Integer.parseInt(request.getParameter("comment_num"));
		boolean result = boardService.commentDelete(comment_num);
		// 2. 데이터 공유
		model.addAttribute("result", result);
		// 3. view 파일처리
		return "/board/commentDelete";
	}
}
