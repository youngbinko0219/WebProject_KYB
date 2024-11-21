package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnection;

public class CommentDAO {
	private Connection conn;

	// DAO 초기화
	public CommentDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
	}

	// 자유게시판 댓글 추가
	public boolean addFreeboardComment(CommentDTO comment) {
		String query = "INSERT INTO freeboard_comments (comment_id, board_id, user_id, content, created_date) "
				+ "VALUES (comments_id_seq.NEXTVAL, ?, ?, ?, SYSDATE)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, comment.getBoardId());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setString(3, comment.getContent());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 질문게시판 댓글 추가
	public boolean addQaboardComment(CommentDTO comment) {
		String query = "INSERT INTO qaboard_comments (comment_id, board_id, user_id, content, created_date) "
				+ "VALUES (comments_id_seq.NEXTVAL, ?, ?, ?, SYSDATE)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, comment.getBoardId());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setString(3, comment.getContent());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 자유게시판 댓글 목록 가져오기
	public List<CommentDTO> getFreeboardComments(int boardId) {
		String query = "SELECT c.comment_id, c.board_id, c.user_id, u.username, c.content, c.created_date "
				+ "FROM freeboard_comments c " + "JOIN users u ON c.user_id = u.user_id "
				+ "WHERE c.board_id = ? ORDER BY c.created_date ASC";
		List<CommentDTO> comments = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, boardId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				CommentDTO comment = new CommentDTO();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setBoardId(rs.getInt("board_id"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setUsername(rs.getString("username")); // 댓글 작성자 이름 추가
				comment.setContent(rs.getString("content"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comments;
	}

	// 질문게시판 댓글 목록 가져오기
	public List<CommentDTO> getQABoardComments(int postId) {
		String query = "SELECT comment_id, board_id, user_id, content, created_date "
				+ "FROM qaboard_comments WHERE board_id = ? ORDER BY created_date ASC";
		List<CommentDTO> comments = new ArrayList<>();
		UserDAO userDAO = new UserDAO(); // UserDAO 인스턴스 생성

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				CommentDTO comment = new CommentDTO();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setBoardId(rs.getInt("board_id"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setContent(rs.getString("content"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));

				// userId를 사용하여 username 조회
				String username = userDAO.getUsernameByUserId(comment.getUserId());
				comment.setUsername(username); // username 설정
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comments;
	}

	// 댓글 수정 (자유게시판)
	public boolean updateFreeboardComment(CommentDTO comment) {
		String query = "UPDATE freeboard_comments SET content = ?, updated_date = SYSDATE WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getCommentId());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 댓글 수정 (질문게시판)
	public boolean updateQaboardComment(CommentDTO comment) {
		String query = "UPDATE qaboard_comments SET content = ?, updated_date = SYSDATE WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getCommentId());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 댓글 삭제 (자유게시판)
	public boolean deleteFreeboardComment(int commentId) {
		String query = "DELETE FROM freeboard_comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 댓글 삭제 (질문게시판)
	public boolean deleteQaboardComment(int commentId) {
		String query = "DELETE FROM qaboard_comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 댓글 데이터 매핑
	private CommentDTO mapRowToCommentDTO(ResultSet rs) throws SQLException {
		return new CommentDTO(rs.getInt("comment_id"), rs.getInt("board_id"), rs.getInt("user_id"),
				rs.getString("content"), rs.getTimestamp("created_date"), rs.getTimestamp("updated_date"));
	}

	public CommentDTO getFreeboardCommentById(int commentId) {
		String query = "SELECT * FROM freeboard_comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return mapRowToCommentDTO(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CommentDTO getQaboardCommentById(int commentId) {
		String query = "SELECT * FROM qaboard_comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return mapRowToCommentDTO(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 자유게시판 게시글 작성자 ID 조회
	public int getFreeboardPostAuthorId(int boardId) {
		String query = "SELECT user_id FROM freeboard_posts WHERE board_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, boardId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // 작성자를 찾을 수 없을 경우
	}

	// 질문게시판 게시글 작성자 ID 조회
	public int getQaboardPostAuthorId(int boardId) {
		String query = "SELECT user_id FROM qaboard_posts WHERE board_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, boardId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("user_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; // 작성자를 찾을 수 없을 경우
	}

}
