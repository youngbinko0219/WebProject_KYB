<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 삭제</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2>게시글 삭제</h2>
		<p>정말 이 게시글을 삭제하시겠습니까?</p>

		<form action="${pageContext.request.contextPath}/freeboard/delete"
			method="post">
			<input type="hidden" name="id" value="${param.id}">
			<div class="d-flex justify-content-between">
				<button type="submit" class="btn btn-danger">삭제</button>
				<a
					href="${pageContext.request.contextPath}/freeboard/view?id=${param.id}"
					class="btn btn-secondary">취소</a>
			</div>
		</form>
	</div>
</body>
</html>
