package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class QaBoardDAO {
	private Connection conn;
	private UserDAO userDAO;

	public QaBoardDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
		this.userDAO = new UserDAO();
	}

	// 게시글 목록 가져오기
	public List<QaBoardDTO> getQABoardList(int offset, int limit) {
		String query = "SELECT q.id, q.user_id, u.username, q.title, q.created_date, q.view_count " + "FROM qaboard q "
				+ "LEFT JOIN users u ON q.user_id = u.user_id "
				+ "ORDER BY q.created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<QaBoardDTO> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, offset);
			pstmt.setInt(2, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				QaBoardDTO post = new QaBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setUsername(rs.getString("username")); // Username from join
				post.setTitle(rs.getString("title"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setViewCount(rs.getInt("view_count"));

				list.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 특정 게시글 조회
	public QaBoardDTO getQAPostById(int postId) {
		String query = "SELECT q.id, q.user_id, u.username, q.title, q.content, q.created_date, q.updated_date, q.view_count "
				+ "FROM qaboard q " + "LEFT JOIN users u ON q.user_id = u.user_id " + "WHERE q.id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				QaBoardDTO post = new QaBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setUsername(rs.getString("username")); // Username from join
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setUpdatedDate(rs.getDate("updated_date"));
				post.setViewCount(rs.getInt("view_count"));

				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // 게시글을 찾을 수 없을 때
	}

	// 새로운 게시글 저장
	public boolean createQAPost(QaBoardDTO post) {
		String query = "INSERT INTO qaboard (id, user_id, title, content, created_date, view_count) "
				+ "VALUES (qaboard_id_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, post.getUserId());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());

			return pstmt.executeUpdate() > 0; // 성공적으로 삽입되면 true
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 게시글 수정
	public boolean updateQAPost(QaBoardDTO post) {
		String query = "UPDATE qaboard SET title = ?, content = ?, updated_date = SYSDATE WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setInt(3, post.getId());

			return pstmt.executeUpdate() > 0; // 성공적으로 수정되면 true
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 게시글 삭제
	public boolean deleteQAPostById(int postId) {
		String query = "DELETE FROM qaboard WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0; // 성공적으로 삭제되면 true
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 게시글 조회수 증가
	public boolean incrementViewCount(int postId) {
		String query = "UPDATE qaboard SET view_count = view_count + 1 WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0; // 성공적으로 업데이트되면 true
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 게시글 총 개수 (페이징 처리에 사용)
	public int getQABoardCount() {
		String query = "SELECT COUNT(*) AS count FROM qaboard";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 최신 게시글 가져오기 (대시보드에서 사용)
	public List<QaBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT q.id, q.title, q.created_date, q.view_count, u.username " + "FROM qaboard q "
				+ "LEFT JOIN users u ON q.user_id = u.user_id "
				+ "ORDER BY q.created_date DESC FETCH FIRST ? ROWS ONLY";
		List<QaBoardDTO> latestPosts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				QaBoardDTO post = new QaBoardDTO();
				post.setId(rs.getInt("id"));
				post.setTitle(rs.getString("title"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setUsername(rs.getString("username"));
				post.setViewCount(rs.getInt("view_count"));

				latestPosts.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return latestPosts;
	}

	public QaBoardDTO getPostById(int postId) {
		String query = "SELECT * FROM qaboard WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				QaBoardDTO post = new QaBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setUpdatedDate(rs.getDate("updated_date"));
				post.setViewCount(rs.getInt("view_count"));

				// user_id를 기반으로 username 조회
				String username = userDAO.getUsernameByUserId(post.getUserId());
				post.setUsername(username); // username 설정

				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<QaBoardDTO> searchPosts(String query) {
		List<QaBoardDTO> results = new ArrayList<>();
		String sql = "SELECT * FROM qaboard WHERE title LIKE ? OR content LIKE ?";

		DBConnection db = new DBConnection();
		try {
			db.prepareStatement(sql);
			String keyword = "%" + query + "%";
			db.psmt.setString(1, keyword);
			db.psmt.setString(2, keyword);

			db.rs = db.psmt.executeQuery();
			while (db.rs.next()) {
				QaBoardDTO post = new QaBoardDTO();
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
