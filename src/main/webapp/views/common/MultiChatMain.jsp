<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%
// 세션에서 userId 가져오기
Integer userId = (Integer) session.getAttribute("userId");

if (userId == null) {
	response.sendRedirect(request.getContextPath() + "/views/user/login.jsp");
	return;
}

String username = user != null ? user.getUsername() : "UnknownUser"; // 예외 처리
%>
<html>
<head>
<title>웹소켓 채팅</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-8">
				<!-- Card for Chat Information -->
				<div class="card shadow-lg">
					<div class="card-header bg-primary text-white">
						<h2 class="mb-0">웹소켓 채팅</h2>
					</div>
					<div class="card-body">
						<p class="lead text-center">
							안녕하세요, <span class="fw-bold text-success"><%=username%></span>님!<br>
							아래 버튼을 클릭하여 채팅에 참여하세요.
						</p>
						<div class="d-flex justify-content-center">
							<button class="btn btn-primary btn-lg" onclick="chatWinOpen();">
								채팅 참여</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script>
        // 서버에서 전달된 username을 JavaScript 변수로 설정
        const username = "<%=username%>";

    function chatWinOpen() {
      if (!username || username === "UnknownUser") {
        alert("사용자 이름을 확인할 수 없습니다. 다시 로그인해주세요.");
        return;
      }
      // username을 사용하여 채팅창 열기
      window.open("ChatWindow.jsp?chatId=" + encodeURIComponent(username), "", "width=320,height=400");
    }
  </script>
</body>
</html>
