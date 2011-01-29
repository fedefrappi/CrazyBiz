package com.example.crazybiz;

import java.math.BigDecimal;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PostEntryComponent extends VerticalLayout{
	
	private HorizontalLayout up;
	private TextField price;
	private TextField source;
	private TextField message;
	
	public PostEntryComponent() {
		setSpacing(true);
		up = new HorizontalLayout();
		price = new TextField("Price");
		price.setValue(new BigDecimal(0.00));
		source = new TextField("Source");
		message = new TextField("Message");
		message.setSizeFull();
		up.addComponent(price);
		up.addComponent(source);
		addComponent(up);
		addComponent(message);
	}

	public BigDecimal getPrice() {
		return Utils.parsePrice(price.getValue().toString());
	}

	public String getSource() {
		return source.getValue().toString();
	}

	public String getMessage() {
		return message.getValue().toString();
	}
	
	
}
