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
public class positionBoardController {

	@Autowired
	BoardService boardService;
	@Autowired
	ApplicationContext applicationContext;
	
	
	@GetMapping("/board/positionBoardList")
	public String positionBoardList(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		// 1. 데이터 처리
		String board_category = "포지션 게시판";
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
		return "/board/positionBoardList";
	}

	@GetMapping("/board/positionBoardView")
	public String positionBoardView(Model model, HttpServletRequest request) {
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
		
		return "/board/positionBoardView";
	}

	@GetMapping("/board/positionBoardWriteForm")
	public String positionBoardWriteForm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		return "/board/positionBoardWriteForm";

	}

	@PostMapping("/board/positionBoardWrite")
	public String positionBoardWrite(Member member, BoardDTO dto, Model model, HttpServletRequest request,
			@RequestParam("img") MultipartFile multipartFile) {
		// 1. 데이터 처리
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("pg", session.getAttribute("pg"));
		String positionBoard = "포지션 게시판";
		dto.setBoard_category(positionBoard);
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
		return "/board/positionBoardWrite";
	}

	@GetMapping("/board/positionBoardModifyForm")
	public String positionBoardModifyForm(BoardDTO dto, Model model, HttpServletRequest request) {
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

		return "/board/positionBoardModifyForm";
	}

	@PostMapping("/board/positionBoardModify")
	public String positionBoardModify(BoardDTO dto, Model model, 
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

		return "/board/positionBoardModify";
	}

	// 데이터 삭제
	@GetMapping("/board/positionBoardDelete")
	public String positionBoardDelete(Model model, HttpServletRequest request) {
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
		return "/board/positionBoardDelete";
	}
		
	@GetMapping("/positionSearch")
	public String newsSearch(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		model.addAttribute("member", session.getAttribute("member"));
		String board_category = "포지션 게시판";
		// 데이터 처리
		String name = request.getParameter("name"); // 검색어 분류
		String keyword = request.getParameter("keyword"); // 검색어

		// 페이지 처리
		int pg = 1;
		if (request.getParameter("pg") != null) {
			pg = Integer.parseInt(request.getParameter("pg"));
		}
		int endnum = pg * 20;
		int startnum = endnum - 19;

		// 검색 결과 및 페이징 처리
		List<Board> list = null;
		int totalA = 0;

		// 검색 처리
		if (name == null || keyword == null) {
		// 적절한 에러 처리 또는 기본 동작 설정
			return "error";
		}

		// DB 조회
		if (board_category.equals("포지션 게시판")) {
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

		// 데이터 공유
		model.addAttribute("pageList", pageList);
		if (startPage > 10)
			model.addAttribute("previousPage", startPage - 1);
		if (endPage < totalP)
			model.addAttribute("nextPage", endPage + 1);
		model.addAttribute("pg", pg);
		model.addAttribute("list", list);

		// view 처리 파일 공유
		return "/board/positionBoardList";
	}
}
