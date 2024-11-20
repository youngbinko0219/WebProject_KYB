<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="/views/common/header.jsp"%>

<div class="container mt-5">
	<h2>"${query}" 검색 결과</h2>

	<!-- 제목 검색 결과 -->
	<h3 class="mt-4">제목 검색 결과</h3>

	<!-- 자유게시판 제목 검색 결과 -->
	<c:if test="${not empty freeboardTitleResults}">
		<h4 class="mt-3">자유게시판</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${freeboardTitleResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${freeboardTitlePage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&freeboardTitlePage=${freeboardTitlePage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${freeboardTitleTotalPages}" var="i">
					<li class="page-item ${i == freeboardTitlePage ? 'active' : ''}">
						<a class="page-link"
						href="?query=${query}&freeboardTitlePage=${i}"> ${i} </a>
					</li>
				</c:forEach>
				<c:if test="${freeboardTitlePage < freeboardTitleTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&freeboardTitlePage=${freeboardTitlePage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 질문게시판 제목 검색 결과 -->
	<c:if test="${not empty qaBoardTitleResults}">
		<h4 class="mt-3">질문게시판</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${qaBoardTitleResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${qaBoardTitlePage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&qaBoardTitlePage=${qaBoardTitlePage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${qaBoardTitleTotalPages}" var="i">
					<li class="page-item ${i == qaBoardTitlePage ? 'active' : ''}">
						<a class="page-link" href="?query=${query}&qaBoardTitlePage=${i}">
							${i} </a>
					</li>
				</c:forEach>
				<c:if test="${qaBoardTitlePage < qaBoardTitleTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&qaBoardTitlePage=${qaBoardTitlePage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 자료실 제목 검색 결과 -->
	<c:if test="${not empty dataBoardTitleResults}">
		<h4 class="mt-3">자료실</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${dataBoardTitleResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/databoard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${dataBoardTitlePage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&dataBoardTitlePage=${dataBoardTitlePage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${dataBoardTitleTotalPages}" var="i">
					<li class="page-item ${i == dataBoardTitlePage ? 'active' : ''}">
						<a class="page-link"
						href="?query=${query}&dataBoardTitlePage=${i}"> ${i} </a>
					</li>
				</c:forEach>
				<c:if test="${dataBoardTitlePage < dataBoardTitleTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&dataBoardTitlePage=${dataBoardTitlePage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 내용 검색 결과 -->
	<h3 class="mt-4">내용 검색 결과</h3>

	<!-- 자유게시판 내용 검색 결과 -->
	<c:if test="${not empty freeboardContentResults}">
		<h4 class="mt-3">자유게시판</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${freeboardContentResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/freeboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${freeboardContentPage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&freeboardContentPage=${freeboardContentPage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${freeboardContentTotalPages}" var="i">
					<li class="page-item ${i == freeboardContentPage ? 'active' : ''}">
						<a class="page-link"
						href="?query=${query}&freeboardContentPage=${i}"> ${i} </a>
					</li>
				</c:forEach>
				<c:if test="${freeboardContentPage < freeboardContentTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&freeboardContentPage=${freeboardContentPage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 질문게시판 내용 검색 결과 -->
	<c:if test="${not empty qaBoardContentResults}">
		<h4 class="mt-3">질문게시판</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${qaBoardContentResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/qaboard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${qaBoardContentPage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&qaBoardContentPage=${qaBoardContentPage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${qaBoardContentTotalPages}" var="i">
					<li class="page-item ${i == qaBoardContentPage ? 'active' : ''}">
						<a class="page-link"
						href="?query=${query}&qaBoardContentPage=${i}"> ${i} </a>
					</li>
				</c:forEach>
				<c:if test="${qaBoardContentPage < qaBoardContentTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&qaBoardContentPage=${qaBoardContentPage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 자료실 내용 검색 결과 -->
	<c:if test="${not empty dataBoardContentResults}">
		<h4 class="mt-3">자료실</h4>
		<ul class="list-group">
			<c:forEach var="post" items="${dataBoardContentResults}">
				<li class="list-group-item"><a
					href="${pageContext.request.contextPath}/databoard/view?id=${post.id}">
						${post.title} </a> <small class="text-muted">-
						${post.createdDate}</small></li>
			</c:forEach>
		</ul>
		<!-- 페이징 네비게이션 -->
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<c:if test="${dataBoardContentPage > 1}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&dataBoardContentPage=${dataBoardContentPage - 1}"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach begin="1" end="${dataBoardContentTotalPages}" var="i">
					<li class="page-item ${i == dataBoardContentPage ? 'active' : ''}">
						<a class="page-link"
						href="?query=${query}&dataBoardContentPage=${i}"> ${i} </a>
					</li>
				</c:forEach>
				<c:if test="${dataBoardContentPage < dataBoardContentTotalPages}">
					<li class="page-item"><a class="page-link"
						href="?query=${query}&dataBoardContentPage=${dataBoardContentPage + 1}"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
		</nav>
	</c:if>

	<!-- 검색 결과가 없을 경우 -->
	<c:if
		test="${empty freeboardTitleResults && empty qaBoardTitleResults && empty dataBoardTitleResults
                 && empty freeboardContentResults && empty qaBoardContentResults && empty dataBoardContentResults}">
		<p class="mt-4">검색 결과가 없습니다.</p>
	</c:if>
</div>

<%@ include file="/views/common/footer.jsp"%>
