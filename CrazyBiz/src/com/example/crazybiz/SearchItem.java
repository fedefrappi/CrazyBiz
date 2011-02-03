package com.example.crazybiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp.DbcpException;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

import db.DBactions;
import db.MyResult;
import db.ResultContainer;

public class SearchItem extends VerticalLayout {
	private CrazybizApplication crazybizApplication;
	private HorizontalLayout searchOptionsLayout;
	private Button searchAll;
	private Button searchBrand;
	private Button searchModel;
	private Button searchStatus;
	private Button searchCustomQuery;
	private Window subwindow;
	private TextField filter;
	private Table table;
	private Button backButton;
	private String queryString;
	private VerticalLayout detailsLayout;
	private TextArea detailsTextArea;
	private Button editItem;
	private int itemID;
	
	
	public SearchItem(final CrazybizApplication crazybizApplication, final String username) {
		this.crazybizApplication = crazybizApplication;
		setMargin(true);
		setSpacing(true);
		
		itemID = -1;
		
		// Search options
		searchOptionsLayout = new HorizontalLayout();
		searchOptionsLayout.setSpacing(true);
		filter = new TextField();
		searchAll = new Button("all");
		searchBrand = new Button("by brand");
		searchModel = new Button("by model");
		searchStatus = new Button("by status");
		searchCustomQuery = new Button("custom");
		
		subwindow = new Window("A modal subwindow");
	    subwindow.setModal(true);
        // Configure the modal window layout and components
        VerticalLayout layout = (VerticalLayout) subwindow.getContent();
        layout.setMargin(true);
        layout.setSpacing(true);
        TextField customQuery = new TextField("This is a modal subwindow.");
        subwindow.addComponent(customQuery);
        Button closeSubwindow = new Button("Close", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                (subwindow.getParent()).removeWindow(subwindow);
            }
        });
        layout.addComponent(closeSubwindow);
        layout.setComponentAlignment(closeSubwindow, Alignment.TOP_RIGHT);
	    
		searchAll.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
		        queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
        		"FROM brand,model,item " +
        		"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
        		"ORDER BY item.lastModified DESC";
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
				"AND brand.brand_name = '"+filter.getValue().toString()+"' " +
				"ORDER BY item.lastModified DESC";
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
				"AND model.model_name = '"+filter.getValue().toString()+"' " +
				"ORDER BY item.lastModified DESC";
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
				"AND item.status = '"+filter.getValue().toString()+"' " + 
				"ORDER BY item.lastModified DESC";
		        table.removeAllItems();
				table.setContainerDataSource(ResultContainer.create(queryString));
			}
		});
		searchCustomQuery.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(subwindow.getParent()==null){
					// INIT MODAL WINDOW
					subwindow = new Window("A modal subwindow");
					subwindow.setWidth("500px");
					subwindow.setHeight("260px");
				    subwindow.setModal(true);
			        // Configure the modal window layout and components
			        VerticalLayout layout = (VerticalLayout) subwindow.getContent();
			        layout.setMargin(true);
			        layout.setSpacing(true);
			        final TextField customQuery = new TextField("This is a modal subwindow.");
			        customQuery.setSizeFull();
			        customQuery.setHeight("180px");
			        subwindow.addComponent(customQuery);
			        Button cancel = new Button("Cancel", new Button.ClickListener() {
			            public void buttonClick(ClickEvent event) {
			                (subwindow.getParent()).removeWindow(subwindow);
			            }
			        });
			        Button confirmQuery = new Button("Confirm", new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							queryString = customQuery.getValue().toString();
							table.removeAllItems();
							table.setContainerDataSource(ResultContainer.create(queryString));
							(subwindow.getParent()).removeWindow(subwindow);
						}
					});
			        HorizontalLayout buttonsLayout = new HorizontalLayout();
			        buttonsLayout.addComponent(confirmQuery);
			        buttonsLayout.addComponent(cancel);
			        layout.addComponent(buttonsLayout);
			        // SHOW MODAL WINDOW
					getWindow().addWindow(subwindow);
				}
			}
		});
		searchOptionsLayout.addComponent(searchAll);
		searchOptionsLayout.addComponent(searchBrand);
		searchOptionsLayout.addComponent(searchModel);
		searchOptionsLayout.addComponent(searchStatus);
		searchOptionsLayout.addComponent(searchCustomQuery);
		searchOptionsLayout.addComponent(filter);
		
		detailsLayout = new VerticalLayout();
		detailsLayout.setSizeFull();
		detailsTextArea = new TextArea();
		detailsTextArea.setWidth("500px");
		detailsTextArea.setHeight("150px");
		detailsLayout.addComponent(detailsTextArea);
		detailsLayout.setComponentAlignment(detailsTextArea, Alignment.MIDDLE_CENTER);
		
		table = new Table("Results");
		table.setWidth("500px");
		table.setSelectable(true);
        table.setMultiSelect(false);
        table.setImmediate(true);
        table.setColumnReorderingAllowed(false);
        table.setColumnCollapsingAllowed(true);
        
        table.addContainerProperty("ItemID", Integer.class, null);
        table.addContainerProperty("Brand", String.class, null);
        table.addContainerProperty("Model", String.class, null);
        table.addContainerProperty("Status", String.class, null);
                
        // Populate from db with Default query
        queryString = "SELECT item.item_id,brand.brand_name,model.model_name,item.status " +
        		"FROM brand,model,item " +
        		"WHERE item.model_id = model.model_id AND model.brand_id = brand.brand_id " +
        		"ORDER BY item.lastModified DESC";
        table.setContainerDataSource(ResultContainer.create(queryString));
        table.setVisibleColumns(new String[]{"brand","model","status"});

        
        // Action handler
        table.addListener(new Table.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
               // in multiselect mode, a Set of itemIds is returned,
                // in singleselect mode the itemId is returned directly
                MyResult result = (MyResult)event.getProperty().getValue();
                if (result != null) {
                    // Get details to show
                	itemID = result.getItemID();
                    String text = "";
                    try {
                    	/*
                    	PreparedStatement stm = DBactions.conn.prepareStatement(
                    			"SELECT brand.brand_name,model.model_name,item.status,buy.price,buy.date,buy.name,sell.price,sell.date,sell.buyer,proposal.price " +
                    			"FROM brand,model,item,buy,sell,proposal " +
                    			"WHERE item.item_id=? AND brand.brand_id=model.brand_id AND item.model_id=model.model_id AND buy.item_id=? AND sell.item_id=? AND proposal.item_id=? " +
                    			"ORDER BY proposal.price DESC;");
                    	stm.setInt(1, itemID);
                    	stm.setInt(2, itemID);
                    	stm.setInt(3, itemID);
                    	stm.setInt(4, itemID);
						ResultSet rs = stm.executeQuery();
						if(rs.next()){
							text = "Brand:\t\t\t\t\t" + rs.getString(1) + "\n" +
								   "Model:\t\t\t\t\t" + rs.getString(2) + "\n" +
								   "Status:\t\t\t\t\t" + rs.getString(3) + "\n" +
								   "Bougth:\t\t\t\t\t" + rs.getInt(4) + " €" + " on " + rs.getDate(5) + " by " + rs.getString(6) + "\n" +
								   "Sold:\t\t\t\t\t" + rs.getInt(7) + " €" + " on " + rs.getDate(8) + " by " + rs.getString(9) + "\n" +
								   "Best proposal:\t\t\t\t" + rs.getInt(10) + " €\n\n" +
								   "Item ID:\t\t\t\t\t" + itemID;
						}
						*/
                    	PreparedStatement stm = DBactions.conn.prepareStatement(
                    			"SELECT brand.brand_name,model.model_name,item.status " +
                    			"FROM brand,model,item " +
                    			"WHERE item.item_id=? AND model.model_id=item.model_id AND brand.brand_id=model.brand_id;");
                    	stm.setInt(1, itemID);
                    	ResultSet rs = stm.executeQuery();
                    	if(rs.next()){
                    		text = text + 
                    			"Brand:\t\t\t\t\t" + rs.getString(1) + "\n" +
                    			"Model:\t\t\t\t\t" + rs.getString(2) + "\n" +
                    			"Status:\t\t\t\t\t" + rs.getString(3) + "\n";
                    	}
                    	stm = DBactions.conn.prepareStatement(
                    			"SELECT buy.price,buy.date,buy.name " +
                    			"FROM buy " +
                    			"WHERE buy.item_id=?;");
                    	stm.setInt(1, itemID);
                    	rs = stm.executeQuery();
                    	if(rs.next()){
                    		text = text + 
                    			"Bougth:\t\t\t\t\t" + rs.getBigDecimal(1) + " €" + " on " + rs.getDate(2) + " by " + rs.getString(3) + "\n";
                    	}
                    	stm = DBactions.conn.prepareStatement(
                    			"SELECT sell.price,sell.date,sell.buyer " +
                    			"FROM sell " +
                    			"WHERE sell.item_id=?;");
                    	stm.setInt(1, itemID);
                    	rs = stm.executeQuery();
                    	if(rs.next()){
                    		text = text + 
                    			"Sold:\t\t\t\t\t" + rs.getBigDecimal(1) + " €" + " on " + rs.getDate(2) + " by " + rs.getString(3) + "\n";
                    	}
                    	stm = DBactions.conn.prepareStatement(
                    			"SELECT proposal.price " +
                    			"FROM proposal " +
                    			"WHERE proposal.item_id=? " +
                    			"ORDER BY proposal.price DESC;");
                    	stm.setInt(1, itemID);
                    	rs = stm.executeQuery();
                    	if(rs.next()){
                    		text = text + 
                    			"Best proposal:\t\t\t\t" + rs.getInt(10) + " €\n\n";
                    	}
                    	text = text + "Item ID:\t\t\t\t\t" + itemID;
					} catch (SQLException e) {
						e.printStackTrace();
					}
                    // Show details
					detailsTextArea.setValue(text);
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
		
		editItem = new Button("Edit item");
		editItem.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				crazybizApplication.setInsert(new InsertItem(crazybizApplication, username, itemID));
				crazybizApplication.getWindow().setContent(crazybizApplication.getInsert());
			}
		});
		
		addComponent(backLayout);
		addComponent(searchOptionsLayout);
		addComponent(table);
		detailsLayout.addComponent(editItem);
		addComponent(detailsLayout);
		
		this.setComponentAlignment(backLayout, Alignment.TOP_LEFT);
		this.setComponentAlignment(searchOptionsLayout, Alignment.TOP_CENTER);
		this.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
	}
}
