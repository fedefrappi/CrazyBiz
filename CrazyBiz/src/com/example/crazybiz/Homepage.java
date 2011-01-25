package com.example.crazybiz;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class Homepage extends VerticalLayout {
	private String username;
	private Label cash;
	private Button insertButton;
	private Button searchButton;
	
	public Homepage(String username) {
		this.username = username;
		init();
	}

	private void init() {
		// Label
		cash = new Label("Current cash: -1000€");
		// Buttons
		insertButton = new Button("Insert Item");
        insertButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getWindow().setContent(new InsertItem(username));
			}
		});
        searchButton = new Button("Search item");
        searchButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getWindow().setContent(new SearchItem());
			}
		});
        // Horizontal layout for buttons
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setHeight("50px");
        horizontalLayout.setWidth(SIZE_UNDEFINED,0);
        horizontalLayout.addComponent(insertButton);
        horizontalLayout.addComponent(searchButton);
        
        addComponent(cash);
        addComponent(horizontalLayout);
		setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		setComponentAlignment(cash, Alignment.MIDDLE_CENTER);
	}
}
