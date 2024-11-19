<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:if test="${sessionScope.userId == null}">
	<script>
    alert("로그인이 필요합니다.");
    window.location.href = "${pageContext.request.contextPath}/views/user/login.jsp";
  </script>
</c:if>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>자료실 글쓰기</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-4">자료실 글쓰기</h2>
		<form action="${pageContext.request.contextPath}/databoard/write"
			method="post" enctype="multipart/form-data">
			<!-- 제목 입력 -->
			<div class="mb-3">
				<label for="title" class="form-label">제목</label> <input type="text"
					class="form-control" id="title" name="title" required>
			</div>
			<!-- 내용 입력 -->
			<div class="mb-3">
				<label for="content" class="form-label">내용</label>
				<textarea class="form-control" id="content" name="content" rows="5"
					required></textarea>
			</div>
			<!-- 파일 업로드 -->
			<div class="mb-3">
				<label for="files" class="form-label">파일 업로드 (선택)</label> <input
					type="file" class="form-control" id="files" name="files" multiple>
				<small class="form-text text-muted">여러 파일을 업로드하려면 파일을 선택할 때
					여러 개를 선택하세요.</small>
			</div>
			<!-- 작성 버튼 -->
			<div class="d-flex justify-content-end">
				<button type="submit" class="btn btn-primary me-2">작성</button>
				<a href="${pageContext.request.contextPath}/databoard/list?page=1"
					class="btn btn-secondary">취소</a>
			</div>
		</form>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
