<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입 페이지</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<!-- 기존의 그라데이션 CSS 파일을 포함 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/signup.css">
</head>
<body>
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<div class="card">
					<div class="card-header text-center">
						<h2>회원가입</h2>
					</div>
					<div class="card-body">
						<form action="${pageContext.request.contextPath}/UserController"
							method="post" id="signupForm">
							<div class="mb-3">
								<label for="fullName" class="form-label">이름</label> <input
									type="text" class="form-control" id="fullName" name="fullName"
									placeholder="이름을 입력하세요" required>
							</div>
							<div class="mb-3">
								<label for="username" class="form-label">아이디</label> <input
									type="text" class="form-control" id="username" name="username"
									placeholder="아이디를 입력하세요" required>
							</div>
							<div class="mb-3">
								<label for="password" class="form-label">비밀번호</label> <input
									type="password" class="form-control" id="password"
									name="password" placeholder="비밀번호를 입력하세요" required>
							</div>
							<div class="mb-3">
								<label for="email" class="form-label">이메일</label> <input
									type="email" class="form-control" id="email" name="email"
									placeholder="이메일 주소를 입력하세요" required>
							</div>
							<div class="mb-3">
								<label for="phoneNumber" class="form-label">전화번호</label> <input
									type="text" class="form-control" id="phoneNumber"
									name="phoneNumber" placeholder="전화번호를 입력하세요" required>
							</div>
							<div class="d-grid">
								<button type="submit" class="btn btn-primary">회원가입</button>
							</div>
						</form>
					</div>
					<div class="card-footer text-center">
						<a href="${pageContext.request.contextPath}/views/user/login.jsp">이미
							계정이 있으신가요? 로그인</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 메시지가 존재할 때만 alert 표시 -->
	<c:if test="${not empty errorMessage}">
		<script>
      let errorMessage = "${fn:escapeXml(errorMessage)}";
      if (errorMessage) {
        alert(errorMessage);
      }
    </script>
	</c:if>
	<c:if test="${not empty successMessage}">
		<script>
      let successMessage = "${fn:escapeXml(successMessage)}";
      if (successMessage) {
        alert(successMessage);
      }
    </script>
	</c:if>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Custom JavaScript -->
	<script src="${pageContext.request.contextPath}/resources/js/signup.js"></script>
</body>
</html>
