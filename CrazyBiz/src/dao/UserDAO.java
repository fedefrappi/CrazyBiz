package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO {
	
	public static boolean isValidLogin(String user, String pw) throws SQLException, ClassNotFoundException{
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/crazybiztest", "root", "pw");
		PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
		stm.setString(1, user);
		stm.setString(2, pw);
		ResultSet res = stm.executeQuery();
		if(res.next()){
			return true;
		}else{
			return false;
		}
	}
	
}
