package model;

import java.sql.Timestamp;

/**
 * MessageDTO는 MESSAGES 테이블의 데이터를 담는 데이터 전송 객체입니다.
 */
public class MessageDTO {
	private int messageId; // 메시지 고유 ID
	private int receiverId; // 받은 사용자 ID
	private String content; // 메시지 내용
	private Timestamp sentDate; // 보낸 날짜
	private boolean readStatus; // 읽음 여부 (true or false)

	// 기본 생성자
	public MessageDTO() {
	}

	// 전체 필드를 포함하는 생성자
	public MessageDTO(int messageId, int receiverId, String content, Timestamp sentDate, boolean readStatus) {
		this.messageId = messageId;
		this.receiverId = receiverId;
		this.content = content;
		this.sentDate = sentDate;
		this.readStatus = readStatus;
	}

	// messageId를 제외한 생성자 (자동 생성되는 경우를 위해)
	public MessageDTO(int receiverId, String content, Timestamp sentDate, boolean readStatus) {
		this.receiverId = receiverId;
		this.content = content;
		this.sentDate = sentDate;
		this.readStatus = readStatus;
	}

	// Getters and Setters
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getSentDate() {
		return sentDate;
	}

	public void setSentDate(Timestamp sentDate) {
		this.sentDate = sentDate;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	// toString 메서드 (디버깅 용도)
	@Override
	public String toString() {
		return "MessageDTO [messageId=" + messageId + ", receiverId=" + receiverId + ", content=" + content
				+ ", sentDate=" + sentDate + ", readStatus=" + readStatus + "]";
	}
}
