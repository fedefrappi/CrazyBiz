package com.example.crazybiz;


import java.sql.SQLException;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.LoginForm.LoginEvent;

import dao.DBactions;

public class LoginView extends VerticalLayout {
	private Panel loginPanel;
	private LoginForm loginForm;
	
	public LoginView() {
		setSpacing(true);
		loginPanel = new Panel("CrazyBiz Login");
		loginForm = new LoginForm();
		loginForm.setUsernameCaption("Username");
		loginForm.setPasswordCaption("Password");
		loginForm.setLoginButtonCaption("Login");
		loginForm.addListener(new LoginListener() {
			@Override
			public void onLogin(LoginEvent event) {
				//TODO Add credentials control
				try {
					DBactions.connect();
					//if(UserDAO.isValidLogin(event.getLoginParameter("username"), event.getLoginParameter("password"))){
						getWindow().showNotification("LOGGED IN","\nWelcome "+event.getLoginParameter("username"));
						getWindow().setContent(new Homepage(event.getLoginParameter("username")));
					//}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		loginPanel.setWidth(Sizeable.SIZE_UNDEFINED,0);
		loginPanel.addComponent(loginForm);
		
		this.addComponent(loginPanel);
		this.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}
}