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

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Bootstrap Icons -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.3/font/bootstrap-icons.css">

<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/header.css">

</head>
<body>
	<!-- 상단 바 -->
	<nav
		class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
		<div class="container">
			<!-- 로고 -->
			<a class="navbar-brand"
				href="${pageContext.request.contextPath}/dashboard">대시보드</a>

			<!-- 검색 바 -->
			<div class="search-bar d-none d-lg-block me-auto">
				<form action="${pageContext.request.contextPath}/search"
					method="get">
					<div class="input-group">
						<input type="text" class="form-control"
							placeholder="제목 검색어를 입력하세요" name="query">
						<button class="btn btn-outline-secondary" type="submit">
							<i class="bi bi-search"></i>
						</button>
					</div>
				</form>
			</div>

			<!-- 모바일 토글 버튼 -->
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<!-- 네비게이션 메뉴 -->
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/dashboard">홈</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/freeboard/list?page=1">자유게시판</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/databoard/list?page=1">자료실</a></li>
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/qaboard/list?page=1">질문게시판</a></li>
				</ul>

				<!-- 사용자 메뉴 -->
				<ul class="navbar-nav ms-auto">
					<%
					if (isLoggedIn) {
					%>
					<!-- 쪽지 아이콘 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/MessageController"> <i
							class="bi bi-envelope-fill"></i> 쪽지
					</a></li>
					<!-- 메시지 아이콘 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/common/MultiChatMain.jsp">
							<i class="bi bi-chat-fill"></i> 메시지
					</a></li>
					<!-- 프로필 이름 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/user/profile.jsp">
							<i class="bi bi-person-circle"></i> ${user.getUsername()}
					</a></li>
					<!-- 로그아웃 -->
					<li class="nav-item"><a class="nav-link text-danger"
						href="${pageContext.request.contextPath}/LogoutController"> <i
							class="bi bi-box-arrow-right"></i> 로그아웃
					</a></li>
					<%
					} else {
					%>
					<!-- 로그인 -->
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/views/user/login.jsp">
							<i class="bi bi-box-arrow-in-right"></i> 로그인
					</a></li>
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
