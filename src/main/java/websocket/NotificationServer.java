package websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/NotificationServer/{userId}")
public class NotificationServer {
	private static Map<String, Session> userSessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("userId") String userId) {
		userSessions.put(userId, session);
		System.out.println("웹소켓 연결: " + session.getId() + " 사용자: " + userId);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
	}

	@OnClose
	public void onClose(Session session, @PathParam("userId") String userId) {
		userSessions.remove(userId);
		System.out.println("웹소켓 종료: " + session.getId() + " 사용자: " + userId);
	}

	@OnError
	public void onError(Session session, Throwable e) {
		System.out.println("에러 발생: " + session.getId());
		e.printStackTrace();
	}

	// 특정 사용자에게 메시지 전송 메서드
	public static void sendNotification(String userId, String message) {
		Session session = userSessions.get(userId);
		if (session != null && session.isOpen()) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
