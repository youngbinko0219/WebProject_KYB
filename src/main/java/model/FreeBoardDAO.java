package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class FreeBoardDAO {
	private Connection conn;

	public FreeBoardDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
	}

	// 게시글 목록 가져오기 (리스트 페이지에서 사용)
	public List<FreeBoardDTO> getFreeBoardList(int offset, int limit) {
		String query = "SELECT id, user_id, username, title, created_date, view_count, like_count "
				+ "FROM freeboard ORDER BY created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<FreeBoardDTO> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, offset);
			pstmt.setInt(2, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				FreeBoardDTO post = new FreeBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setUsername(rs.getString("username")); // username 추가
				post.setTitle(rs.getString("title"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setViewCount(rs.getInt("view_count"));
				post.setLikeCount(rs.getInt("like_count"));

				list.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 게시글 총 개수 가져오기 (페이지네이션에 사용)
	public int getFreeBoardCount() {
		String query = "SELECT COUNT(*) AS count FROM freeboard";
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

	// 특정 게시글 조회 (상세 페이지에서 사용)
	public FreeBoardDTO getPostById(int postId) {
		String query = "SELECT id, user_id, username, title, content, created_date, updated_date, view_count, like_count "
				+ "FROM freeboard WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				FreeBoardDTO post = new FreeBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setUsername(rs.getString("username")); // username 추가
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setUpdatedDate(rs.getDate("updated_date"));
				post.setViewCount(rs.getInt("view_count"));
				post.setLikeCount(rs.getInt("like_count"));

				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // 게시글을 찾을 수 없을 때
	}

	// 특정 게시글의 좋아요 수 증가 (좋아요 기능)
	public boolean incrementLikeCount(int postId) {
		String query = "UPDATE freeboard SET like_count = like_count + 1 WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0; // 업데이트 성공 여부 반환
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 특정 게시글의 좋아요 수 감소 (좋아요 취소 기능) // 수정
	public boolean decrementLikeCount(int postId) {
		String query = "UPDATE freeboard SET like_count = like_count - 1 WHERE id = ? AND like_count > 0";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0; // 업데이트 성공 여부 반환
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 사용자가 특정 게시글에 좋아요를 눌렀는지 확인 (좋아요 상태 확인) // 추가
	public boolean hasUserLikedPost(int userId, int postId) {
		String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND post_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 특정 게시글에 사용자 좋아요 추가 (좋아요 기능) // 추가
	public void addUserLike(int userId, int postId) {
		String query = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 특정 게시글에서 사용자 좋아요 제거 (좋아요 취소 기능) // 추가
	public void removeUserLike(int userId, int postId) {
		String query = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 특정 게시글의 좋아요 수 가져오기 (옵션, 필요 시 사용)
	public int getLikeCount(int postId) {
		String query = "SELECT like_count FROM freeboard WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("like_count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 최신 게시글 가져오기 (대시보드에서 사용 가능)
	public List<FreeBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT id, user_id, username, title, created_date, view_count, like_count "
				+ "FROM freeboard ORDER BY created_date DESC FETCH FIRST ? ROWS ONLY";
		List<FreeBoardDTO> posts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				FreeBoardDTO post = new FreeBoardDTO();
				post.setId(rs.getInt("id"));
				post.setUserId(rs.getInt("user_id"));
				post.setUsername(rs.getString("username")); // username 추가
				post.setTitle(rs.getString("title"));
				post.setCreatedDate(rs.getDate("created_date"));
				post.setViewCount(rs.getInt("view_count"));
				post.setLikeCount(rs.getInt("like_count"));

				posts.add(post);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}

	// 새로운 게시글 저장
	public boolean createPost(FreeBoardDTO post) {
		String query = "INSERT INTO freeboard (id, user_id, username, title, content, created_date, view_count, like_count) "
				+ "VALUES (freeboard_id_seq.NEXTVAL, ?, ?, ?, ?, ?, 0, 0)";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, post.getUserId());
			pstmt.setString(2, post.getUsername()); // username 추가
			pstmt.setString(3, post.getTitle());
			pstmt.setString(4, post.getContent());
			pstmt.setDate(5, new java.sql.Date(post.getCreatedDate().getTime())); // java.util.Date -> java.sql.Date 변환

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean incrementViewCount(int postId) {
		String query = "UPDATE freeboard SET view_count = view_count + 1 WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0; // 업데이트 성공 여부 반환
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deletePostById(int postId) {
		String query = "DELETE FROM freeboard WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updatePost(FreeBoardDTO post) {
		String query = "UPDATE freeboard SET title = ?, content = ?, updated_date = SYSDATE WHERE id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setInt(3, post.getId());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getUsernameByUserId(int userId) {
		String query = "SELECT username FROM users WHERE user_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // 조회 실패 시 null 반환
	}

	public List<FreeBoardDTO> searchPostsByTitle(String query) {
		List<FreeBoardDTO> results = new ArrayList<>();
		String sql = "SELECT * FROM freeboard WHERE title LIKE ?";

		DBConnection db = new DBConnection();
		try {
			db.prepareStatement(sql);
			String keyword = "%" + query + "%";
			db.psmt.setString(1, keyword);

			db.rs = db.psmt.executeQuery();
			while (db.rs.next()) {
				FreeBoardDTO post = new FreeBoardDTO();
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

	public List<FreeBoardDTO> searchPostsByContent(String query) {
		List<FreeBoardDTO> results = new ArrayList<>();
		String sql = "SELECT * FROM freeboard WHERE content LIKE ?";

		DBConnection db = new DBConnection();
		try {
			db.prepareStatement(sql);
			String keyword = "%" + query + "%";
			db.psmt.setString(1, keyword);

			db.rs = db.psmt.executeQuery();
			while (db.rs.next()) {
				FreeBoardDTO post = new FreeBoardDTO();
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
