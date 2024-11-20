<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="/views/common/header.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>대시보드</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/dashboard.css">
</head>
<body>

	<div class="container my-4">
		<!-- Dashboard Header -->
		<header class="bg-light p-3 mb-4">
			<h2 class="text-start">대시보드</h2>
		</header>

		<!-- 수정 완료 알림 표시 -->
		<c:if test="${sessionScope.updateSuccess}">
			<script>
        alert('수정 완료되었습니다.');
      </script>
			<c:remove var="updateSuccess" scope="session" />
		</c:if>
		<div class="row">

			<!-- 로그인 성공 알림 표시 -->
			<c:if test="${sessionScope.loginSuccess}">
				<script>
          alert('${sessionScope.username}님 환영합니다.');
        </script>
				<!-- 세션에서 속성 제거 -->
				<c:remove var="loginSuccess" scope="session" />
				<c:remove var="username" scope="session" />
			</c:if>

			<!-- 자유게시판 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div
						class="card-header card-header-freeboard d-flex justify-content-between align-items-center">
						<span>자유게시판</span> <a
							href="${pageContext.request.contextPath}/freeboard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a>
					</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestFreePosts}">
								<c:forEach var="post" items="${latestFreePosts}">
									<li class="list-group-item d-flex align-items-center"><span
										class="title flex-grow-1"><a
											href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}"
											class="text-decoration-none text-dark">${post.title}</a></span> <span
										class="author fixed-width">${post.username}</span> <span
										class="date fixed-width">${post.createdDate}</span> <span
										class="views fixed-width">${post.viewCount}</span></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>

			<!-- 자료실 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div
						class="card-header card-header-databoard d-flex justify-content-between align-items-center">
						<span>자료실</span> <a
							href="${pageContext.request.contextPath}/databoard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a>
					</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestDataPosts}">
								<c:forEach var="post" items="${latestDataPosts}">
									<li class="list-group-item d-flex align-items-center"><span
										class="title flex-grow-1"><a
											href="${pageContext.request.contextPath}/databoard/view?id=${post.id}"
											class="text-decoration-none text-dark">${post.title}</a></span> <span
										class="author fixed-width">${post.username}</span> <span
										class="date fixed-width">${post.createdDate}</span> <span
										class="views fixed-width">${post.viewCount}</span></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>

			<!-- 질문 게시판 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div
						class="card-header card-header-qaboard d-flex justify-content-between align-items-center">
						<span>질문 게시판</span> <a
							href="${pageContext.request.contextPath}/qaboard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a>
					</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestQAPosts}">
								<c:forEach var="post" items="${latestQAPosts}">
									<li class="list-group-item d-flex align-items-center"><span
										class="title flex-grow-1"><a
											href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}"
											class="text-decoration-none text-dark">${post.title}</a></span> <span
										class="author fixed-width">${post.username}</span> <span
										class="date fixed-width">${post.createdDate}</span> <span
										class="views fixed-width">${post.viewCount}</span></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<%@ include file="/views/common/footer.jsp"%>
</body>
</html>
