package model;

import java.util.Date;

public class DataBoardDTO {
	private int id;
	private int userId;
	private String title;
	private String content;
	private Date createdDate;
	private Date updatedDate;
	private String originalFilename;
	private String storedFilename;
	private int viewCount;

	// 기본 생성자
	public DataBoardDTO() {
	}

	// 매개변수가 있는 생성자
	public DataBoardDTO(int id, int userId, String title, String content, Date createdDate, Date updatedDate,
			String originalFilename, String storedFilename, int viewCount) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.originalFilename = originalFilename;
		this.storedFilename = storedFilename;
		this.viewCount = viewCount;
	}

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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

}
