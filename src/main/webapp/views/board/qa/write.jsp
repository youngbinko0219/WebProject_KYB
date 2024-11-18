<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>질문 게시판 글쓰기</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-4">질문 게시판 글쓰기</h2>
		<form action="${pageContext.request.contextPath}/qaboard/write"
			method="post">
			<!-- 제목 입력 -->
			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title"
					placeholder="제목을 입력하세요" required>
			</div>

			<!-- 내용 입력 -->
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="10"
					placeholder="내용을 입력하세요" required></textarea>
			</div>

			<!-- 작성 버튼 -->
			<div class="d-flex justify-content-end">
				<button type="submit" class="btn btn-primary me-2">작성하기</button>
				<a href="${pageContext.request.contextPath}/qaboard/list?page=1"
					class="btn btn-secondary">취소</a>
			</div>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
