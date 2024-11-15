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
		<!-- 수정 폼 -->
		<form action="${pageContext.request.contextPath}/freeboard/edit"
			method="post">
			<input type="hidden" name="id" value="${post.id}">

			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title" value="${post.title}"
					required>
			</div>

			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="10"
					required>${post.content}</textarea>
			</div>

			<div class="d-flex justify-content-end">
				<a
					href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}"
					class="btn btn-secondary me-2">취소</a>
				<button type="submit" class="btn btn-primary">저장</button>
			</div>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
