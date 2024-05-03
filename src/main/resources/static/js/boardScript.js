function checkWriteForm() {
	var frm = document.boardwrite_form1;
	
	// 입력검사
	if (!frm.detail_category.value) {
		alert("카테고리를 선택하세요.");
		frm.detail_category.focus();		
		return false;
	}
	if (!frm.board_title.value) {
		alert("제목을 입력하세요.");
		frm.board_title.focus();		
		return false;
	}
	if (!frm.board_content.value) {
		alert("내용을 입력하세요.");
		frm.board_content.focus();		
		return false;
	}
	
	frm.submit(); // 데이터 전송
}
function checkBoardModify() {
	var frm = document.boardmodify_form1;
	var detail_category = frm.detail_category.value;
	var board_title = frm.board_title.value;
	var board_content = frm.board_content.value;
	if(!detail_category) {
		alert("카테고리를 선택하세요");
		frm.detail_category.focus();
		return false
	}
	if(!board_title) {
		alert("제목을 입력하세요");
		frm.board_title.focus();
		return false
	}
	if(!board_content) {
		alert("내용을 입력하세요");
		frm.board_content.focus();
		return false
	}
	frm.submit();
}