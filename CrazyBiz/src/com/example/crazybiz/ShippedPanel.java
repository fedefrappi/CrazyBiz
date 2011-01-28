package com.example.crazybiz;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ShippedPanel extends VerticalLayout{

	private TextField company;
	private TextField tracking;
	private TextField to;
		
	public ShippedPanel() {        
        company = new TextField("Shipping Company");
        tracking = new TextField("Tracking");
        to = new TextField("To");
        
        addComponent(company);
        addComponent(tracking);
        addComponent(to);
	}

	public String getCompany() {
		return company.getValue().toString();
	}

	public String getTracking() {
		return tracking.getValue().toString();
	}

	public String getTo() {
		return to.getValue().toString();
	}
	
}
