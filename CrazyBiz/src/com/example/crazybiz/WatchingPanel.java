package com.example.crazybiz;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WatchingPanel extends VerticalLayout{

	private TextField price;

	private static final List<String> priceOptions = Arrays.asList(new String[] {"IN", "OUT"});
	private OptionGroup priceOptionsSelection;
	
	public WatchingPanel() {
		price = new TextField("Price");
		price.setValue(new BigDecimal(0.00));

		priceOptionsSelection = new OptionGroup("FDP", priceOptions);
		priceOptionsSelection.setNullSelectionAllowed(false);
        priceOptionsSelection.select("OUT"); // select this by default
        priceOptionsSelection.setImmediate(true); // send the change to the server at once
        
        addComponent(price);
        addComponent(priceOptionsSelection);
	}
	
	public BigDecimal getPrice() {
		return Utils.parsePrice(price.getValue().toString());
	}
	
	public boolean getFdpin(){
		return priceOptionsSelection.isSelected("IN");
	}

	public void setPrice(BigDecimal newPrice) {
		this.price.setValue(newPrice);
	}

	public void setPriceOptionsSelection(Boolean fdpinValue) {
		if(fdpinValue==true){
			this.priceOptionsSelection.select("IN");
		}else{
			this.priceOptionsSelection.select("OUT");
		}
	}
	
	
}
