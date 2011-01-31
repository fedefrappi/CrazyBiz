package com.example.crazybiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

import dao.DBactions;

public class SearchItem extends VerticalLayout {
	private CrazybizApplication crazybizApplication;
	private HorizontalLayout searchOptionsLayout;
	private Button searchAll;
	private Button searchBrand;
	private Button searchModel;
	private Button searchStatus;
	private TextField filter;
	private Table table;
	private Label selected;
	private Button backButton;
	private String queryString;
	
	public SearchItem(final CrazybizApplication crazybizApplication, final String username) {
		this.crazybizApplication = crazybizApplication;
		
		// Search options
		searchOptionsLayout = new HorizontalLayout();
		searchOptionsLayout.setSpacing(true);
		filter = new TextField();
		searchAll = new Button("All");
		searchBrand = new Button("by brand");
		searchModel = new Button("by model");
		searchStatus = new Button("by status");
		searchAll.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
        		"FROM brand,model,item " +
        		"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id";
		        table.removeAllItems();
				table.setContainerDataSource(ResultContainer.create(queryString));
			}
		});
		searchBrand.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
				"FROM brand,model,item " +
				"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
				"AND brand.brand_name = '"+filter.getValue().toString()+"'";
		        table.removeAllItems();
				table.setContainerDataSource(ResultContainer.create(queryString));
			}
		});
		searchModel.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
				"FROM brand,model,item " +
				"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
				"AND model.model_name = '"+filter.getValue().toString()+"'";
		        table.removeAllItems();
				table.setContainerDataSource(ResultContainer.create(queryString));
			}
		});
		searchStatus.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
				"FROM brand,model,item " +
				"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
				"AND item.status = '"+filter.getValue().toString()+"'";
		        table.removeAllItems();
				table.setContainerDataSource(ResultContainer.create(queryString));
			}
		});
		searchOptionsLayout.addComponent(searchAll);
		searchOptionsLayout.addComponent(searchBrand);
		searchOptionsLayout.addComponent(searchModel);
		searchOptionsLayout.addComponent(searchStatus);
		searchOptionsLayout.addComponent(filter);
		
		selected = new Label("");
		table = new Table("Results");
		table.setWidth("500px");
		table.setSelectable(true);
        table.setMultiSelect(false);
        table.setImmediate(true);
        // turn on column reordering and collapsing
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);        
        
        // Populate from db with Default query
        queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
        		"FROM brand,model,item " +
        		"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id";
        table.setContainerDataSource(ResultContainer.create(queryString));

        // Action handler
        table.addListener(new Table.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
               // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                MyResult result = (MyResult)event.getProperty().getValue();
                if (result == null) {
                    selected.setValue("No selection");
                } else {
                    selected.setValue("Selected: " + result.getItemID());
                    // Scatena qualcosa //
                }
			}
		});
        
		// Back button layout
		HorizontalLayout backLayout = new HorizontalLayout();
		backLayout.setMargin(false,true,false,false);
		backButton = new Button("Back");
		backButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeAllComponents();
				crazybizApplication.setHome(new Homepage(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getHome());
			}
		});
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backLayout.addComponent(backButton);
		
		addComponent(backLayout);
		addComponent(searchOptionsLayout);
		addComponent(table);
		addComponent(selected);
		
		this.setComponentAlignment(backLayout, Alignment.TOP_LEFT);
		this.setComponentAlignment(searchOptionsLayout, Alignment.TOP_CENTER);
		this.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		this.setComponentAlignment(selected, Alignment.MIDDLE_CENTER);



	}
}
