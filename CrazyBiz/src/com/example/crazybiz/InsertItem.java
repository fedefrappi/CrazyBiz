package com.example.crazybiz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import panels.BoughtPanel;
import panels.OnSalePanel;
import panels.ShippedPanel;
import panels.SoldPanel;
import panels.WatchingPanel;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import db.DBactions;
import domain.Brand;
import domain.Model;

public class InsertItem extends GridLayout{
	private CrazybizApplication crazybizApplication;
	private String username;
	private ComboBox brand = new ComboBox("Brand");
	private ComboBox model = new ComboBox("Model");
	private TextField source;
	private StatusManager status;
	private Button backButton;
	private Button saveButton;
	private VerticalLayout leftLayout;
	
	// Right Panels
	private WatchingPanel wp;
	private BoughtPanel bp;
	private ShippedPanel shp;
	private OnSalePanel op;
	private SoldPanel sop;
	
	// DB variables
	private int brandID;
	private int modelID;
	private int itemID;
	private int watchingID;
	private int buyID;
	private int shippingID;
	private ArrayList<Integer> proposalIDs;
	private ArrayList<Integer> postIDs;
	private int soldID;

	
	public InsertItem(final CrazybizApplication crazybizApplication, final String username) {
		super(2,1);
		this.crazybizApplication = crazybizApplication;
		this.username = username;
		
		// Default value: -1 , object not stored in the db.
		brandID = -1;
		modelID = -1;
		itemID = -1;
		watchingID = -1;
		buyID = -1;
		shippingID = -1;
		proposalIDs = new ArrayList<Integer>();
		postIDs = new ArrayList<Integer>();
		soldID = -1;
		
		init();
		
		// backButton listener: back to the homepage
		backButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeAllComponents();
				crazybizApplication.setHome(new Homepage(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getHome());
			}
		});
	}
	
	public InsertItem(final CrazybizApplication crazybizApplication, final String username, int itemID){
		// Constructor to use when coming from Search
		super(2,1);
		this.crazybizApplication = crazybizApplication;
		this.username = username;
		// Default value: -1 , object not stored in the db.
		brandID = -1;
		modelID = -1;
		watchingID = -1;
		buyID = -1;
		shippingID = -1;
		proposalIDs = new ArrayList<Integer>();
		postIDs = new ArrayList<Integer>();
		soldID = -1;
		
		this.itemID = itemID;
		init();
		
		// backButton listener: back to the search view
		backButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeAllComponents();
				crazybizApplication.setSearch(new SearchItem(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getSearch());
			}
		});
		
		// Fill all the fields
		try {
			// Overview
			PreparedStatement stm = DBactions.conn.prepareStatement(
					"SELECT brand.brand_name,model.model_name,item.source,brand.brand_id,model.model_id " +
					"FROM item,model,brand " +
					"WHERE item.item_id=? AND item.model_id=model.model_id AND model.brand_id=brand.brand_id;");
			stm.setInt(1, itemID);
			ResultSet rs = stm.executeQuery();
			if(rs.next()){
				brand.setValue(rs.getString(1));
				model.setValue(rs.getString(2));
				source.setValue(rs.getString(3));
				this.brandID = rs.getInt(4);
				this.modelID = rs.getInt(5);
			}
			// Watching panel
			stm = DBactions.conn.prepareStatement(
					"SELECT price,fdpin,id " +
					"FROM watched " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			if(rs.next()){
				wp.setPrice(rs.getBigDecimal(1));
				wp.setPriceOptionsSelection(rs.getBoolean(2));
				status.getWatchingCheckbox().setValue(true);
				status.getWatchingButton().setEnabled(true);
				this.watchingID = rs.getInt(3);
			}
			// Buy panel
			stm = DBactions.conn.prepareStatement(
					"SELECT price,name,phone,email,country,city,date,id " +
					"FROM buy " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			if(rs.next()){
				bp.setPrice(rs.getBigDecimal(1));
				bp.setSellerName(rs.getString(2));
				bp.setSellerPhone(rs.getString(3));
				bp.setSellerEmail(rs.getString(4));
				bp.setSellerCountry(rs.getString(5));
				bp.setSellerCity(rs.getString(6));
				bp.setDate(rs.getDate(7));
				status.getBoughtCheckbox().setValue(true);
				status.getBoughtButton().setEnabled(true);
				this.buyID = rs.getInt(8);
			}

			// Shipping panel
			stm = DBactions.conn.prepareStatement(
					"SELECT tracking,recipient,company,id " +
					"FROM shipping " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			if(rs.next()){
				shp.setTracking(rs.getString(1));
				shp.setRecipient(rs.getString(2));
				shp.setCompany(rs.getString(3));
				status.getShippedCheckbox().setValue(true);
				status.getShippedButton().setEnabled(true);
				this.shippingID = rs.getInt(4);
			}
			// Posts
			int existingPosts = 0;
			stm = DBactions.conn.prepareStatement(
					"SELECT price,source,message,date,id " +
					"FROM post " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			while(rs.next()){
				PostEntryComponent tmp = new PostEntryComponent();
				tmp.setPrice(rs.getBigDecimal(1));
				tmp.setSource(rs.getString(2));
				tmp.setMessage(rs.getString(3));
				tmp.setDate(rs.getDate(4));
				postIDs.add(rs.getInt(5));
				op.getPostComponent().addEntry(tmp);
				existingPosts++;
			}
			// Proposals
			int existingProposals = 0;
			stm = DBactions.conn.prepareStatement(
					"SELECT price,user,message,date,id " +
					"FROM proposal " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			while(rs.next()){
				ProposalEntryComponent tmp = new ProposalEntryComponent();
				tmp.setPrice(rs.getBigDecimal(1));
				tmp.setUser(rs.getString(2));
				tmp.setMessage(rs.getString(3));
				tmp.setDate(rs.getDate(4));
				proposalIDs.add(rs.getInt(5));
				op.getProposalComponent().addEntry(tmp);
				existingProposals++;
			}

			if(existingPosts>0 || existingProposals>0){
				status.getOnsaleCheckbox().setValue(true);
				status.getOnsaleButton().setEnabled(true);
			}
			// Sold panel
			stm = DBactions.conn.prepareStatement(
					"SELECT price,date,buyer,id " +
					"FROM sell " +
					"WHERE item_id=?;");
			stm.setInt(1, itemID);
			rs = stm.executeQuery();
			if(rs.next()){
				sop.setPrice(rs.getBigDecimal(1));
				sop.setDate(rs.getDate(2));
				sop.setBuyer(rs.getString(3));
				status.getSoldCheckbox().setValue(true);
				status.getSoldButton().setEnabled(true);
				this.soldID = rs.getInt(4);
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	private void init() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		// Left layout
		leftLayout = new VerticalLayout();
		
		// Panels
		wp = new WatchingPanel();
		bp = new BoughtPanel();
		shp = new ShippedPanel();
		op = new OnSalePanel();
		sop = new SoldPanel();
		
		// Back button layout
		HorizontalLayout backLayout = new HorizontalLayout();
		backLayout.setMargin(false,true,false,false);
		backButton = new Button("Back");
		
	// Custom listener in constructors //-------------------------------
		
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backLayout.addComponent(backButton);

		List<Brand> brandList = new ArrayList<Brand>();
		try {
			brandList = DBactions.getBrands();
		} catch (SQLException e1) {e1.printStackTrace();}
		for(Brand currentBrand : brandList){
			brand.addItem(currentBrand.getName());			
		}
		brand.setWidth("100px");
		brand.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		brand.setImmediate(true);
		brand.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				String selectedBrand = event.getProperty().toString();
				List<Model> filteredModels;
				try {
					filteredModels = DBactions.getModels(selectedBrand);
					model.removeAllItems();
					for(Model currentModel : filteredModels){
						model.addItem(currentModel.getName());			
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		brand.setNullSelectionAllowed(false);
		brand.setNewItemsAllowed(true);
		brand.setNewItemHandler(new NewItemHandler() {
			public void addNewItem(String newItemCaption) {
				brand.addItem(newItemCaption);
				brand.setValue(newItemCaption);
			}
		});
		
		model.setWidth("150px");
		model.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		model.setImmediate(true);
		model.setNullSelectionAllowed(false);
		model.setNewItemsAllowed(true);
		model.setNewItemHandler(new NewItemHandler() {
			public void addNewItem(String newItemCaption) {
				model.addItem(newItemCaption);
				model.setValue(newItemCaption);
			}
		});

		source = new TextField("Source");
		source.setWidth("200px");
		
		status = new StatusManager(this);

		saveButton = new Button("Save item");
		saveButton.addListener(new ClickListener() {		
			public void buttonClick(ClickEvent event) {
				try {
					executeQuery();
				} catch (SQLException e) {e.printStackTrace();}
			}
		});
		
		leftLayout.addComponent(backLayout);
        setComponentAlignment(backLayout, Alignment.TOP_LEFT);
		leftLayout.addComponent(brand);
		leftLayout.addComponent(model);
		leftLayout.addComponent(source);
		leftLayout.addComponent(status);
		leftLayout.addComponent(saveButton);
		
		this.addComponent(leftLayout,0,0);
	}

	public void showWatchingPanel(){
		this.removeComponent(1,0);
		if(wp == null){
			wp = new WatchingPanel();
		}
		this.addComponent(wp,1,0);
	}
	public void showBoughtPanel(){
		this.removeComponent(1,0);
		if(bp == null){
			bp = new BoughtPanel();
		}
		this.addComponent(bp,1,0);
	}
	public void showShippedPanel(){
		this.removeComponent(1,0);
		if(shp == null){
			shp = new ShippedPanel();
		}
		this.addComponent(shp,1,0);
	}
	public void showOnSalePanel(){
		this.removeComponent(1,0);
		if(op == null){
			op = new OnSalePanel();
		}
		this.addComponent(op,1,0);
	}
	public void showSoldPanel(){
		this.removeComponent(1,0);
		if(sop == null){
			sop = new SoldPanel();
		}
		this.addComponent(sop,1,0);
	}
	
	protected void executeQuery() throws SQLException{
		PreparedStatement stm = null;
		ResultSet res = null;
		
		// BRAND - OK
		// Check if selected brand is in the db
		stm = DBactions.conn.prepareStatement("SELECT brand_id FROM brand WHERE brand_name=?");
		stm.setString(1, brand.getValue().toString());
		ResultSet rs = stm.executeQuery();
		if(rs.next()){
			brandID = rs.getInt(1);
		}
		if(brandID==-1){
			// Insert brand
			try {
				stm = DBactions.conn.prepareStatement("INSERT INTO brand(brand_name,brand_website) VALUES(?,?);",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				stm.setString(1, brand.getValue().toString());
				stm.setString(2, "unknown");
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					brandID = keys.getInt(1);
				}
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else{
			// Update brand
			try {
				stm = DBactions.conn.prepareStatement("UPDATE brand SET brand_name=? WHERE brand_id=?;");
				stm.clearParameters();
				stm.setString(1, brand.getValue().toString());
				stm.setInt(2, brandID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		//MODEL - OK
		// Check if selected model is in the db
		stm = DBactions.conn.prepareStatement("SELECT model_id FROM model,brand WHERE model_name=? AND model.brand_id=?");
		stm.setString(1, model.getValue().toString());
		stm.setInt(2, brandID);
		rs = stm.executeQuery();
		if(rs.next()){
			modelID = rs.getInt(1);
		}
		if(modelID==-1){
			// Insert model
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO model(brand_id,model_name,model_price) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				stm.setInt(1, brandID);
				stm.setString(2, model.getValue().toString());
				stm.setBigDecimal(3, BigDecimal.valueOf(0.0));
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					modelID = keys.getInt(1);
				}
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else{
			// Update model
			try {
				stm = DBactions.conn.prepareStatement("UPDATE model SET model_name=? WHERE model_id=?;");
				stm.clearParameters();
				stm.setString(1, model.getValue().toString());
				stm.setInt(2, modelID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		// ITEM - OK
		if(itemID==-1){
			// Insert item
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO item(source,model_id,status,lastModified) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				if(source.getValue() == null){
					stm.setNull(1, Types.VARCHAR);
				}else{
					stm.setString(1, source.getValue().toString());
				}
				stm.setInt(2, modelID);
				stm.setString(3, "disabled");
				stm.setDate(4, new Date(System.currentTimeMillis()));
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					itemID = keys.getInt(1);
				}
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else{
			// Update item
			try {
				stm = DBactions.conn.prepareStatement("UPDATE item SET source=?,lastModified=? WHERE item_id=?;");
				stm.clearParameters();
				if(source.getValue() == null){
					stm.setNull(1, Types.VARCHAR);
				}else{
					stm.setString(1, source.getValue().toString());
				}
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		// WATCHING - OK
		if(status.getWatchingCheckbox().booleanValue() && watchingID == -1){
			// Insert watching
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO watched(price,fdpin,item_id) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				if(wp.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, wp.getPrice());
				}
				stm.setBoolean(2, wp.getFdpin());
				stm.setInt(3, itemID);
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					watchingID = keys.getInt(1);
				}
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "watching");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else if(status.getWatchingCheckbox().booleanValue()){
			// Update watching
			try {
				stm = DBactions.conn.prepareStatement("UPDATE watched SET price=?,fdpin=? WHERE id=?;");
				stm.clearParameters();
				if(wp.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, wp.getPrice());
				}
				stm.setBoolean(2, wp.getFdpin());
				stm.setInt(3, watchingID);
				stm.executeUpdate();

				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "watching");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		// BUY
		if(status.getBoughtCheckbox().booleanValue() && buyID == -1){
			// Insert buy
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO buy(price,name,phone,email,country,city,item_id,date) VALUES(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				if(bp.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, bp.getPrice());
				}
				if(bp.getSellerName() == null){
					stm.setNull(2, Types.VARCHAR);
				}else{
					stm.setString(2, bp.getSellerName());
				}
				if(bp.getSellerPhone() == null){
					stm.setNull(3, Types.VARCHAR);
				}else{
					stm.setString(3, bp.getSellerPhone());
				}
				if(bp.getSellerEmail() == null){
					stm.setNull(4, Types.VARCHAR);
				}else{
					stm.setString(4, bp.getSellerEmail());
				}
				if(bp.getSellerCountry() == null){
					stm.setNull(5, Types.VARCHAR);
				}else{
					stm.setString(5, bp.getSellerCountry());
				}
				if(bp.getSellerCity() == null){
					stm.setNull(6, Types.VARCHAR);
				}else{
					stm.setString(6, bp.getSellerCity());
				}
				stm.setInt(7, itemID);
				stm.setDate(8, new Date(bp.getDate().getTime()));
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					buyID = keys.getInt(1);
				}
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "bought");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else if(status.getBoughtCheckbox().booleanValue()){
			// Update buy
			try {
				stm = DBactions.conn.prepareStatement("UPDATE buy SET price=?,name=?,phone=?,email=?,country=?,city=?,date=? WHERE id=?;");
				if(bp.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, bp.getPrice());
				}
				if(bp.getSellerName() == null){
					stm.setNull(2, Types.VARCHAR);
				}else{
					stm.setString(2, bp.getSellerName());
				}
				if(bp.getSellerPhone() == null){
					stm.setNull(3, Types.VARCHAR);
				}else{
					stm.setString(3, bp.getSellerPhone());
				}
				if(bp.getSellerEmail() == null){
					stm.setNull(4, Types.VARCHAR);
				}else{
					stm.setString(4, bp.getSellerEmail());
				}
				if(bp.getSellerCountry() == null){
					stm.setNull(5, Types.VARCHAR);
				}else{
					stm.setString(5, bp.getSellerCountry());
				}
				if(bp.getSellerCity() == null){
					stm.setNull(6, Types.VARCHAR);
				}else{
					stm.setString(6, bp.getSellerCity());
				}
				stm.setDate(7, new Date(bp.getDate().getTime()));
				stm.setInt(8, buyID);
				stm.executeUpdate();
				
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "bought");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
				
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		// SHIPPING
		if(status.getShippedCheckbox().booleanValue() && shippingID == -1){
			// Insert shipping
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO shipping(tracking,recipient,company,item_id) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				if(shp.getTracking() == null){
					stm.setNull(1, Types.VARCHAR);
				}else{
					stm.setString(1, shp.getTracking());
				}
				if(shp.getRecipient() == null){
					stm.setNull(2, Types.VARCHAR);
				}else{
					stm.setString(2, shp.getRecipient());
				}
				if(shp.getCompany() == null){
					stm.setNull(3, Types.VARCHAR);
				}else{
					stm.setString(3, shp.getCompany());
				}
				stm.setInt(4, itemID);
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					shippingID = keys.getInt(1);
				}
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "shipped");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else if(status.getShippedCheckbox().booleanValue()){
			// Update shipping
			try {
				stm = DBactions.conn.prepareStatement("UPDATE shipping SET tracking=?,recipient=?,company=? WHERE id=?;");
				if(shp.getTracking() == null){
					stm.setNull(1, Types.VARCHAR);
				}else{
					stm.setString(1, shp.getTracking());
				}
				if(shp.getRecipient() == null){
					stm.setNull(2, Types.VARCHAR);
				}else{
					stm.setString(2, shp.getRecipient());
				}
				if(shp.getCompany() == null){
					stm.setNull(3, Types.VARCHAR);
				}else{
					stm.setString(3, shp.getCompany());
				}
				stm.setInt(4, shippingID);
				stm.executeUpdate();
				
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "shipped");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		// ON SALE
		if(status.getOnsaleCheckbox().booleanValue()){
			// Insert on sale
			for(int k=0; k<op.getProposalComponent().getEntries().size(); k++){
				if(k>=proposalIDs.size()){
					proposalIDs.add(-1);
				}
				if(proposalIDs.get(k) == -1){
					try{
						stm = 
							DBactions.conn.prepareStatement("INSERT INTO proposal(price,user,message,item_id,date) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
						stm.clearParameters();
						if(op.getProposalComponent().getEntries().get(k).getPrice() == null){
							stm.setNull(1, Types.VARCHAR);
						}else{
							stm.setBigDecimal(1, op.getProposalComponent().getEntries().get(k).getPrice());
						}
						if(op.getProposalComponent().getEntries().get(k).getUser() == null){
							stm.setNull(2, Types.VARCHAR);
						}else{
							stm.setString(2, op.getProposalComponent().getEntries().get(k).getUser());
						}
						if(op.getProposalComponent().getEntries().get(k).getMessage() == null){
							stm.setNull(3, Types.VARCHAR);
						}else{
							stm.setString(3, op.getProposalComponent().getEntries().get(k).getMessage());
						}
						stm.setInt(4, itemID);
						stm.setDate(5,new Date(op.getProposalComponent().getEntries().get(k).getDate().getTime()));
						stm.executeUpdate();
						ResultSet keys = stm.getGeneratedKeys();
						if(keys.next()){
							proposalIDs.set(k, keys.getInt(1));
						}
						
						// Update item status
						stm = 
							DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
						stm.clearParameters();
						stm.setString(1, "onSale");
						stm.setDate(2, new Date(System.currentTimeMillis()));
						stm.setInt(3, itemID);
						stm.executeUpdate();
					} catch (MySQLIntegrityConstraintViolationException e) {}
				}else{
					// Update proposal
					try {
						stm = DBactions.conn.prepareStatement("UPDATE proposal SET price=?,user=?,message=?,date=? WHERE id=?;");
						stm.clearParameters();
						if(op.getProposalComponent().getEntries().get(k).getPrice() == null){
							stm.setNull(1, Types.VARCHAR);
						}else{
							stm.setBigDecimal(1, op.getProposalComponent().getEntries().get(k).getPrice());
						}
						if(op.getProposalComponent().getEntries().get(k).getUser() == null){
							stm.setNull(2, Types.VARCHAR);
						}else{
							stm.setString(2, op.getProposalComponent().getEntries().get(k).getUser());
						}
						if(op.getProposalComponent().getEntries().get(k).getMessage() == null){
							stm.setNull(3, Types.VARCHAR);
						}else{
							stm.setString(3, op.getProposalComponent().getEntries().get(k).getMessage());
						}
						stm.setDate(4,new Date(op.getProposalComponent().getEntries().get(k).getDate().getTime()));
						stm.setInt(5, proposalIDs.get(k));
						stm.executeUpdate();
						
						// Update item status
						stm = 
							DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
						stm.clearParameters();
						stm.setString(1, "onSale");
						stm.setDate(2, new Date(System.currentTimeMillis()));
						stm.setInt(3, itemID);
						stm.executeUpdate();
					} catch (MySQLIntegrityConstraintViolationException e) {}
				}
			}
			for(int k=0; k<op.getPostComponent().getEntries().size(); k++){
				if(k>=postIDs.size()){
					postIDs.add(-1);
				}
				if(postIDs.get(k) == -1){
					try{
						stm = 
							DBactions.conn.prepareStatement("INSERT INTO post(price,source,message,item_id,date) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
						stm.clearParameters();
						if(op.getPostComponent().getEntries().get(k).getPrice() == null){
							stm.setNull(1, Types.VARCHAR);
						}else{
							stm.setBigDecimal(1, op.getPostComponent().getEntries().get(k).getPrice());
						}
						if(op.getPostComponent().getEntries().get(k).getSource() == null){
							stm.setNull(2, Types.VARCHAR);
						}else{
							stm.setString(2, op.getPostComponent().getEntries().get(k).getSource());
						}
						if(op.getPostComponent().getEntries().get(k).getMessage() == null){
							stm.setNull(3, Types.VARCHAR);
						}else{
							stm.setString(3, op.getPostComponent().getEntries().get(k).getMessage());
						}
						stm.setInt(4, itemID);
						stm.setDate(5,new Date(op.getPostComponent().getEntries().get(k).getDate().getTime()));
						stm.executeUpdate();
						ResultSet keys = stm.getGeneratedKeys();
						if(keys.next()){
							postIDs.set(k, keys.getInt(1));
						}
						
						// Update item status
						stm = 
							DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
						stm.clearParameters();
						stm.setString(1, "onSale");
						stm.setDate(2, new Date(System.currentTimeMillis()));
						stm.setInt(3, itemID);
						stm.executeUpdate();
					} catch (MySQLIntegrityConstraintViolationException e) {}
				}
				else{
					// Update post
					try {
						stm = DBactions.conn.prepareStatement("UPDATE post SET price=?,source=?,message=?,date=? WHERE id=?;");
						stm.clearParameters();
						if(op.getPostComponent().getEntries().get(k).getPrice() == null){
							stm.setNull(1, Types.VARCHAR);
						}else{
							stm.setBigDecimal(1, op.getPostComponent().getEntries().get(k).getPrice());
						}
						if(op.getPostComponent().getEntries().get(k).getSource() == null){
							stm.setNull(2, Types.VARCHAR);
						}else{
							stm.setString(2, op.getPostComponent().getEntries().get(k).getSource());
						}
						if(op.getPostComponent().getEntries().get(k).getMessage() == null){
							stm.setNull(3, Types.VARCHAR);
						}else{
							stm.setString(3, op.getPostComponent().getEntries().get(k).getMessage());
						}
						stm.setDate(4,new Date(op.getPostComponent().getEntries().get(k).getDate().getTime()));
						stm.setInt(5, postIDs.get(k));
						stm.executeUpdate();
						
						// Update item status
						stm = 
							DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
						stm.clearParameters();
						stm.setString(1, "onSale");
						stm.setDate(2, new Date(System.currentTimeMillis()));
						stm.setInt(3, itemID);
						stm.executeUpdate();
					} catch (MySQLIntegrityConstraintViolationException e) {}
				}
			}
		}
		
		// SELL
		if(status.getSoldCheckbox().booleanValue() && soldID == -1){
			// Insert sell
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO sell(price,date,item_id,buyer) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				if(sop.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, sop.getPrice());
				}
				stm.setDate(2, new Date(sop.getDate().getTime()));
				stm.setInt(3, itemID);
				if(sop.getBuyer() == null){
					stm.setNull(4, Types.DECIMAL);
				}else{
					stm.setString(4, sop.getBuyer());
				}
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					soldID = keys.getInt(1);
				}
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "sold");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}else if(status.getSoldCheckbox().booleanValue()){
			// Update sell
			try {
				stm = DBactions.conn.prepareStatement("UPDATE sell SET price=?,date=?,buyer=? WHERE id=?;");
				if(sop.getPrice() == null){
					stm.setNull(1, Types.DECIMAL);
				}else{
					stm.setBigDecimal(1, sop.getPrice());
				}
				stm.setDate(2, new Date(sop.getDate().getTime()));
				if(sop.getBuyer() == null){
					stm.setNull(3, Types.DECIMAL);
				}else{
					stm.setString(3, sop.getBuyer());
				}
				stm.setInt(4, soldID);
				stm.executeUpdate();
				
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=?,lastModified=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "sold");
				stm.setDate(2, new Date(System.currentTimeMillis()));
				stm.setInt(3, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
	}
}