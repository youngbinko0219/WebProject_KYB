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

	// 게시글 목록 가져오기
	public List<FreeBoardDTO> getFreeBoardList(int offset, int limit) {
		String query = "SELECT * FROM freeboard ORDER BY created_date DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		List<FreeBoardDTO> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, offset);
			pstmt.setInt(2, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new FreeBoardDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"),
						rs.getString("content"), rs.getDate("created_date"), rs.getDate("updated_date"),
						rs.getInt("view_count"), rs.getInt("like_count") // like_count 추가
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 게시글 총 개수 가져오기
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

	// 특정 게시글 좋아요 수 증가
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

	// 특정 게시글의 좋아요 수 가져오기
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

	public FreeBoardDTO getPostById(int postId) {
		String query = "SELECT * FROM freeboard WHERE id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new FreeBoardDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"),
						rs.getString("content"), rs.getDate("created_date"), rs.getDate("updated_date"),
						rs.getInt("view_count"), rs.getInt("like_count") // 좋아요 수 포함
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // 게시글을 찾을 수 없을 때
	}

	public List<FreeBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT * FROM freeboard ORDER BY created_date DESC FETCH FIRST ? ROWS ONLY";
		List<FreeBoardDTO> posts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				posts.add(new FreeBoardDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"),
						rs.getString("content"), rs.getDate("created_date"), rs.getDate("updated_date"),
						rs.getInt("view_count"), rs.getInt("like_count")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}
}
