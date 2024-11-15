package model;

import java.sql.Timestamp;

public class CommentDTO {
	private int commentId;
	private String boardType;
	private int boardId;
	private int userId;
	private String content;
	private Timestamp createdDate;
	private Timestamp updatedDate;

	// 기본 생성자
	public CommentDTO() {
	}

	// 모든 필드를 초기화하는 생성자
	public CommentDTO(int commentId, String boardType, int boardId, int userId, String content, Timestamp createdDate,
			Timestamp updatedDate) {
		this.commentId = commentId;
		this.boardType = boardType;
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

	public String getBoardType() {
		return boardType;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
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
}
