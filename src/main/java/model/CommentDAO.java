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

	public CommentDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
	}

	// 댓글 추가
	public boolean addComment(CommentDTO comment) {
		String query = "INSERT INTO comments (comment_id, board_type, board_id, user_id, content, created_date) "
				+ "VALUES (comments_id_seq.NEXTVAL, 'F', ?, ?, ?, SYSDATE)";
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

	// 댓글 수정
	public boolean updateComment(CommentDTO comment) {
		String query = "UPDATE comments SET content = ?, updated_date = SYSDATE WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getCommentId());
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 댓글 삭제
	public boolean deleteComment(int commentId) {
		String query = "DELETE FROM comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 특정 댓글 조회
	public CommentDTO getCommentById(int commentId) {
		String query = "SELECT * FROM comments WHERE comment_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, commentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new CommentDTO(rs.getInt("comment_id"), rs.getString("board_type"), rs.getInt("board_id"),
						rs.getInt("user_id"), rs.getString("content"), rs.getTimestamp("created_date"),
						rs.getTimestamp("updated_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 특정 게시글에 대한 댓글 리스트 가져오기
	public List<CommentDTO> getCommentsByPostId(int postId) {
		String query = "SELECT * FROM comments WHERE board_id = ? AND board_type = 'F' ORDER BY created_date ASC";
		List<CommentDTO> comments = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.add(new CommentDTO(rs.getInt("comment_id"), rs.getString("board_type"), rs.getInt("board_id"),
						rs.getInt("user_id"), rs.getString("content"), rs.getTimestamp("created_date"),
						rs.getTimestamp("updated_date")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comments;
	}
}
