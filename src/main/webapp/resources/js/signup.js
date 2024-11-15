document.addEventListener("DOMContentLoaded", function() {
	const form = document.querySelector("form");

	form.addEventListener("submit", function(event) {
		const password = document.getElementById("password").value;
		const email = document.getElementById("email").value;

		// 이메일 형식 확인 (간단한 정규 표현식)
		const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		if (!emailPattern.test(email)) {
			alert("유효한 이메일 주소를 입력해주세요.");
			event.preventDefault();
			return;
		}

		// 비밀번호 길이 확인
		if (password.length < 6) {
			alert("비밀번호는 최소 6자리 이상이어야 합니다.");
			event.preventDefault();
		}
	});
});
