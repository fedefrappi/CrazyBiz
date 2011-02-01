package com.example.crazybiz;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ProposalEntryComponent extends VerticalLayout implements Property.ValueChangeListener{
	
	private HorizontalLayout up;

	private TextField price;
	private PopupDateField date;
	private TextField user;
	private TextField message;
	
	public ProposalEntryComponent() {
		setSpacing(true);
		up = new HorizontalLayout();
		price = new TextField("Price");
		price.setValue(new BigDecimal(0.00));
		date = new PopupDateField("Date");
		date.setResolution(PopupDateField.RESOLUTION_DAY);
		user = new TextField("User");
		message = new TextField("Message");
		message.setSizeFull();
		up.addComponent(price);
		up.addComponent(user);
        up.addComponent(date);
		addComponent(up);
		addComponent(message);
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        Object value = event.getProperty().getValue();
        if (value == null || !(value instanceof Date)) {
            getWindow().showNotification("Invalid date entered");
        } else {
            String dateOut = dateFormatter.format(value);
        }
	}
	
	public BigDecimal getPrice() {
		return Utils.parsePrice(price.getValue().toString());
	}

	public String getMessage() {
		return message.getValue().toString();
	}

	public String getUser() {
		return user.getValue().toString();
	}
	
	public Date getDate() {
		if(date.getValue() == null){
			return new Date(System.currentTimeMillis());
		}
		return (Date)date.getValue();
	}

}