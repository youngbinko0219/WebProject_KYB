<!-- footer.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/footer.css">

<!-- Bootstrap 5 CSS 포함 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Font Awesome 포함 -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
	integrity="sha512-Fo3rlrZj/kMVqjfvJyOaX63fTwY6rD3gqN0O8kIGeMVgA3VzSYlG/XF4wbG0vDgqMzxbo6Rw+Th6oVZiqvT1cw=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />

<!-- 커스텀 CSS -->
<style>
/* 네이버 스타일의 푸터를 구현하기 위한 스타일 */
.footer {
	background-color: #f5f5f5; /* 네이버 푸터 배경색 */
	color: #333; /* 기본 텍스트 색상 */
	padding: 20px 0;
	border-top: 1px solid #e5e5e5;
	position: relative;
	bottom: 0;
	width: 100%;
}

.footer a {
	color: #333;
	text-decoration: none;
}

.footer a:hover {
	text-decoration: underline;
}

.footer .container {
	max-width: 1080px;
}

.footer .footer-links {
	margin-bottom: 20px;
}

.footer .footer-links li {
	display: inline-block;
	margin-right: 15px;
}

.footer .footer-info {
	font-size: 14px;
	line-height: 1.6;
}

.footer .social-icons i {
	font-size: 18px;
	margin-right: 10px;
	color: #555;
}

.footer .social-icons i:hover {
	color: #2DB400; /* 네이버 그린 */
}
</style>

<div class="footer"
	style="position: fixed; bottom: 0; width: 100%; z-index: 1000;">
	<div class="container">
		<!-- 연락처 정보 -->
		<div class="row footer-info" style="transform: translateX(-130px);">
			<div class="col-md-6">
				<p>
					<i class="fas fa-envelope me-2"></i>이메일: support@example.com
				</p>
				<p>
					<i class="fas fa-phone me-2"></i>전화: 1234-5678
				</p>
				<p>
					<i class="fas fa-map-marker-alt me-2"></i>주소: 서울특별시 강남구 테헤란로 123
				</p>
			</div>
		</div>
		<!-- 저작권 표시 -->
		<div class="row mt-3">
			<div class="col-md-12 text-center">
				<p>© 2023 YourCompanyName. All rights reserved.</p>
			</div>
		</div>
	</div>
</div>
