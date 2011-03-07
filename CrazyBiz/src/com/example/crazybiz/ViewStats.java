package com.example.crazybiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.vaadin.vaadinvisualizations.BarChart;
import org.vaadin.vaadinvisualizations.PieChart;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

import db.DBactions;

public class ViewStats extends VerticalLayout{
	private CrazybizApplication crazybizApplication;
	private String username;
	private Button backButton;
	private HorizontalLayout backLayout;
	private GridLayout grid;
	private PieChart pieBrand;
	private PieChart pieModel;
	private BarChart barBrand;
	private BarChart barModel;
	
	public ViewStats(final CrazybizApplication crazybizApplication, final String username) {
		this.crazybizApplication = crazybizApplication;
		this.username = username;
		init();
	}

	private void init() {
		// backButton : back to the homepage
		backLayout = new HorizontalLayout();
		backLayout.setMargin(false,true,false,false);
		backButton = new Button("Back");
		backButton.addListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				crazybizApplication.getWindow().removeAllComponents();
				crazybizApplication.setHome(new Homepage(crazybizApplication, username));
				crazybizApplication.getWindow().setContent(crazybizApplication.getHome());
			}
		});
		backButton.setStyleName(BaseTheme.BUTTON_LINK);
		backLayout.addComponent(backButton);
		
		grid = new GridLayout(2, 2);
		grid.setSpacing(true);
		
		pieBrand = new PieChart();
		populateBrands(pieBrand);
		pieBrand.setWidth("500px");
		pieBrand.setHeight("250px");
		pieBrand.setOption("width", "500px");
		pieBrand.setOption("is3D", true);
		pieBrand.setOption("title", "Brands:");
		
		pieModel = new PieChart();
		populateModels(pieModel);
		pieModel.setWidth("500px");
		pieModel.setHeight("250px");
		pieModel.setOption("width", "500px");
		pieModel.setOption("is3D", true);
		pieModel.setOption("title", "Models:");
		
		barBrand = new BarChart();
		barBrand.addXAxisLabel("Brand");
		barBrand.addBar("Tot.€");
		barBrand.addBar("Avg.€");
		populateBarBrand(barBrand);
		barBrand.setWidth("500px");
		barBrand.setHeight("250px");
		barBrand.setOption("width", "500px");
		barBrand.setOption("is3D", true);
		barBrand.setOption("title", "Earnings by brand:");
		
		barModel = new BarChart();
		barModel.addXAxisLabel("Model");
		barModel.addBar("Tot.€");
		barModel.addBar("Avg.€");
		populateBarModel(barModel);
		barModel.setWidth("500px");
		barModel.setHeight("500px");
		barModel.setOption("width", "500px");
		barModel.setOption("is3D", true);
		barModel.setOption("title", "Earnings by model:");
		
		grid.addComponent(pieBrand,0,0);
		grid.addComponent(pieModel,1,0);
		grid.addComponent(barBrand,0,1);
		grid.addComponent(barModel,1,1);
		
		this.addComponent(backLayout);
		this.addComponent(grid);
		
		this.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
	}

	private void populateBarBrand(BarChart barBrand) {
		try {
			PreparedStatement stm = DBactions.conn.prepareStatement(
					"SELECT brand.brand_name,sum(buy.price),sum(sell.price),count(brand.brand_name) " +
					"FROM item,brand,model,buy,sell " +
					"WHERE brand.brand_id=model.brand_id AND model.model_id=item.model_id AND item.item_id=buy.item_id AND item.item_id=sell.item_id " +
					"GROUP BY brand.brand_name");
			ResultSet res = stm.executeQuery();
			while(res.next()){
				double total = Double.parseDouble(res.getBigDecimal(3).subtract(res.getBigDecimal(2)).toString());
				double mean = (double)(total/(double)res.getInt(4));
				barBrand.add(res.getString(1), new double[]{total,mean});
			}
		}
		catch (SQLException e) {e.printStackTrace();}	
	}

	private void populateBarModel(BarChart barModel) {
		try {
			PreparedStatement stm = DBactions.conn.prepareStatement(
					"SELECT model.model_name,sum(buy.price),sum(sell.price),count(model.model_name) " +
					"FROM item,model,buy,sell " +
					"WHERE model.model_id=item.model_id AND item.item_id=buy.item_id AND item.item_id=sell.item_id " +
					"GROUP BY model.model_name");
			ResultSet res = stm.executeQuery();
			while(res.next()){
				double total = Double.parseDouble(res.getBigDecimal(3).subtract(res.getBigDecimal(2)).toString());
				double mean = (double)(total/(double)res.getInt(4));
				barModel.add(res.getString(1), new double[]{total,mean});
			}
		}
		catch (SQLException e) {e.printStackTrace();}	
	}

	private void populateBrands(PieChart pieBrand) {
		try {
			PreparedStatement stm = DBactions.conn.prepareStatement(
					"SELECT brand.brand_name,count(brand.brand_id)  " +
					"FROM brand,item,model " +
					"WHERE brand.brand_id=model.brand_id AND model.model_id=item.model_id " +
					"GROUP BY brand.brand_name");
			ResultSet res = stm.executeQuery();
			while(res.next()){
				pieBrand.add(res.getString(1), res.getInt(2));
			}
		}
		catch (SQLException e) {e.printStackTrace();}	
	}
	
	private void populateModels(PieChart pieModel) {
		try {
			PreparedStatement stm = DBactions.conn.prepareStatement(
					"SELECT model.model_name,count(model.model_id)  " +
					"FROM item,model " +
					"WHERE model.model_id=item.model_id " +
					"GROUP BY model.model_name");
			ResultSet res = stm.executeQuery();
			while(res.next()){
				pieModel.add(res.getString(1), res.getInt(2));
			}
		}
		catch (SQLException e) {e.printStackTrace();}	
	}

}
