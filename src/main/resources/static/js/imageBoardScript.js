function checkWriteForm() {	
	var frm = document.form1;
	var detail_category = frm.detail_category.value;
	// 입력검사
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
	if (!frm.img.value) {
        alert("파일을 첨부하세요.");
        return false;
    }
	if(!detail_category) {
		alert("카테고리를 선택하세요");
		frm.detail_category.focus();
		return false
	}
	frm.submit(); // 데이터 전송 
}

function checkBoardModify() {
	var frm = document.form1;
	var subject = frm.subject.value;
	var content = frm.content.value;
	if(!subject) {
		alert("제목을 입력하세요");
		frm.subject.focus();
		return false
	}
	if(!content) {
		alert("내용을 입력하세요");
		frm.content.focus();
		return false
	}
	if(!detail_category) {
		alert("카테고리를 선택하세요");
		frm.detail_category.focus();
		return false
	}
	frm.submit();
}