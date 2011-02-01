package com.example.crazybiz;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SoldPanel extends VerticalLayout implements Property.ValueChangeListener{

	private TextField price;
	private PopupDateField date;
	private TextField buyer;
		
	public SoldPanel() {        
        price = new TextField("Price");
        price.setValue(new BigDecimal(0.00));
		date = new PopupDateField("Date");
        date.setResolution(PopupDateField.RESOLUTION_DAY);
        date.addListener(this);
        date.setImmediate(true);
        buyer = new TextField("Buyer");
        
        addComponent(price);
        addComponent(date);
        addComponent(buyer);
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

	public Date getDate() {
		if(date.getValue() == null){
			return new Date(System.currentTimeMillis());
		}
		return (Date)date.getValue();
	}

	public String getBuyer() {
		return buyer.getValue().toString();
	}
	
	public void setPrice(BigDecimal newPrice) {
		this.price.setValue(newPrice);
	}
	
	public void setBuyer(String newBuyer) {
		this.buyer.setValue(newBuyer);
	}
	
	public void setDate(Date newDate) {
		this.date.setValue(newDate);
	}
}
