package com.example.crazybiz;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.LoginForm.LoginEvent;

public class LoginView extends VerticalLayout {
	private Panel loginPanel;
	private LoginForm loginForm;
	
	public LoginView() {
		loginPanel = new Panel("CrazyBiz Login");
		
		loginForm = new LoginForm();
		loginForm.setUsernameCaption("Username");
		loginForm.setPasswordCaption("Password");
		loginForm.setLoginButtonCaption("Login");
		loginForm.addListener(new LoginListener() {
			@Override
			public void onLogin(LoginEvent event) {	
				System.out.println("Login");
				System.out.println(event.getLoginParameter("username"));
				System.out.println(event.getLoginParameter("password"));
				getWindow().showNotification("LOGGED IN","\nWelcome "+event.getLoginParameter("username"));
				getWindow().setContent(new Panel("abra"));
			}
		});
		
		loginPanel.setWidth(Sizeable.SIZE_UNDEFINED,0);
		loginPanel.addComponent(loginForm);
		this.addComponent(loginPanel);
		this.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
	}
}