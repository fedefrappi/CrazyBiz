package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Brand;
import domain.Model;


public class DBactions {
	public static Connection conn;
	
	public static void connect() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn =  DriverManager.getConnection("jdbc:mysql://localhost:3306/crazybiztest", "root", "pw");
	}
	
	public static boolean isValidLogin(String user, String pw) throws SQLException, ClassNotFoundException{
		
		PreparedStatement stm = conn.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
		stm.setString(1, user);
		stm.setString(2, pw);
		ResultSet res = stm.executeQuery();
		if(res.next()){
			return true;
		}else{
			return false;
		}
	}
	
	public static List<Brand> getBrands() throws SQLException{
		PreparedStatement stm = conn.prepareStatement("SELECT * FROM brand");
		ResultSet res = stm.executeQuery();
		List<Brand> result = new ArrayList<Brand>();
		while(res.next()){
			result.add(new Brand(res.getInt("brand_id"),res.getString("brand_name"),res.getString("brand_website")));
		}
		return result;
	}
	
	public static List<Model> getModels(String brandName) throws SQLException{
		PreparedStatement stm = conn.prepareStatement("SELECT * FROM model,brand WHERE model.brand_id = brand.brand_id AND brand.brand_name =?");
		stm.setString(1, brandName);
		ResultSet res = stm.executeQuery();
		List<Model> result = new ArrayList<Model>();
		while(res.next()){
			result.add(new Model(res.getInt("model_id"),res.getString("model_name"),res.getBigDecimal("model_price")));
		}
		return result;
	}
	
	public static void addBrand(String brandName) throws SQLException{
		PreparedStatement stm = conn.prepareStatement("INSERT INTO brand(brand_name,brand_website) VALUES(?,?)");
		stm.setString(1, brandName);
		stm.setString(2, "unknown");
	}

	public static ResultSet getSearchResults(String string) throws SQLException {
		PreparedStatement stm = conn.prepareStatement(string);
		return stm.executeQuery();
	}

	
}
