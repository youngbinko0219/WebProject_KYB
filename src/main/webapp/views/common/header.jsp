<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="model.UserDTO"%>

<%
// 세션에서 사용자 로그인 상태 확인
UserDTO user = (UserDTO) session.getAttribute("user");
boolean isLoggedIn = (user != null);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<!-- Meta tags for responsive behavior -->
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Navigation</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container">
			<a class="navbar-brand"
				href="${pageContext.request.contextPath}/views/board/dashboard/dashboard.jsp">목차</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/dashboard">대시보드</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/board/free/list.jsp">자유게시판</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/board/data/list.jsp">자료실</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/board/qa/list.jsp">질문
							게시판</a></li>
				</ul>
				<ul class="navbar-nav">
					<%
					if (isLoggedIn) {
					%>
					<!-- 프로필 수정 버튼 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/user/profile.jsp">프로필
							수정</a></li>
					<!-- 로그아웃 버튼 -->
					<li class="nav-item"><a class="nav-link text-danger"
						href="${pageContext.request.contextPath}/LogoutController">로그아웃</a></li>
					<%
					} else {
					%>
					<!-- 로그인 버튼 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/user/login.jsp">로그인</a></li>
					<%
					}
					%>
				</ul>
			</div>
		</div>
	</nav>

	<!-- Bootstrap 5 JavaScript with Popper for toggle functionality -->
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>
</body>
</html>
