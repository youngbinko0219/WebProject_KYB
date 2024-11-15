package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class UserDAO {
	private DBConnection db;
	private Connection conn;

	public UserDAO() {
		db = new DBConnection();
		conn = db.con;
	}

	// 회원가입: 사용자 정보를 데이터베이스에 삽입하는 메서드
	public boolean registerUser(UserDTO user) {
		String query = "INSERT INTO USERS (USER_ID, USERNAME, PASSWORD, EMAIL, FULL_NAME, PHONE_NUMBER) VALUES (USER_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getFullName());
			pstmt.setString(5, user.getPhoneNumber());

			int result = pstmt.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isUsernameDuplicate(String username) {
		String query = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0; // 0보다 크면 중복 아이디 존재
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // 중복 아이디 없음
	}

	// 사용자 조회: 사용자 이름과 이메일을 기반으로 사용자 정보 조회
	public UserDTO getUserByUsernameAndEmail(String username, String email) {
		String query = "SELECT * FROM USERS WHERE USERNAME = ? AND EMAIL = ?";
		UserDTO user = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO(rs.getInt("USER_ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"),
						rs.getString("EMAIL"), rs.getString("FULL_NAME"), rs.getString("PHONE_NUMBER"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// 비밀번호 업데이트: 이메일을 통해 비밀번호 변경
	public boolean updatePasswordByEmail(String email, String newPassword) {
		String query = "UPDATE USERS SET PASSWORD = ? WHERE EMAIL = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, newPassword); // 실제로는 해싱된 비밀번호를 저장하는 것이 좋음
			pstmt.setString(2, email);

			int result = pstmt.executeUpdate();
			return result > 0; // 업데이트 성공 여부 반환
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 비밀번호 업데이트: 사용자 이름을 통해 비밀번호 변경
	public boolean updatePasswordByUsername(String username, String newPassword) {
		String query = "UPDATE USERS SET PASSWORD = ? WHERE USERNAME = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, newPassword); // 실제로는 해싱된 비밀번호를 저장하는 것이 좋음
			pstmt.setString(2, username);

			int result = pstmt.executeUpdate();
			return result > 0; // 업데이트 성공 여부 반환
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 사용자 인증: 사용자 이름과 비밀번호를 기반으로 로그인 인증
	public UserDTO authenticateUser(String username, String password) {
		String query = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
		UserDTO user = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO(rs.getInt("USER_ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"),
						rs.getString("EMAIL"), rs.getString("FULL_NAME"), rs.getString("PHONE_NUMBER"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public UserDTO getUserByUsername(String username) {
		String query = "SELECT * FROM USERS WHERE USERNAME = ?";
		UserDTO user = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO(rs.getInt("USER_ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"),
						rs.getString("EMAIL"), rs.getString("FULL_NAME"), rs.getString("PHONE_NUMBER"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public UserDTO getUserById(int userId) {
		String query = "SELECT * FROM USERS WHERE USER_ID = ?";
		UserDTO user = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO(rs.getInt("USER_ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"),
						rs.getString("EMAIL"), rs.getString("FULL_NAME"), rs.getString("PHONE_NUMBER"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// 사용자 정보를 업데이트하는 메서드
	public boolean updateUser(UserDTO user) {
		String query = "UPDATE USERS SET FULL_NAME = ?, EMAIL = ?, PHONE_NUMBER = ? WHERE USER_ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, user.getFullName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getPhoneNumber());
			pstmt.setInt(4, user.getUserId());

			int result = pstmt.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 자원 해제 메서드
	public void close() {
		db.close();
	}
}
