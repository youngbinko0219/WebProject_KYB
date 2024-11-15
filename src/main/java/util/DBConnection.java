package util;

//DBConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "c##KYB";
	private static final String PASSWORD = "1234";

	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;

	// 생성자에서 연결을 설정하여 간단하게 초기화
	public DBConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("데이터베이스 연결 성공!");
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC 드라이버를 찾을 수 없습니다: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("데이터베이스 연결 실패: " + e.getMessage());
		}
	}

	// SQL 쿼리 실행을 위한 Statement 객체 생성 메서드
	public void createStatement() throws SQLException {
		stmt = con.createStatement();
	}

	// SQL 쿼리 실행을 위한 PreparedStatement 객체 생성 메서드
	public void prepareStatement(String sql) throws SQLException {
		psmt = con.prepareStatement(sql);
	}

	// 자원 해제 메서드: ResultSet, Statement, PreparedStatement, Connection 순서로 해제
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (psmt != null)
				psmt.close();
			if (con != null)
				con.close();
			System.out.println("데이터베이스 연결 종료!");
		} catch (SQLException e) {
			System.out.println("연결 종료 중 오류 발생: " + e.getMessage());
		}
	}
}
