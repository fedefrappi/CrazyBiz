package com.example.crazybiz;

import java.util.Arrays;
import java.util.List;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class StatusCheckBox extends VerticalLayout implements Property.ValueChangeListener{

	private static final List<String> statuses = Arrays.asList(new String[] {
            "Watching", "Bought", "Shipped", "On sale", "Sold" });
	private OptionGroup statusSelect;
	
	public StatusCheckBox() {
		// Init
		setSpacing(true);
		statusSelect = new OptionGroup("Item status:", statuses);
		statusSelect.setMultiSelect(true);
        statusSelect.setNullSelectionAllowed(true); // user can not 'unselect'
        statusSelect.setImmediate(true); // send the change to the server at once

		// Set status from db
		
		// Add to the layout
        addComponent(statusSelect);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
