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

	public QaBoardDAO() {
		DBConnection db = new DBConnection();
		this.conn = db.con;
	}

	public List<QaBoardDTO> getLatestPosts(int limit) {
		String query = "SELECT * FROM qaboard ORDER BY created_date DESC FETCH FIRST ? ROWS ONLY";
		List<QaBoardDTO> posts = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				posts.add(new QaBoardDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("title"),
						rs.getString("content"), rs.getDate("created_date"), rs.getDate("updated_date"),
						rs.getInt("view_count")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return posts;
	}
}
