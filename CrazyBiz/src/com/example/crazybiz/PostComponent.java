package com.example.crazybiz;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class PostComponent extends VerticalLayout{
	
	private List<PostEntryComponent> entries;
	private Button addEntry;
	
	public PostComponent() {
		setSpacing(true);
		entries = new ArrayList<PostEntryComponent>();
		addEntry = new Button("Add entry");
		addEntry.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				PostEntryComponent pec = new PostEntryComponent();
				entries.add(pec);
				addComponent(pec);
			}
		});
		addComponent(addEntry);
	}
}
