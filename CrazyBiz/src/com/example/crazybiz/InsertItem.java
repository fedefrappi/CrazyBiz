package com.example.crazybiz;

import java.sql.SQLException;
import java.util.List;

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

import dao.UserDAO;
import domain.Brand;
import domain.Model;

public class InsertItem extends GridLayout{
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

	
	public InsertItem(String username) {
		super(2,1);
		this.username = username;
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
				getWindow().removeComponent(getParent());
				getWindow().setContent(new Homepage(username));
			}
		});
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backLayout.addComponent(backButton);

		// Sets the combobox to show a certain property as the item caption
		List<Brand> brandList = UserDAO.getBrands();
		for(Brand currentBrand : brandList){
			brand.addItem(currentBrand.getName());			
		}
		// Sets the icon to use with the items
		brand.setWidth("100px");
		// Set the appropriate filtering mode for this example
		brand.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		brand.setImmediate(true);
		brand.addListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				String selectedBrand = event.getProperty().toString();
				List<Model> filteredModels;
				try {
					filteredModels = UserDAO.getModels(selectedBrand);
					model.removeAllItems();
					for(Model currentModel : filteredModels){
						model.addItem(currentModel.getName());			
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// Disallow null selections
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
		// Set the appropriate filtering mode for this example
		model.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		model.setImmediate(true);
		// Disallow null selections
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
}