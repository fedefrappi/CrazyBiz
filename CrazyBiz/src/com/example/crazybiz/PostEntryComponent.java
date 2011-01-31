package com.example.crazybiz;

import java.math.BigDecimal;
import java.util.Date;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class PostEntryComponent extends VerticalLayout{
	
	private HorizontalLayout up;
	private TextField price;
	private PopupDateField date;
	private TextField source;
	private TextField message;
	
	public PostEntryComponent() {
		setSpacing(true);
		up = new HorizontalLayout();
		price = new TextField("Price");
		price.setValue(new BigDecimal(0.00));
		source = new TextField("Source");
		date = new PopupDateField("Date");
		date.setResolution(PopupDateField.RESOLUTION_DAY);
		message = new TextField("Message");
		message.setSizeFull();
		up.addComponent(price);
		up.addComponent(source);
		up.addComponent(date);
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
	
	public Date getDate() {
		Date d = (Date)date.getValue();
		return d;
	}
	
}
