package model;

import java.util.Date;

public class FreeBoardDTO {
	private int id;
	private int userId;
	private String title;
	private String content;
	private Date createdDate;
	private Date updatedDate;
	private int viewCount;
	private int likeCount; // 좋아요 수 필드 추가

	// 기본 생성자
	public FreeBoardDTO() {
	}

	// 매개변수 있는 생성자
	public FreeBoardDTO(int id, int userId, String title, String content, Date createdDate, Date updatedDate,
			int viewCount, int likeCount) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.viewCount = viewCount;
		this.likeCount = likeCount; // likeCount 초기화
	}

	// Getter와 Setter 메서드
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
	} // 좋아요 수 setter
}
