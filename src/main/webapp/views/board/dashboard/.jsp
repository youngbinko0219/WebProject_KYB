<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
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
</head>
<body>

	<!-- Include the common header with the navigation bar -->
	<jsp:include page="/views/common/header.jsp" />

	<!-- Dashboard Header -->
	<header class="bg-light p-3 mb-4">
		<h2 class="text-center">대시보드</h2>
	</header>

	<div class="container my-4">
		<div class="row">
			<!-- 자유게시판 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div class="card-header bg-danger text-white">자유게시판</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestFreePosts}">
								<c:forEach var="post" items="${latestFreePosts}">
									<li class="list-group-item"><a
										href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}"
										class="text-decoration-none text-dark"> ${post.title} </a></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
						<li class="list-group-item text-end"><a
							href="${pageContext.request.contextPath}/freeboard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a></li>
					</ul>
				</div>
			</div>

			<!-- 자료실 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div class="card-header bg-primary text-white">자료실</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestDataPosts}">
								<c:forEach var="post" items="${latestDataPosts}">
									<li class="list-group-item"><a
										href="${pageContext.request.contextPath}/databoard/view?id=${post.id}"
										class="text-decoration-none text-dark"> ${post.title} </a></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
						<li class="list-group-item text-end"><a
							href="${pageContext.request.contextPath}/databoard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a></li>
					</ul>
				</div>
			</div>

			<!-- 질문 게시판 -->
			<div class="col-12 mb-4">
				<div class="card w-100">
					<div class="card-header bg-success text-white">질문 게시판</div>
					<ul class="list-group list-group-flush">
						<c:choose>
							<c:when test="${!empty latestQAPosts}">
								<c:forEach var="post" items="${latestQAPosts}">
									<li class="list-group-item"><a
										href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}"
										class="text-decoration-none text-dark"> ${post.title} </a></li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li class="list-group-item text-center text-muted">등록된 게시글이
									없습니다.</li>
							</c:otherwise>
						</c:choose>
						<li class="list-group-item text-end"><a
							href="${pageContext.request.contextPath}/qaboard/list?page=1"
							class="text-decoration-none text-primary">더보기 &gt;</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
