<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>자료실 수정</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-4">자료실 수정</h2>
		<form
			action="${pageContext.request.contextPath}/databoard/edit?id=${post.id}"
			method="post" enctype="multipart/form-data">
			<!-- 제목 -->
			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title" value="${post.title}"
					required>
			</div>
			<!-- 내용 -->
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="5"
					required>${post.content}</textarea>
			</div>
			<!-- 파일 업로드 -->
			<div class="mb-3">
				<label for="files" class="form-label">파일 업로드 (선택)</label> <input
					type="file" class="form-control" id="files" name="files" multiple>
				<small class="form-text text-muted">기존 파일을 대체하려면 새 파일을
					선택하세요.</small>
			</div>
			<!-- 수정 버튼 -->
			<div class="d-flex justify-content-end">
				<button type="submit" class="btn btn-primary me-2">수정</button>
				<a
					href="${pageContext.request.contextPath}/databoard/view?id=${post.id}"
					class="btn btn-secondary">취소</a>
			</div>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
