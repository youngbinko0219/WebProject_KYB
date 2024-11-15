<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="model.FreeBoardDTO"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%
// 세션에서 상세 게시글 객체 가져오기
FreeBoardDTO post = (FreeBoardDTO) request.getAttribute("post");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시글 상세보기</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>

	<div class="container my-4">
		<h2 class="mb-3">${post.title}</h2>
		<p class="text-muted">작성자: ${post.userId} | 작성일:
			${post.createdDate} | 조회수: ${post.viewCount} | 좋아요: ${post.likeCount}
		</p>
		<hr>
		<div class="content mb-5">
			<p>${post.content}</p>
		</div>
		<div class="d-flex justify-content-end">
			<c:if
				test="${sessionScope.userId != null && sessionScope.userId == post.userId}">
				<a
					href="${pageContext.request.contextPath}/freeboard/edit?id=${post.id}"
					class="btn btn-warning me-2">수정</a>
			</c:if>
			<c:if
				test="${sessionScope.userId != null && sessionScope.userId == post.userId}">
				<a href="#" id="deleteButton" class="btn btn-danger me-2">삭제</a>
			</c:if>
			<a href="${pageContext.request.contextPath}/freeboard/list?page=1"
				class="btn btn-secondary me-2">목록으로</a> <a
				href="${pageContext.request.contextPath}/freeboard/like?id=${post.id}"
				class="btn btn-primary">좋아요</a>
		</div>
	</div>
	<!-- 댓글 영역 -->
	<h4 class="mt-5">댓글</h4>

	<!-- 댓글 리스트 -->
	<ul class="list-group mb-4">
		<c:choose>
			<c:when test="${!empty comments}">
				<c:forEach var="comment" items="${comments}">
					<li class="list-group-item">
						<p>${comment.content}</p> <small class="text-muted">작성자:
							${comment.userId} | 작성일: ${comment.createdDate}</small> <c:if
							test="${sessionScope.userId != null && sessionScope.userId == comment.userId}">
							<div class="d-flex justify-content-end mt-2">
								<a
									href="${pageContext.request.contextPath}/comment/edit?id=${comment.id}"
									class="btn btn-warning btn-sm me-2">수정</a> <a
									href="${pageContext.request.contextPath}/comment/delete?id=${comment.id}"
									class="btn btn-danger btn-sm">삭제</a>
							</div>
						</c:if>
					</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li class="list-group-item text-center text-muted">등록된 댓글이
					없습니다.</li>
			</c:otherwise>
		</c:choose>
	</ul>

	<!-- 댓글 작성 폼 -->
	<c:if test="${sessionScope.userId != null}">
		<form action="${pageContext.request.contextPath}/comment/add"
			method="post" class="mb-4">
			<input type="hidden" name="postId" value="${post.id}">
			<div class="mb-3">
				<textarea name="content" class="form-control" rows="3"
					placeholder="댓글을 입력하세요..." required></textarea>
			</div>
			<button type="submit" class="btn btn-primary">댓글 작성</button>
		</form>
	</c:if>
	<c:if test="${sessionScope.userId == null}">
		<p class="text-muted">
			댓글을 작성하려면 <a
				href="${pageContext.request.contextPath}/views/user/login.jsp">로그인</a>하세요.
		</p>
	</c:if>


	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<!-- JavaScript -->
	<script>
    const deleteButton = document.getElementById("deleteButton");
    if (deleteButton) {
      deleteButton.addEventListener("click", function(event) {
        event.preventDefault(); // 기본 동작 방지
        const isConfirmed = confirm("정말 삭제하시겠습니까?");
        if (isConfirmed) {
          // 삭제 URL로 이동
          window.location.href = "${pageContext.request.contextPath}/freeboard/delete?id=${post.id}";
        }
      });
    }
  </script>
</body>
</html>
