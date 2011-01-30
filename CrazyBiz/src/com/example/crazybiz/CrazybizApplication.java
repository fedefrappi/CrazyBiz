package com.example.crazybiz;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class CrazybizApplication extends Application {
	
	private Window mainWindow;
	private LoginView login;
	private Homepage home;
	private InsertItem insert;
	private SearchItem search;
	private String username;
	
	@Override
	public void init() {
		mainWindow = new Window("Crazybiz Application");
		
		login = new LoginView(this);
		mainWindow.addComponent(login);
		setMainWindow(mainWindow);
		setTheme("crazybiztheme");
	}

	public Window getWindow(){
		return mainWindow;
	}
	
	public LoginView getLogin() {
		return login;
	}

	public Homepage getHome() {
		return home;
	}

	public InsertItem getInsert() {
		return insert;
	}

	public SearchItem getSearch() {
		return search;
	}
	
	
	public void setLogin(LoginView login) {
		this.login = login;
	}

	public void setHome(Homepage home) {
		this.home = home;
	}

	public void setInsert(InsertItem insert) {
		this.insert = insert;
	}

	public void setSearch(SearchItem search) {
		this.search = search;
	}

	public void setUsername(String username){
		this.username = username;
	}

}
