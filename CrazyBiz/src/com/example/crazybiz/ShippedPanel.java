package com.example.crazybiz;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ShippedPanel extends VerticalLayout{

	private TextField company;
	private TextField tracking;
	private TextField recipient;
		
	public ShippedPanel() {        
        company = new TextField("Shipping Company");
        tracking = new TextField("Tracking");
        recipient = new TextField("To");
        
        addComponent(company);
        addComponent(tracking);
        addComponent(recipient);
	}

	public String getCompany() {
		return company.getValue().toString();
	}

	public String getTracking() {
		return tracking.getValue().toString();
	}

	public String getRecipient() {
		return recipient.getValue().toString();
	}
	
	public void setCompany(String newCompany) {
		this.company.setValue(newCompany);
	}
	
	public void setTracking(String newTracking) {
		this.tracking.setValue(newTracking);
	}
	
	public void setRecipient(String newRecipient) {
		this.recipient.setValue(newRecipient);
	}
	
}
