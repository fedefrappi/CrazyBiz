package com.example.crazybiz;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ShippedPanel extends VerticalLayout{

	private TextField shippingCompany;
	private TextField tracking;
	private TextField to;
		
	public ShippedPanel() {        
        shippingCompany = new TextField("Shipping Company");
        tracking = new TextField("Tracking");
        to = new TextField("To");
        
        addComponent(shippingCompany);
        addComponent(tracking);
        addComponent(to);
	}

}
