package model;

import java.util.Date;

public class DataBoardDTO {
	private int id; // 게시글 ID
	private int userId; // 작성자 ID
	private String username; // 작성자 이름 (추가)
	private String title; // 제목
	private String content; // 내용
	private String originalFilename; // 업로드된 파일의 원본 이름
	private String storedFilename; // 서버에 저장된 파일 이름
	private Date createdDate; // 작성일
	private Date updatedDate; // 수정일
	private int viewCount; // 조회수

	// 기본 생성자
	public DataBoardDTO() {
	}

	// 생성자
	public DataBoardDTO(int id, int userId, String username, String title, String content, String originalFilename,
			String storedFilename, Date createdDate, Date updatedDate, int viewCount) {
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.title = title;
		this.content = content;
		this.originalFilename = originalFilename;
		this.storedFilename = storedFilename;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.viewCount = viewCount;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getStoredFilename() {
		return storedFilename;
	}

	public void setStoredFilename(String storedFilename) {
		this.storedFilename = storedFilename;
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

	@Override
	public String toString() {
		return "DataBoardDTO [id=" + id + ", userId=" + userId + ", username=" + username + ", title=" + title
				+ ", content=" + content + ", originalFilename=" + originalFilename + ", storedFilename="
				+ storedFilename + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", viewCount="
				+ viewCount + "]";
	}
}
