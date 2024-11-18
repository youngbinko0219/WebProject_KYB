<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>질문 게시판</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-4">질문 게시판</h2>

		<!-- 게시글 리스트 -->
		<table class="table table-hover">
			<thead class="table-light">
				<tr align="center">
					<th width="10%">번호</th>
					<th width="*">제목</th>
					<th width="15%">작성자</th>
					<th width="10%">조회수</th>
					<th width="15%">작성일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 게시물이 없을 때 -->
				<c:choose>
					<c:when test="${empty postList}">
						<tr>
							<td colspan="5" align="center">등록된 게시물이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${postList}" var="post">
							<tr align="center">
								<!-- 데이터베이스의 id 값을 번호로 표시 -->
								<td>${post.id}</td>
								<td align="left"><a
									href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}"
									class="text-decoration-none text-dark"> ${post.title} </a></td>
								<td>${post.username}</td>
								<td>${post.viewCount}</td>
								<td>${post.createdDate}</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

		<!-- 페이지네이션 -->
		<nav>
			<ul class="pagination justify-content-center">
				<c:forEach begin="1" end="${totalPages}" var="page">
					<li class="page-item ${currentPage == page ? 'active' : ''}">
						<a class="page-link"
						href="${pageContext.request.contextPath}/qaboard/list?page=${page}">
							${page} </a>
					</li>
				</c:forEach>
			</ul>
		</nav>

		<!-- 글쓰기 버튼 -->
		<div class="d-flex justify-content-end mt-3">
			<c:if test="${sessionScope.userId != null}">
				<button type="button" class="btn btn-primary"
					onclick="location.href='${pageContext.request.contextPath}/qaboard/write'">글쓰기</button>
			</c:if>
		</div>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
