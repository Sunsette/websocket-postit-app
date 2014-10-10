package se.sunsette.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import se.sunsette.model.Postit;

public class PostitRepository {

	String userName = "root";
	String pass = "";
	String url = "jdbc:mysql://localhost/";
	String driver = "com.mysql.jdbc.Driver";
	String db = "postitApplication";

	Connection con;
	Statement st;

	public PostitRepository() {

		try {
			con = DriverManager.getConnection(url + db, userName, pass);
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int addProduct(Postit postit) {

		String sql = "INSERT INTO postit(title, information, color, whiteboard_id) VALUES(?,?,?,?)";
		int generatedId = 0;

		try {
			PreparedStatement pstmt = con.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, postit.getTitle());
			pstmt.setString(2, postit.getInformation());
			pstmt.setString(3, postit.getColor());
			pstmt.setInt(4, postit.getWhiteboardId());

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

	public void updatePostit(int id, Postit postit) {

		String sql = "UPDATE postit SET title = ?, information = ?, color = ?, whiteboard_id = ? WHERE id = ?";

		try {
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, postit.getTitle());
			pstmt.setString(2, postit.getInformation());
			pstmt.setString(3, postit.getColor());
			pstmt.setInt(4, postit.getWhiteboardId());
			pstmt.setInt(5, id);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deletePostit(int id) {

		String sql = "DELETE FROM postit WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Postit> getAllPostits(int currentWhiteboardId) {

		String sql = "SELECT postit.id, title, information, color, whiteboard_id FROM postit INNER JOIN whiteboard ON whiteboard.id = postit.whiteboard_id WHERE whiteboard_id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, currentWhiteboardId);

			ResultSet rs = pstmt.executeQuery();

			List<Postit> allPostits = new ArrayList<>();

			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String information = rs.getString("information");
				String color = rs.getString("color");
				int whiteboardId = rs.getInt("whiteboard_id");

				Postit pi = new Postit(id, title, information, color,
						whiteboardId);
				allPostits.add(pi);
			}

			return allPostits;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

}
