function joinFormCheck() {
	var frm = document.form1;
	// 입력검사
	if (!frm.member_id.value) {
		alert("아이디를 입력하세요");
		frm.id.focus();
		return false;
	}
	if (!frm.password1.value) {
		alert("비밀번호를 입력하세요");
		frm.password1.focus();
		return false;
	}
	if (frm.password1.value.length < 8) {
		alert("비밀번호를 8자리이상 입력하세요");
		frm.password1.focus();
		return false;
	}
	if (!frm.password2.value) {
		alert("비밀번호 재입력을 입력하세요");
		frm.password2.focus();
		return false;
	}
	if (frm.password1.value != frm.password2.value) {
		alert("비밀번호가 서로 다릅니다");
		frm.pwd2.focus();
		frm.pwd2.value = "";
		return false;
	}
	if (!frm.membername.value) {
		alert("이름을 입력하세요");
		frm.membername.focus();
		return false;
	}
	if (!frm.year.value) {
		alert("생년월일을 입력하세요");
		frm.year.focus();
		return false;
	}
	if (!frm.month.value) {
		alert("생년월일을 입력하세요.");
		frm.month.focus();
		return false;
	}
	if (!frm.day.value) {
		alert("생년월일을 입력하세요.");
		frm.day.focus();
		return false;
	}
	if (!frm.gender.value) {
		alert("성별을 선택하세요");
		return false;
	}
	if (!frm.email.value) {
		alert("이메일을 입력하세요");
		frm.email.focus();
		return false;
	}
	if (!frm.gender.value) {
		alert("성별을 선택하세요");
		frm.gender.focus();
		return false;
	}
	if (!frm.phone.value) {
		alert("연락처를 입력하세요")
		frm.phone.focus();
		return false;
	}
	if (!frm.address1.value || !frm.address2.value) {
		alert("주소를 입력하세요")
		return false;
	}
	
	if (!frm.terms.checked) {
		alert("이용약관 및 개인정보처리방침 사안에 동의해주십시오")
		return false;
	}
	
	formSubmit();
}