package com.example.crazybiz;

import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WatchingPanel extends VerticalLayout{

	private TextField price;
	private static final List<String> priceOptions = Arrays.asList(new String[] {"IN", "OUT"});
	private OptionGroup priceOptionsSelection;
	
	public WatchingPanel() {
		price = new TextField("Price");

		priceOptionsSelection = new OptionGroup("FDP", priceOptions);
		priceOptionsSelection.setNullSelectionAllowed(false);
        priceOptionsSelection.select("OUT"); // select this by default
        priceOptionsSelection.setImmediate(true); // send the change to the server at once
        
        addComponent(price);
        addComponent(priceOptionsSelection);
	}
}
