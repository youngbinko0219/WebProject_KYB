<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인 페이지</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 외부 CSS 파일 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/login.css">
</head>
<body>
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<div class="card shadow-sm">
					<div class="card-header text-center">
						<h2>로그인</h2>
					</div>
					<div class="card-body p-4">
						<form action="${pageContext.request.contextPath}/LoginController"
							method="post">
							<div class="mb-3">
								<label for="username" class="form-label">아이디</label> <input
									type="text" class="form-control" id="username" name="username"
									required>
							</div>
							<div class="mb-3">
								<label for="password" class="form-label">비밀번호</label> <input
									type="password" class="form-control" id="password"
									name="password" required>
							</div>
							<div class="mb-3 form-check">
								<input type="checkbox" class="form-check-input" id="rememberMe"
									name="rememberMe"> <label class="form-check-label"
									for="rememberMe">자동 로그인</label>
							</div>
							<button type="submit" class="btn btn-login w-100">로그인</button>
						</form>
						<div class="text-center mt-3">
							<a
								href="${pageContext.request.contextPath}/views/user/signup.jsp"
								class="custom-link">회원가입</a> | <a
								href="${pageContext.request.contextPath}/views/user/forgot_password.jsp"
								class="custom-link">비밀번호 찾기</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
