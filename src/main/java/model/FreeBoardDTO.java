package model;

import java.util.Date;

public class FreeBoardDTO {
	private int id; // 게시글 ID
	private int userId; // 회원 번호
	private String username; // 회원 아이디 (추가)
	private String title; // 제목
	private String content; // 내용
	private Date createdDate; // 작성일
	private Date updatedDate; // 수정일
	private int viewCount; // 조회수
	private int likeCount; // 좋아요 수

	// 기본 생성자
	public FreeBoardDTO() {
	}

	// 생성자
	public FreeBoardDTO(int id, int userId, String username, String title, String content, Date createdDate,
			Date updatedDate, int viewCount, int likeCount) {
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.viewCount = viewCount;
		this.likeCount = likeCount;
	}

	// Getter 및 Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	} // 추가

	public void setUsername(String username) {
		this.username = username;
	} // 추가

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
}
