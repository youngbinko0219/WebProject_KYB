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

	public List<DataBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT * FROM databoard ORDER BY created_date DESC FETCH FIRST ? ROWS ONLY";
		List<DataBoardDTO> posts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				posts.add(new DataBoardDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"),
						rs.getString("content"), rs.getDate("created_date"), rs.getDate("updated_date"),
						rs.getString("original_filename"), rs.getString("stored_filename"), rs.getInt("view_count")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}
}
