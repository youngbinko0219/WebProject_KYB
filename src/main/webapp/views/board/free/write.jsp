<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 작성</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>

	<div class="container my-4">
		<h2 class="mb-4">게시글 작성</h2>
		<form action="${pageContext.request.contextPath}/freeboard/write"
			method="post">
			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title" required>
			</div>
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="10"
					required></textarea>
			</div>
			<button type="submit" class="btn btn-primary">등록</button>
			<a href="${pageContext.request.contextPath}/freeboard/list?page=1"
				class="btn btn-secondary">취소</a>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
