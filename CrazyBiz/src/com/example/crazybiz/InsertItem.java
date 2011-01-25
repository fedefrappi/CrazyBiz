package com.example.crazybiz;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class InsertItem extends VerticalLayout implements ValueChangeListener{
	private String username;
	private ComboBox brand = new ComboBox("Brand");
	private ComboBox model = new ComboBox("Brand");
	private TextField source;
	
	public InsertItem(String username) {
		this.username = username;
		init();
	}
	
	private void init() {
		// Sets the combobox to show a certain property as the item caption
		//brand.setItemCaptionPropertyId(ExampleUtil.iso3166_PROPERTY_NAME);
		brand.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		// Sets the icon to use with the items
		//brand.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);
		brand.setWidth("100px");
		// Set the appropriate filtering mode for this example
		brand.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		brand.setImmediate(true);
		brand.addListener(this);
		// Disallow null selections
		brand.setNullSelectionAllowed(false);
		
		// Sets the combobox to show a certain property as the item caption
		//brand.setItemCaptionPropertyId(ExampleUtil.iso3166_PROPERTY_NAME);
		model.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
		// Sets the icon to use with the items
		//brand.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);
		model.setWidth("150px");
		// Set the appropriate filtering mode for this example
		model.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		model.setImmediate(true);
		model.addListener(this);
		// Disallow null selections
		model.setNullSelectionAllowed(false);
		
		source = new TextField("Source");
		
		ImageUpload iu = new ImageUpload();
		
		
		this.addComponent(brand);
		this.addComponent(model);
		this.addComponent(source);
		this.addComponent(iu);
		
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}