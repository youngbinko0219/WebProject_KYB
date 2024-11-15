<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>프로필 수정</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/profile.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-5">
		<h2>프로필 수정</h2>
		<form action="${pageContext.request.contextPath}/user/profile"
			method="post">
			<div class="mb-3">
				<label for="name" class="form-label">이름</label> <input type="text"
					class="form-control" id="fullName" name="fullName"
					value="${user.fullName}" required>
			</div>
			<div class="mb-3">
				<label for="email" class="form-label">이메일</label> <input
					type="email" class="form-control" id="email" name="email"
					value="${user.email}" required>
			</div>
			<button type="submit" class="btn btn-primary">수정</button>
		</form>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>