<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/views/common/header.jsp"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>자료실 상세보기</title>
<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container my-4">
		<h2 class="mb-3">${post.title}</h2>
		<p class="text-muted">작성자: ${post.username} | 작성일:
			${post.createdDate} | 조회수: ${post.viewCount}</p>
		<hr>
		<div class="content mb-5">
			<p>${post.content}</p>
		</div>

		<!-- 파일 미리보기 -->
		<c:if test="${post.storedFilename != null}">
			<div class="mb-3">
				<h5>파일 미리보기</h5>
				<ul>
					<c:forEach var="fileIndex" begin="0"
						end="${fn:length(post.storedFilename.split(',')) - 1}">
						<c:set var="originalFile"
							value="${post.originalFilename.split(',')[fileIndex].trim()}" />
						<c:set var="storedFile"
							value="${post.storedFilename.split(',')[fileIndex].trim()}" />

						<c:choose>
							<c:when
								test="${storedFile.endsWith('.png') || storedFile.endsWith('.jpg') || storedFile.endsWith('.jpeg') || storedFile.endsWith('.gif')}">
								<img
									src="${pageContext.request.contextPath}/resources/upload/${storedFile}"
									alt="${originalFile}" class="img-fluid mb-3">
							</c:when>

							<c:when
								test="${storedFile.endsWith('.mp4') || storedFile.endsWith('.avi')}">
								<video controls class="w-100 mb-3">
									<source
										src="${pageContext.request.contextPath}/resources/upload/${storedFile}"
										type="video/mp4">
									지원하지 않는 동영상 형식입니다.
								</video>
							</c:when>

							<c:when
								test="${storedFile.endsWith('.mp3') || storedFile.endsWith('.wav')}">
								<audio controls class="w-100 mb-3">
									<source
										src="${pageContext.request.contextPath}/resources/upload/${storedFile}"
										type="audio/mpeg">
									지원하지 않는 음원 형식입니다.
								</audio>
							</c:when>

							<c:otherwise>
								<a
									href="${pageContext.request.contextPath}/resources/upload/${storedFile}"
									class="btn btn-secondary">${originalFile} 보기</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<div class="d-flex justify-content-end mb-4">
			<c:if
				test="${sessionScope.userId != null && sessionScope.userId == post.userId}">
				<a
					href="${pageContext.request.contextPath}/databoard/edit?id=${post.id}"
					class="btn btn-warning me-2">수정</a>
				<a href="#" id="deleteButton" class="btn btn-danger me-2">삭제</a>
			</c:if>
			<a href="${pageContext.request.contextPath}/databoard/list?page=1"
				class="btn btn-secondary">목록으로</a>
		</div>

		<c:if test="${post.storedFilename != null}">
			<div class="d-flex justify-content-end">
				<c:forEach var="fileIndex" begin="0"
					end="${fn:length(post.storedFilename.split(',')) - 1}">
					<c:set var="originalFile"
						value="${post.originalFilename.split(',')[fileIndex].trim()}" />
					<c:set var="storedFile"
						value="${post.storedFilename.split(',')[fileIndex].trim()}" />
					<a
						href="${pageContext.request.contextPath}/databoard/download?id=${post.id}&storedFile=${storedFile}"
						class="btn btn-info mb-2 ms-2">${originalFile} 다운로드</a>
				</c:forEach>
			</div>
		</c:if>
	</div>

	<!-- Bootstrap 5 JavaScript -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script>
    const deleteButton = document.getElementById("deleteButton");
    if (deleteButton) {
      deleteButton.addEventListener("click", function(event) {
        event.preventDefault(); // 기본 동작 방지
        const isConfirmed = confirm("정말 삭제하시겠습니까?");
        if (isConfirmed) {
          window.location.href = "${pageContext.request.contextPath}/databoard/delete?id=${post.id}";
        }
      });
    }
  </script>
</body>
</html>
