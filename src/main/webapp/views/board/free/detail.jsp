<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>자유게시판 상세보기</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
/* 댓글 스타일 */
.comment-content {
	border: none;
	background-color: transparent;
	resize: none;
	box-shadow: none;
	width: 100%;
}

.comment-content:focus {
	outline: none;
	border: 1px solid #ccc;
	background-color: #f8f9fa;
}

.comment-actions {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
}
</style>
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-3">${post.title}</h2>
		<p class="text-muted">
			작성자: ${post.username} | 작성일: ${post.createdDate} | 조회수:
			${post.viewCount} | 좋아요: <span id="likeCount">${post.likeCount}</span>
		</p>
		<hr>
		<div class="content mb-5">
			<p>${post.content}</p>
		</div>

		<!-- 좋아요 버튼 -->
		<c:if test="${sessionScope.userId != null}">
			<div class="d-flex justify-content-end mb-3">
				<form action="${pageContext.request.contextPath}/freeboard/like"
					method="post">
					<input type="hidden" name="id" value="${post.id}">
					<button type="submit"
						class="btn ${liked ? 'btn-danger' : 'btn-primary'}">
						<c:if test="${liked}">좋아요 취소</c:if>
						<c:if test="${!liked}">좋아요</c:if>
					</button>
				</form>
			</div>
		</c:if>

		<!-- 수정 및 삭제 버튼 -->
		<div class="d-flex justify-content-end">
			<c:if
				test="${sessionScope.userId != null && sessionScope.userId == post.userId}">
				<a
					href="${pageContext.request.contextPath}/freeboard/edit?id=${post.id}"
					class="btn btn-warning me-2">수정</a>
				<a href="#" id="deleteButton" class="btn btn-danger me-2">삭제</a>
			</c:if>
			<a href="${pageContext.request.contextPath}/freeboard/list?page=1"
				class="btn btn-secondary me-2">목록으로</a>
		</div>
	</div>

	<!-- 댓글 영역 -->
	<h4 class="mt-5">댓글</h4>
	<ul class="list-group mb-4">
		<c:choose>
			<c:when test="${!empty comments}">
				<c:forEach var="comment" items="${comments}">
					<li class="list-group-item">
						<form id="comment-form-${comment.commentId}" method="post"
							action="${pageContext.request.contextPath}/comment/updateFreeboard"
							class="d-flex flex-column gap-2">
							<!-- 댓글 내용 -->
							<textarea name="content" class="comment-content" rows="1"
								readonly>${comment.content}</textarea>
							<small class="text-muted">작성자: ${comment.username} | 작성일:
								${comment.createdDate}</small>

							<!-- 댓글 액션 버튼 -->
							<div class="comment-actions">
								<c:if
									test="${sessionScope.userId != null && sessionScope.userId == comment.userId}">
									<button type="button" class="btn btn-warning btn-sm edit-btn">수정</button>
									<button type="submit"
										class="btn btn-primary btn-sm save-btn d-none">저장</button>
									<a
										href="${pageContext.request.contextPath}/comment/deleteFreeboard?commentId=${comment.commentId}"
										class="btn btn-danger btn-sm">삭제</a>
								</c:if>
								<input type="hidden" name="commentId"
									value="${comment.commentId}">
							</div>
						</form>
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
		<form action="${pageContext.request.contextPath}/comment/addFreeboard"
			method="post" class="mb-4">
			<input type="hidden" name="boardId" value="${post.id}">
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
	<script>
        // 댓글 수정 모드 전환
        document.addEventListener("DOMContentLoaded", () => {
            const editButtons = document.querySelectorAll(".edit-btn");
            const saveButtons = document.querySelectorAll(".save-btn");

            editButtons.forEach((button) => {
                button.addEventListener("click", () => {
                    const form = button.closest("form");
                    const textarea = form.querySelector(".comment-content");
                    const saveButton = form.querySelector(".save-btn");

                    // 수정 모드 활성화
                    textarea.removeAttribute("readonly");
                    textarea.focus();
                    button.classList.add("d-none");
                    saveButton.classList.remove("d-none");
                });
            });

            saveButtons.forEach((button) => {
                button.addEventListener("click", (event) => {
                    const form = button.closest("form");
                    const textarea = form.querySelector(".comment-content");

                    // 저장 버튼 클릭 시 내용 확인
                    if (textarea.value.trim() === "") {
                        alert("댓글 내용을 입력해주세요.");
                        event.preventDefault(); // 폼 전송 방지
                    }
                });
            });
        });

        const deleteButton = document.getElementById("deleteButton");
        if (deleteButton) {
            deleteButton.addEventListener("click", function(event) {
                event.preventDefault(); // 기본 동작 방지
                const isConfirmed = confirm("정말 삭제하시겠습니까?");
                if (isConfirmed) {
                    window.location.href = "${pageContext.request.contextPath}/freeboard/delete?id=${post.id}";
                }
            });
        }
    </script>
</body>
</html>
