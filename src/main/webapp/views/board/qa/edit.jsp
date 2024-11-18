<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 수정</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-4">게시글 수정</h2>

		<!-- 게시글 수정 폼 -->
		<form action="${pageContext.request.contextPath}/qaboard/edit"
			method="post">
			<!-- 게시글 ID (숨겨진 필드) -->
			<input type="hidden" name="id" value="${post.id}">

			<!-- 제목 입력 -->
			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title" value="${post.title}"
					required>
			</div>

			<!-- 내용 입력 -->
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="10"
					required>${post.content}</textarea>
			</div>

			<!-- 작성자 정보 -->
			<p class="text-muted">작성자: ${post.username}</p>

			<!-- 저장 버튼 -->
			<button type="submit" class="btn btn-primary">저장</button>

			<!-- 취소 버튼 -->
			<a
				href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}"
				class="btn btn-secondary">취소</a>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
