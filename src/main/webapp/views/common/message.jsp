<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>쪽지함</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
<style>
body {
	background-color: #f8f9fa;
}

.message-list {
	list-style: none;
	padding: 0;
}

.message-item {
	border-bottom: 1px solid #dee2e6;
	padding: 1rem;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.message-item.unread {
	background-color: #e9ecef;
}

.message-content p {
	margin: 0;
	font-weight: 500;
}

.message-content small {
	color: #6c757d;
}

.btn-read {
	font-size: 0.875rem;
}
</style>
<script>
function markAsRead(messageId) {
    fetch('<c:url value="/MarkAsReadController" />', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'messageId=' + messageId
    }).then(response => {
        if (response.ok) {
            // 읽음으로 표시 성공 시 UI 업데이트
            const messageItem = document.getElementById('message-' + messageId);
            if (messageItem) {
                messageItem.classList.remove('unread');
                messageItem.classList.add('read');

                // 읽음 버튼을 '이미 읽음'으로 변경
                const readButton = messageItem.querySelector('.btn-read');
                if (readButton) {
                    readButton.disabled = true;
                    readButton.textContent = '이미 읽음';  // 버튼 텍스트 변경
                }

                // 읽지 않은 메시지 수 업데이트
                const unreadCountElement = document.getElementById('unreadCount');
                let unreadCount = parseInt(unreadCountElement.textContent);
                if (!isNaN(unreadCount) && unreadCount > 0) {
                    unreadCountElement.textContent = unreadCount - 1;
                }
            }
        } else {
            console.error('Failed to mark as read');
        }
    }).catch(error => {
        console.error('Error:', error);
    });
}
</script>
</head>
<body>
	<jsp:include page="/views/common/header.jsp" />
	<header class="container py-3">
		<h1 class="h3">쪽지함</h1>
		<p class="text-muted">
			읽지 않은 쪽지: <span id="unreadCount" class="badge bg-danger">${unreadCount}</span>개
		</p>
	</header>

	<main class="container">
		<c:choose>
			<c:when test="${empty messages}">
				<div class="alert alert-info" role="alert">받은 쪽지가 없습니다.</div>
			</c:when>
			<c:otherwise>
				<ul class="message-list">
					<c:forEach var="message" items="${messages}">
						<li id="message-${message.messageId}"
							class="message-item ${message.readStatus ? 'read' : 'unread'}">
							<div class="message-content">
								<p>${message.content}</p>
								<small>보낸 날짜: ${message.sentDate}</small>
							</div>
							<button type="button" class="btn btn-primary btn-read"
								${message.readStatus ? 'disabled' : ''}
								onclick="markAsRead(${message.messageId})">${message.readStatus ? '이미 읽음' : '읽음'}</button>
						</li>
					</c:forEach>
				</ul>
			</c:otherwise>
		</c:choose>
	</main>

	<jsp:include page="/views/common/footer.jsp" />
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
