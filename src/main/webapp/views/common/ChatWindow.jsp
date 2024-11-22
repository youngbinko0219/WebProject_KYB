<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>
<title>웹소켓 채팅</title>
<script>
    var webSocket = new WebSocket("<%=application.getInitParameter("CHAT_ADDR")%>/ChatingServer");
    var chatWindow, chatMessage, chatId;

    window.onload = function() {
        chatWindow = document.getElementById("chatWindow");
        chatMessage = document.getElementById("chatMessage");

        // URL 파라미터에서 chatId(username) 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        chatId = urlParams.get('chatId');

        if (!chatId) {
            alert("사용자 이름이 올바르지 않습니다. 다시 로그인해주세요.");
            window.close(); // 잘못된 접근이면 창 닫기
        }

        // 브라우저 알림 권한 요청
        if (Notification.permission !== "granted") {
            Notification.requestPermission();
        }
    };

    // 메시지 전송
    function sendMessage() {
        var message = chatMessage.value.trim();
        if (message === "") return;

        // 대화창에 메시지 표시
        chatWindow.innerHTML += "<div class='myMsg'>" + message + "</div>";
        webSocket.send(chatId + "|" + message); // WebSocket 서버로 전송
        chatMessage.value = ""; // 입력창 초기화
        chatWindow.scrollTop = chatWindow.scrollHeight; // 스크롤 자동 이동
    }

    // WebSocket 메시지 수신
    webSocket.onmessage = function(event) {
        var data = event.data.split("|"); // 대화명과 메시지 분리
        var sender = data[0]; // 보낸 사람의 대화명
        var content = data[1]; // 메시지 내용

        if (content) {
            if (content.startsWith("/")) { // 귓속말 처리
                if (content.includes("/" + chatId)) { // 나에게 보낸 귓속말인지 확인
                    var whisperMessage = content.replace("/" + chatId, "[귓속말]: ");
                    chatWindow.innerHTML += "<div class='whisper'>" + sender + " " + whisperMessage + "</div>";
                    showNotification(sender, whisperMessage); // 웹 노티 표시
                }
            } else { // 일반 메시지
                chatWindow.innerHTML += "<div>" + sender + " : " + content + "</div>";
            }
        }
        chatWindow.scrollTop = chatWindow.scrollHeight;
    };

    // 웹 노티피케이션 함수
    function showNotification(sender, message) {
        if (Notification.permission === "granted") {
            var notification = new Notification("새 귓속말이 도착했습니다!", {
                body: sender + "님: " + message, // 노티 내용
                icon: "http://cfile201.uf.daum.net/image/235BFD3F5937AC17164572" // 원하는 아이콘 URL
            });

            notification.onclick = function() {
                window.focus(); // 노티 클릭 시 창 활성화
            };
        } else {
            console.warn("웹 노티피케이션 권한이 허용되지 않았습니다.");
        }
    }

    // 연결 종료
    function disconnect() {
        webSocket.close();
        chatWindow.innerHTML += "채팅이 종료되었습니다.<br/>";
    }

    // 엔터키 입력 처리
    function enterKey() {
        if (window.event.keyCode === 13) { // Enter 키 입력
            sendMessage();
        }
    }
</script>

<style>
    #chatWindow {
        border: 1px solid black;
        width: 270px;
        height: 310px;
        overflow-y: scroll;
        padding: 5px;
    }

    #chatMessage {
        width: 236px;
        height: 30px;
    }

    #sendBtn {
        height: 30px;
        position: relative;
        top: 2px;
        left: -2px;
    }

    .myMsg {
        text-align: right;
    }

    .whisper {
        color: gray;
        font-style: italic;
    }
</style>
</head>
<body>
    <!-- 대화창 UI -->
    대화명: <input type="text" id="chatId" value="<%=request.getParameter("chatId") %>" readonly />
    <button onclick="disconnect()">채팅 종료</button>
    <div id="chatWindow"></div>
    <div>
        <input type="text" id="chatMessage" onkeyup="enterKey();" placeholder="메시지 입력">
        <button id="sendBtn" onclick="sendMessage()">전송</button>
    </div>
</body>
</html>
