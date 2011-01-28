package com.example.crazybiz;

import java.text.DateFormat;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class ProposalEntryComponent extends HorizontalLayout implements Property.ValueChangeListener{
	
	private TextField price;
	private PopupDateField date;
	private TextField user;
	private TextField message;
	
	public ProposalEntryComponent() {
		setSpacing(true);
		price = new TextField("Price");
		date = new PopupDateField("Date");
		user = new TextField("User");
		message = new TextField("Message");
		addComponent(price);
        addComponent(date);
		addComponent(user);
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
}
