package model;

import java.sql.Timestamp;

public class CommentDTO {
	private int commentId; // 댓글 ID
	private int boardId; // 게시글 ID
	private int userId; // 사용자 ID
	private String content; // 댓글 내용
	private Timestamp createdDate; // 생성 날짜
	private Timestamp updatedDate; // 수정 날짜
	private String username;

	// 기본 생성자
	public CommentDTO() {
	}

	// 모든 필드를 초기화하는 생성자
	public CommentDTO(int commentId, int boardId, int userId, String content, Timestamp createdDate,
			Timestamp updatedDate) {
		this.commentId = commentId;
		this.boardId = boardId;
		this.userId = userId;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	// Getter 및 Setter
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "CommentDTO{" + "commentId=" + commentId + ", boardId=" + boardId + ", userId=" + userId + ", content='"
				+ content + '\'' + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
	}
}
