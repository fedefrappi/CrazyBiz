package com.example.crazybiz;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import dao.DBactions;

public class Homepage extends VerticalLayout {
	private CrazybizApplication crazybizApplication;
	private String username;
	private Label userLogged;
	private Button userLogout;
	private Label cash;
	private Button insertButton;
	private Button searchButton;
	
	public Homepage(CrazybizApplication crazybizApplication,String username) {
		this.crazybizApplication = crazybizApplication;
		this.username = username;
		init();
	}

	private void init() {
		setSpacing(true);
		// UserLogged Label and button
		HorizontalLayout userLoggedLayout = new HorizontalLayout();
		userLoggedLayout.setMargin(false,true,false,false);
		userLogged = new Label("You are logged in as " + username);
		userLogout = new Button("Logout");
		userLogout.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//getWindow().removeComponent(getParent());
				//getWindow().setContent(new LoginView());
				crazybizApplication.getWindow().removeAllComponents();
				crazybizApplication.setLogin(new LoginView(crazybizApplication));
				crazybizApplication.getWindow().addComponent(crazybizApplication.getLogin());
			}
		});
		userLogout.setStyleName(BaseTheme.BUTTON_LINK);
		userLoggedLayout.setSpacing(true);
		userLoggedLayout.addComponent(userLogged);
		userLoggedLayout.addComponent(userLogout);

		// Cash Label
		HorizontalLayout cashLayout = new HorizontalLayout();
		cash = new Label("Current cash: " + getCurrentCash() + " €");
		cashLayout.addComponent(cash);
		// Buttons
		insertButton = new Button("Insert Item");
        insertButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeComponent(crazybizApplication.getHome());
				crazybizApplication.setInsert(new InsertItem(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getInsert());
			}
		});
        searchButton = new Button("Search item");
        searchButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeComponent(crazybizApplication.getHome());
				crazybizApplication.setSearch(new SearchItem(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getSearch());
			}
		});
        // Horizontal layout for buttons
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setHeight("50px");
        horizontalLayout.setWidth(SIZE_UNDEFINED,0);
        horizontalLayout.addComponent(insertButton);
        horizontalLayout.addComponent(searchButton);
        
        addComponent(userLoggedLayout);
        addComponent(cashLayout);
        addComponent(horizontalLayout);
        setComponentAlignment(userLoggedLayout, Alignment.TOP_RIGHT);
		setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(cashLayout, Alignment.MIDDLE_CENTER);
		
		// Background
		Embedded image = new Embedded("",new ThemeResource("images/pedobear.png"));
		image.setType(Embedded.TYPE_IMAGE);
		image.setHeight("400px");
		addComponent(image);
		setComponentAlignment(image, Alignment.MIDDLE_CENTER);
	}

	private String getCurrentCash() {
		BigDecimal currentCash = new BigDecimal(0);
		PreparedStatement stm;
		try {
			stm = DBactions.conn.prepareStatement(
					"SELECT sum(buy.price) " +
					"FROM buy;");
			ResultSet rs = stm.executeQuery();
			if(rs.next()){
				currentCash = currentCash.add(rs.getBigDecimal(1));
			}
			stm = DBactions.conn.prepareStatement(
					"SELECT sum(sell.price) " +
					"FROM sell;");
			rs = stm.executeQuery();
			if(rs.next()){
				currentCash = currentCash.subtract(rs.getBigDecimal(1));
			}
		} catch (SQLException e) {e.printStackTrace();}
		return currentCash.toString();
	}
}
