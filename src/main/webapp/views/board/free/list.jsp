<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>자유게시판 목록</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-5">
		<!-- 페이지 제목과 글쓰기 버튼 -->
		<div class="d-flex justify-content-between align-items-center mb-4">
			<h2>자유게시판</h2>
			<a href="${pageContext.request.contextPath}/freeboard/write"
				class="btn btn-primary">글쓰기</a>
		</div>

		<!-- 게시글 목록 테이블 -->
		<div class="card shadow-sm">
			<div class="card-body">
				<table class="table table-hover table-bordered">
					<thead class="table-light">
						<tr>
							<th scope="col" style="width: 10%;">번호</th>
							<th scope="col" style="width: 50%;">제목</th>
							<th scope="col" style="width: 15%;">작성자</th>
							<th scope="col" style="width: 15%;">작성일</th>
							<th scope="col" style="width: 10%;">조회수</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="post" items="${postList}">
							<tr>
								<th scope="row" class="text-center">${post.id}</th>
								<td><a
									href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}"
									class="text-decoration-none text-dark"> ${post.title} </a></td>
								<td class="text-center">${post.userName}</td>
								<td class="text-center">${post.createdDate}</td>
								<td class="text-center">${post.viewCount}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<!-- 페이지네이션 -->
		<nav aria-label="Page navigation" class="mt-4">
			<ul class="pagination justify-content-center">
				<!-- 이전 페이지 세트로 이동 -->
				<c:if test="${startPage > 1}">
					<li class="page-item"><a class="page-link"
						href="${pageContext.request.contextPath}/freeboard/list?page=${startPage - 1}"
						aria-label="Previous"> &laquo; </a></li>
				</c:if>

				<!-- 페이지 번호 출력 -->
				<c:forEach var="i" begin="${startPage}" end="${endPage}">
					<li class="page-item ${i == currentPage ? 'active' : ''}"><a
						class="page-link"
						href="${pageContext.request.contextPath}/freeboard/list?page=${i}">${i}</a>
					</li>
				</c:forEach>

				<!-- 다음 페이지 세트로 이동 -->
				<c:if test="${endPage < totalPages}">
					<li class="page-item"><a class="page-link"
						href="${pageContext.request.contextPath}/freeboard/list?page=${endPage + 1}"
						aria-label="Next"> &raquo; </a></li>
				</c:if>
			</ul>
		</nav>
	</div>

	<!-- Bootstrap 5 JavaScript with Popper for interactivity -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
