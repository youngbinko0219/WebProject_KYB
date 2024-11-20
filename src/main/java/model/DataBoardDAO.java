package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class DataBoardDAO {
	private Connection conn;

	public DataBoardDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
	}

	// 게시글 목록 가져오기 (리스트 페이지에서 사용)
	public List<DataBoardDTO> getDataBoardList(int offset, int limit) {
		String query = "SELECT d.*, u.username " + "FROM databoard d " + "LEFT JOIN users u ON d.user_id = u.user_id "
				+ "ORDER BY d.created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<DataBoardDTO> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, offset);
			pstmt.setInt(2, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(mapRowToDataBoardDTO(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 특정 게시글 가져오기 (상세보기 페이지에서 사용)
	public DataBoardDTO getDataBoardById(int id) {
		String query = "SELECT d.*, u.username " + "FROM databoard d " + "LEFT JOIN users u ON d.user_id = u.user_id "
				+ "WHERE d.id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return mapRowToDataBoardDTO(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 게시글 생성
	public boolean createDataBoardPost(DataBoardDTO post) {
		String query = "INSERT INTO databoard (id, user_id, title, content, original_filename, "
				+ "stored_filename, created_date, view_count) "
				+ "VALUES (databoard_id_seq.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, 0)";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, post.getUserId());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());
			pstmt.setString(4, post.getOriginalFilename());
			pstmt.setString(5, post.getStoredFilename());

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 게시글 수정
	public boolean updateDataBoardPost(DataBoardDTO post) {
		String query = "UPDATE databoard SET title = ?, content = ?, original_filename = ?, "
				+ "stored_filename = ?, updated_date = SYSDATE WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setString(3, post.getOriginalFilename());
			pstmt.setString(4, post.getStoredFilename());
			pstmt.setInt(5, post.getId());

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 게시글 삭제
	public boolean deleteDataBoardPost(int id) {
		String query = "DELETE FROM databoard WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 게시글 조회수 증가
	public boolean incrementViewCount(int id) {
		String query = "UPDATE databoard SET view_count = view_count + 1 WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 최신 게시글 가져오기 (대시보드에서 사용)
	public List<DataBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT d.*, u.username " + "FROM databoard d " + "LEFT JOIN users u ON d.user_id = u.user_id "
				+ "ORDER BY d.created_date DESC FETCH FIRST ? ROWS ONLY";
		List<DataBoardDTO> posts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				posts.add(mapRowToDataBoardDTO(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}

	// Helper Method: ResultSet을 DataBoardDTO로 매핑
	private DataBoardDTO mapRowToDataBoardDTO(ResultSet rs) throws SQLException {
		DataBoardDTO post = new DataBoardDTO();
		post.setId(rs.getInt("id"));
		post.setUserId(rs.getInt("user_id"));
		post.setUsername(rs.getString("username")); // JOIN된 username
		post.setTitle(rs.getString("title"));
		post.setContent(rs.getString("content"));
		post.setOriginalFilename(rs.getString("original_filename"));
		post.setStoredFilename(rs.getString("stored_filename"));
		post.setCreatedDate(rs.getDate("created_date"));
		post.setUpdatedDate(rs.getDate("updated_date"));
		post.setViewCount(rs.getInt("view_count"));
		return post;
	}

	public DataBoardDTO getPostById(int postId) {
		String query = "SELECT * FROM databoard WHERE id = ?";
		DataBoardDTO post = null;

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				post = new DataBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setOriginalFilename(rs.getString("original_filename"));
				post.setStoredFilename(rs.getString("stored_filename"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setUpdatedDate(rs.getDate("updated_date"));
				post.setViewCount(rs.getInt("view_count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return post;
	}

	public List<DataBoardDTO> searchPostsByTitle(String query) {
		List<DataBoardDTO> results = new ArrayList<>();
		String sql = "SELECT * FROM databoard WHERE title LIKE ?";

		DBConnection db = new DBConnection();
		try {
			db.prepareStatement(sql);
			String keyword = "%" + query + "%";
			db.psmt.setString(1, keyword);

			db.rs = db.psmt.executeQuery();
			while (db.rs.next()) {
				DataBoardDTO post = new DataBoardDTO();
				// db.rs에서 데이터 추출하여 post 객체에 설정
				post.setId(db.rs.getInt("id"));
				post.setTitle(db.rs.getString("title"));
				post.setContent(db.rs.getString("content"));
				post.setUserId(db.rs.getInt("user_id"));
				post.setCreatedDate(db.rs.getTimestamp("created_date"));
				// 필요한 다른 필드들도 설정
				results.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // 로깅하는 것이 좋습니다.
		} finally {
			db.close(); // 자원 해제
		}
		return results;
	}

	public List<DataBoardDTO> searchPostsByContent(String query) {
		List<DataBoardDTO> results = new ArrayList<>();
		String sql = "SELECT * FROM databoard WHERE content LIKE ?";

		DBConnection db = new DBConnection();
		try {
			db.prepareStatement(sql);
			String keyword = "%" + query + "%";
			db.psmt.setString(1, keyword);

			db.rs = db.psmt.executeQuery();
			while (db.rs.next()) {
				DataBoardDTO post = new DataBoardDTO();
				// db.rs에서 데이터 추출하여 post 객체에 설정
				post.setId(db.rs.getInt("id"));
				post.setTitle(db.rs.getString("title"));
				post.setContent(db.rs.getString("content"));
				post.setUserId(db.rs.getInt("user_id"));
				post.setCreatedDate(db.rs.getTimestamp("created_date"));
				// 필요한 다른 필드들도 설정
				results.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // 로깅하는 것이 좋습니다.
		} finally {
			db.close(); // 자원 해제
		}
		return results;
	}
}