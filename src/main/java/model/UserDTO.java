package model;

public class UserDTO {
	private int userId; // 고유번호
	private String username; // 아이디
	private String password;
	private String email;
	private String fullName; // 사용자 이름
	private String phoneNumber; // 전화번호

	// 기본 생성자
	public UserDTO() {
	}

	// 매개변수가 있는 생성자
	public UserDTO(int userId, String username, String password, String email, String fullName, String phoneNumber) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
	}

	// Getter and Setter methods
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
