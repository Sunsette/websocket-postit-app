package se.sunsette.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.sunsette.model.Whiteboard;

public class WhiteboardRepository {

	String userName = "root";
	String pass = "";
	String url = "jdbc:mysql://localhost/";
	String driver = "com.mysql.jdbc.Driver";
	String db = "postitApplication";

	Connection con;
	Statement st;

	public WhiteboardRepository() {

		try {
			con = DriverManager.getConnection(url + db, userName, pass);
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int addWhiteboard(String name) {
		String sql = "INSERT INTO whiteboard(name) VALUES(?)";

		int generatedId = 0;

		try {
			PreparedStatement pstmt = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, name);

			pstmt.executeUpdate();

			try (ResultSet rs = pstmt.getGeneratedKeys()) {

				if (rs.next()) {
					generatedId = rs.getInt(1);
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedId;
	}

	public void deleteWhiteboard(int id) {
		String sql = "DELETE FROM whiteboard WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			deletePostits(id);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deletePostits(int id) {
		String sql = "DELETE FROM postit WHERE whiteboard_id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, id);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Whiteboard> getAllWhiteboards() {

		String sql = "SELECT id, name FROM whiteboard";

		List<Whiteboard> whiteboards = new ArrayList<>();

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");

				Whiteboard currentWhiteboard = new Whiteboard(id, name);

				whiteboards.add(currentWhiteboard);
			}

			return whiteboards;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
