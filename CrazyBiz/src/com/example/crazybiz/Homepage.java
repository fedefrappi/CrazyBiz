package com.example.crazybiz;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.vaadin.notifique.Notifique;
import org.vaadin.notifique.Notifique.Message;
import org.vaadin.vaadinvisualizations.PieChart;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

import db.DBactions;

public class Homepage extends VerticalLayout {
	private CrazybizApplication crazybizApplication;
	private String username;
	private Label userLogged;
	private Button userLogout;
	private Label cash;
	private Button insertButton;
	private Button searchButton;
	private Button statsButton;
	
	private HorizontalLayout down;
	private Notifique notifications;
	
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
		cash = new Label("Current cash: " + getCurrentCash() + " �");
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
        statsButton = new Button("View stats");
        statsButton.addListener(new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeComponent(crazybizApplication.getHome());
				crazybizApplication.setViewStats(new ViewStats(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getViewStats());
			}
		});
        // Horizontal layout for buttons
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setHeight("50px");
        horizontalLayout.setWidth(SIZE_UNDEFINED,0);
        horizontalLayout.addComponent(insertButton);
        horizontalLayout.addComponent(searchButton);
        horizontalLayout.addComponent(statsButton);
		
		// IMAGE
		Embedded image;
		if(!cash.getValue().toString().contains("-")){
			image = new Embedded("",new ThemeResource("images/RICHpedobear.png"));
		}else{
			image = new Embedded("",new ThemeResource("images/ANGRYpedobear.png"));
		}
		// OLD check with the signum of the BigDecimal class...it fails miserably!
		//	if(new BigDecimal(cash.getValue().toString()).signum() == 1){
		//		image = new Embedded("",new ThemeResource("images/RICHpedobear.png"));
		//	}else{
		//		image = new Embedded("",new ThemeResource("images/ANGRYpedobear.png"));
		//	}
		image.setType(Embedded.TYPE_IMAGE);
		image.setHeight("300px");
		
		
		// NOTIFICATIONS AREA
		notifications = new Notifique(false);
		notifications.setWidth("400px");
		// Populate notifications
		try {
			ResultSet rs = DBactions.getSearchResults(
					"SELECT brand.brand_name,model.model_name,item.source,item.status " +
	        		"FROM brand,model,item " +
	        		"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
	        		"ORDER BY item.lastModified DESC");
			for(int i=0; i<5; i++){
				if(rs.next()){
					String str = rs.getString(1) + "  " + rs.getString(2) + " (" + rs.getString(3) + ")  -->  " + rs.getString(4);
					notifications.add(null, str, true, Notifique.Styles.MAGIC_WHITE, true);
				}
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		notifications.setClickListener(new Notifique.ClickListener() {
			public void messageClicked(Message message) {
				
			}
		});
		
		down = new HorizontalLayout();
		down.setSizeFull();
		down.addComponent(image);
		down.addComponent(notifications);
		down.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		down.setComponentAlignment(notifications, Alignment.MIDDLE_CENTER);
		
        addComponent(userLoggedLayout);
        addComponent(cashLayout);
        addComponent(horizontalLayout);
        addComponent(down);
        
        setComponentAlignment(userLoggedLayout, Alignment.TOP_RIGHT);
		setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(cashLayout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(down, Alignment.MIDDLE_CENTER);
		
	}

	private String getCurrentCash() {
		BigDecimal currentCash = new BigDecimal(0);
		PreparedStatement stm;
		try {
			stm = DBactions.conn.prepareStatement(
					"SELECT sum(sell.price) " +
					"FROM sell;");
			ResultSet rs = stm.executeQuery();
			if(rs.next()){
				if(rs.getBigDecimal(1) != null){
					currentCash = currentCash.add(rs.getBigDecimal(1));
				}
			}
			stm = DBactions.conn.prepareStatement(
					"SELECT sum(buy.price) " +
			"FROM buy;");
			rs = stm.executeQuery();
			if(rs.next()){
				if(rs.getBigDecimal(1) != null){
					currentCash = currentCash.subtract(rs.getBigDecimal(1));
				}
			}
		} catch (SQLException e) {e.printStackTrace();}
		return currentCash.toString();
	}
}
