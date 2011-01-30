package com.example.crazybiz;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaadin.data.util.BeanItemContainer;

import dao.DBactions;

public class ResultContainer extends BeanItemContainer<MyResult> implements Serializable{
    
	public ResultContainer() throws InstantiationException,IllegalAccessException {
          super(MyResult.class);
          return;
    }
	
	public static ResultContainer create(String query){
		ResultContainer rc = null;
		
		try {
			rc = new ResultContainer();
			ResultSet rs = DBactions.getSearchResults(query);
			while(rs.next()){
				MyResult result = new MyResult(rs.getString(1), rs.getString(2), rs.getString(3));
				rc.addBean(result);
			}
		} 
		catch (InstantiationException e) {e.printStackTrace();} 
		catch (IllegalAccessException e) {e.printStackTrace();} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rc;
	}
}
