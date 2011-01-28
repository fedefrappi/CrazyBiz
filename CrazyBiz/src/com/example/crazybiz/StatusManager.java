package com.example.crazybiz;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Button.ClickEvent;

public class StatusManager extends GridLayout{

	private CheckBox watchingCheckbox;
	private CheckBox boughtCheckbox;
	private CheckBox shippedCheckbox;
	private CheckBox onsaleCheckbox;
	private CheckBox soldCheckbox;
	private Button watchingButton;
	private Button boughtButton;
	private Button shippedButton;
	private Button onsaleButton;
	private Button soldButton;
	private InsertItem parent;
	
	public StatusManager(final InsertItem parent) {
		super(2,5);
		this.parent = parent;
		// Init
		setSpacing(true);
		watchingCheckbox = new CheckBox("Watching");
		boughtCheckbox = new CheckBox("Bought");
		shippedCheckbox = new CheckBox("Shipped");
		onsaleCheckbox = new CheckBox("On Sale");
		soldCheckbox = new CheckBox("Sold");
		watchingButton = new Button("-->");
		boughtButton = new Button("-->");
		shippedButton = new Button("-->");
		onsaleButton = new Button("-->");
		soldButton = new Button("-->");
		watchingButton.setEnabled(false);
		boughtButton.setEnabled(false);
		shippedButton.setEnabled(false);
		onsaleButton.setEnabled(false);
		soldButton.setEnabled(false);
		watchingCheckbox.setImmediate(true);
		boughtCheckbox.setImmediate(true);
		shippedCheckbox.setImmediate(true);
		onsaleCheckbox.setImmediate(true);
		soldCheckbox.setImmediate(true);
		
		// ADD LISTENERS
		// --- Checkbox
		watchingCheckbox.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				watchingButton.setEnabled(event.getButton().booleanValue());
			}
		});
		boughtCheckbox.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				boughtButton.setEnabled(event.getButton().booleanValue());
			}
		});
		shippedCheckbox.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				shippedButton.setEnabled(event.getButton().booleanValue());
			}
		});
		onsaleCheckbox.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				onsaleButton.setEnabled(event.getButton().booleanValue());
			}
		});
		soldCheckbox.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				soldButton.setEnabled(event.getButton().booleanValue());
			}
		});
		// --- Buttons
		watchingButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				parent.showWatchingPanel();
			}
		});
		boughtButton.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.showBoughtPanel();
			}
		});
		shippedButton.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.showShippedPanel();
			}
		});
		onsaleButton.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.showOnSalePanel();
			}
		});
		soldButton.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.showSoldPanel();
			}
		});

        
		// Add to the layout
		addComponent(watchingCheckbox,0,0);
		addComponent(watchingButton,1,0);
		addComponent(boughtCheckbox,0,1);
		addComponent(boughtButton,1,1);
		addComponent(shippedCheckbox,0,2);
		addComponent(shippedButton,1,2);
		addComponent(onsaleCheckbox,0,3);
		addComponent(onsaleButton,1,3);
		addComponent(soldCheckbox,0,4);
		addComponent(soldButton,1,4);
	}

}