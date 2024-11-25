<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>비밀번호 재설정</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- 외부 CSS 파일 -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/reset_password.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm">
                    <div class="card-header text-center">
                        <h2>비밀번호 재설정</h2>
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/ResetPasswordController" method="post" id="resetForm">
                            <!-- 숨겨진 필드로 username 전송 -->
                            <input type="hidden" name="username" value="${username}">
                            <div class="mb-3">
                                <label for="newPassword" class="form-label">새 비밀번호</label>
                                <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="새 비밀번호를 입력하세요" required>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">비밀번호 확인</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="비밀번호를 다시 입력하세요" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">비밀번호 재설정</button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center">
                        <a href="${pageContext.request.contextPath}/views/user/login.jsp" class="custom-link">로그인 페이지로 돌아가기</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 서버에서 전달된 message 변수를 JavaScript 변수로 설정 -->
    <c:if test="${not empty message}">
        <script>var message = "${message}";</script>
    </c:if>

    <!-- Bootstrap 5 JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- 외부 JavaScript 파일 불러오기 -->
    <script src="${pageContext.request.contextPath}/resources/js/alertMessages.js"></script>
</body>
</html>
