package com.example.crazybiz;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class PostEntryComponent extends HorizontalLayout{
	
	private TextField price;
	private TextField source;
	private TextField message;
	
	public PostEntryComponent() {
		setSpacing(true);
		price = new TextField("Price");
		source = new TextField("Source");
		message = new TextField("Message");
		addComponent(price);
		addComponent(source);
		addComponent(message);
	}
}
