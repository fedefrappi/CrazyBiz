package com.example.crazybiz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

import dao.DBactions;
import domain.Brand;
import domain.Model;

public class InsertItem extends GridLayout{
	private CrazybizApplication crazybizApplication;
	private String username;
	private ComboBox brand = new ComboBox("Brand");
	private ComboBox model = new ComboBox("Model");
	private TextField source;
	private ImageUpload iu;
	private StatusManager status;
	private Button backButton;
	private Button saveButton;
	private VerticalLayout leftLayout;
	
	private WatchingPanel wp;
	private BoughtPanel bp;
	private ShippedPanel shp;
	private OnSalePanel op;
	private SoldPanel sop;
	
	private int brandID;
	private int modelID;
	private int itemID;

	
	public InsertItem(CrazybizApplication crazybizApplication, String username) {
		super(2,1);
		this.crazybizApplication = crazybizApplication;
		this.username = username;
		
		brandID = -1;
		modelID = -1;
		itemID = -1;
		
		try {
			init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() throws SQLException {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		// Left layout
		leftLayout = new VerticalLayout();
		
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

		List<Brand> brandList = DBactions.getBrands();
		for(Brand currentBrand : brandList){
			brand.addItem(currentBrand.getName());			
		}
		brand.setWidth("100px");
		brand.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		brand.setImmediate(true);
		brand.addListener(new ValueChangeListener() {
			@Override
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
			@Override
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
			@Override
			public void addNewItem(String newItemCaption) {
				model.addItem(newItemCaption);
				model.setValue(newItemCaption);
			}
		});

		source = new TextField("Source");
		source.setWidth("200px");
		
		status = new StatusManager(this);
		
		iu = new ImageUpload();

		saveButton = new Button("Save item");
		saveButton.addListener(new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				if(wp == null){
					wp = new WatchingPanel();
				}
				if(bp == null){
					bp = new BoughtPanel();
				}
				if(shp == null){
					shp = new ShippedPanel();
				}
				if(op == null){
					op = new OnSalePanel();
				}
				if(sop == null){
					sop = new SoldPanel();
				}
				try {
					executeQuery();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		leftLayout.addComponent(backLayout);
        setComponentAlignment(backLayout, Alignment.TOP_LEFT);
		leftLayout.addComponent(brand);
		leftLayout.addComponent(model);
		leftLayout.addComponent(source);
		leftLayout.addComponent(status);
		leftLayout.addComponent(iu);
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

		if(brandID==-1){
			// Insert brand
			try {
				stm = DBactions.conn.prepareStatement("INSERT INTO brand(brand_name,brand_website) VALUES(?,?);",Statement.RETURN_GENERATED_KEYS);
				stm.setString(1, brand.getValue().toString());
				stm.setString(2, "unknown");
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
			try {
				stm = DBactions.conn.prepareStatement("SELECT brand_id FROM brand WHERE brand_name=?;");
				stm.clearParameters();
				stm.setString(1, brand.getValue().toString());
				res = stm.executeQuery();
				while(res.next()){
					brandID = res.getInt(1);
				}
			} catch (SQLException e) {
			}
		}
		
		if(modelID==-1){
			// Insert model
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO model(brand_id,model_name,model_price) VALUES(?,?,?)");
				stm.clearParameters();
				stm.setInt(1, brandID);
				stm.setString(2, model.getValue().toString());
				stm.setBigDecimal(3, BigDecimal.valueOf(0.0));
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
	
			try{
				stm = DBactions.conn.prepareStatement("SELECT model_id FROM model WHERE model_name=?;");
				stm.clearParameters();
				stm.setString(1, model.getValue().toString());
				res = stm.executeQuery();
				while(res.next()){
					modelID = res.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(itemID==-1){
			// Insert item
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO item(source,model_id,status) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
				stm.clearParameters();
				stm.setString(1, source.getValue().toString());
				stm.setInt(2, modelID);
				stm.setString(3, "disabled");
				stm.executeUpdate();
				ResultSet keys = stm.getGeneratedKeys();
				if(keys.next()){
					itemID = keys.getInt(1);
				}
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}

		if(status.getWatchingCheckbox().booleanValue()){
			// Insert watching
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO watched(price,fdpin,item_id) VALUES(?,?,?)");
				stm.clearParameters();
				stm.setBigDecimal(1, wp.getPrice());
				stm.setBoolean(2, wp.getFdpin());
				stm.setInt(3, itemID);
				stm.executeUpdate();
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "watching");
				stm.setInt(2, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}

		if(status.getBoughtCheckbox().booleanValue()){
			// Insert buy
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO buy(price,name,phone,email,country,city,item_id,date) VALUES(?,?,?,?,?,?,?)");
				stm.clearParameters();
				stm.setBigDecimal(1, bp.getPrice());
				stm.setString(2, bp.getSellerName());
				stm.setString(3, bp.getSellerPhone());
				stm.setString(4, bp.getSellerEmail());
				stm.setString(5, bp.getSellerCountry());
				stm.setString(6, bp.getSellerCity());
				stm.setInt(7, itemID);
				stm.setDate(8, (Date)bp.getDate());
				stm.executeUpdate();
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "bought");
				stm.setInt(2, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		if(status.getShippedCheckbox().booleanValue()){
			// Insert shipping
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO shipping(tracking,to,company,item_id) VALUES(?,?,?,?)");
				stm.clearParameters();
				stm.setString(1, shp.getTracking());
				stm.setString(2, shp.getTo());
				stm.setString(3, shp.getCompany());
				stm.setInt(4, itemID);
				stm.executeUpdate();
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "shipped");
				stm.setInt(2, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
		
		if(status.getOnsaleCheckbox().booleanValue()){
			// Insert on sale
			for(ProposalEntryComponent proposal : op.getProposalComponent().getEntries()){
				try{
					stm = 
						DBactions.conn.prepareStatement("INSERT INTO proposal(price,user,message,item_id) VALUES(?,?,?,?)");
					stm.clearParameters();
					stm.setBigDecimal(1, proposal.getPrice());
					stm.setString(2, proposal.getUser());
					stm.setString(3, proposal.getMessage());
					stm.setInt(4, itemID);
					stm.executeUpdate();
				} catch (MySQLIntegrityConstraintViolationException e) {}
			}
			for(PostEntryComponent post : op.getPostComponent().getEntries()){
				try{
					stm = 
						DBactions.conn.prepareStatement("INSERT INTO post(price,source,message,item_id) VALUES(?,?,?,?)");
					stm.clearParameters();
					stm.setBigDecimal(1, post.getPrice());
					stm.setString(2, post.getSource());
					stm.setString(3, post.getMessage());
					stm.setInt(4, itemID);
					stm.executeUpdate();
					
				} catch (MySQLIntegrityConstraintViolationException e) {}
			}
			// Update item status
			stm = 
				DBactions.conn.prepareStatement("UPDATE item SET status=? WHERE item_id=?");
			stm.clearParameters();
			stm.setString(1, "onSale");
			stm.setInt(2, itemID);
			stm.executeUpdate();
		}
		
		if(status.getSoldCheckbox().booleanValue()){
			// Insert sell
			try{
				stm = 
					DBactions.conn.prepareStatement("INSERT INTO sell(price,date,item_id,buyer) VALUES(?,?,?,?)");
				stm.clearParameters();
				stm.setBigDecimal(1, sop.getPrice());
				stm.setDate(2, new Date(100));
				stm.setInt(3, itemID);
				stm.setString(4, sop.getBuyer());
				stm.executeUpdate();
				// Update item status
				stm = 
					DBactions.conn.prepareStatement("UPDATE item SET status=? WHERE item_id=?");
				stm.clearParameters();
				stm.setString(1, "sold");
				stm.setInt(2, itemID);
				stm.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {}
		}
	}
}