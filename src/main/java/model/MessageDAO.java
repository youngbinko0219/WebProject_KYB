package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class MessageDAO {

	// 시스템 이벤트 발생 시 메세지 생성
	public void createNotificationMessage(MessageDTO message) {
		DBConnection db = new DBConnection();
		String sql = "INSERT INTO MESSAGES (RECEIVER_ID, CONTENT) VALUES (?, ?)";

		try {
			db.prepareStatement(sql);
			db.psmt.setInt(1, message.getReceiverId());
			db.psmt.setString(2, message.getContent());
			db.psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // 예외 처리
		} finally {
			db.close();
		}
	}

	// 사용자의 받은 메시지 조회
	public List<MessageDTO> getMessagesForUser(int userId) {
		List<MessageDTO> messages = new ArrayList<>();
		DBConnection db = new DBConnection();
		String sql = "SELECT * FROM MESSAGES WHERE RECEIVER_ID = ? ORDER BY SENT_DATE DESC";

		try {
			db.prepareStatement(sql);
			db.psmt.setInt(1, userId);
			db.rs = db.psmt.executeQuery();

			while (db.rs.next()) {
				MessageDTO message = new MessageDTO();
				message.setMessageId(db.rs.getInt("MESSAGE_ID"));
				message.setReceiverId(db.rs.getInt("RECEIVER_ID"));
				message.setContent(db.rs.getString("CONTENT"));
				message.setSentDate(db.rs.getTimestamp("SENT_DATE"));
				message.setReadStatus(db.rs.getString("READ_STATUS").equals("Y"));
				messages.add(message);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return messages;
	}

	// 읽지 않은 메시지 개수 조회
	public int getUnreadMessageCount(int userId) {
		int count = 0;
		DBConnection db = new DBConnection();
		String sql = "SELECT COUNT(*) AS COUNT FROM MESSAGES WHERE RECEIVER_ID = ? AND READ_STATUS = 'N'";

		try {
			db.prepareStatement(sql);
			db.psmt.setInt(1, userId);
			db.rs = db.psmt.executeQuery();

			if (db.rs.next()) {
				count = db.rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return count;
	}

	// 메시지 읽음 상태 업데이트
	public boolean markAsRead(int messageId) {
		boolean isUpdated = false;
		DBConnection db = new DBConnection();
		String sql = "UPDATE MESSAGES SET READ_STATUS = 'Y' WHERE MESSAGE_ID = ?";

		try {
			db.prepareStatement(sql);
			db.psmt.setInt(1, messageId);
			int rows = db.psmt.executeUpdate();
			if (rows > 0) {
				isUpdated = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return isUpdated;
	}

	// 특정 메시지 조회 (선택 사항)
	public MessageDTO getMessageById(int messageId) {
		MessageDTO message = null;
		DBConnection db = new DBConnection();
		String sql = "SELECT * FROM MESSAGES WHERE MESSAGE_ID = ?";

		try {
			db.prepareStatement(sql);
			db.psmt.setInt(1, messageId);
			db.rs = db.psmt.executeQuery();

			if (db.rs.next()) {
				message = new MessageDTO();
				message.setMessageId(db.rs.getInt("MESSAGE_ID"));
				message.setReceiverId(db.rs.getInt("RECEIVER_ID"));
				message.setContent(db.rs.getString("CONTENT"));
				message.setSentDate(db.rs.getTimestamp("SENT_DATE"));
				message.setReadStatus(db.rs.getString("READ_STATUS").equals("Y"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}

		return message;
	}
}
