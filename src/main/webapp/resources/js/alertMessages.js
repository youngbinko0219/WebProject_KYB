// alertMessages.js

// successMessage가 정의되어 있다면 성공 메시지 알림 표시
if (typeof successMessage !== 'undefined' && successMessage) {
    alert(successMessage);
}

// errorMessage가 정의되어 있다면 오류 메시지 알림 표시
if (typeof errorMessage !== 'undefined' && errorMessage) {
    alert(errorMessage);
}

if (typeof message !== 'undefined' && message) {
    alert(message);
}