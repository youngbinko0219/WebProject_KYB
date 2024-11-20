<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="/views/common/header.jsp"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/results.css">

<div class="container mt-5">
	<h2>"${query}" 검색 결과</h2>

	<c:if test="${not empty freeboardResults}">
		<h3 class="mt-4">자유게시판</h3>
		<ul class="list-group">
			<c:forEach var="post" items="${freeboardResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
	</c:if>

	<c:if test="${not empty qaBoardResults}">
		<h3 class="mt-4">질문게시판</h3>
		<ul class="list-group">
			<c:forEach var="post" items="${qaBoardResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
	</c:if>

	<c:if test="${not empty dataBoardResults}">
		<h3 class="mt-4">자료실</h3>
		<ul class="list-group">
			<c:forEach var="post" items="${dataBoardResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/databoard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
	</c:if>

	<c:if
		test="${empty freeboardResults && empty qaBoardResults && empty dataBoardResults}">
		<p class="mt-4">검색 결과가 없습니다.</p>
	</c:if>
</div>

<%@ include file="/views/common/footer.jsp"%>
