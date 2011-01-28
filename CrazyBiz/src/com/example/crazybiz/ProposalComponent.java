package com.example.crazybiz;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class ProposalComponent extends VerticalLayout{
	
	private List<ProposalEntryComponent> entries;
	private Button addEntry;
	
	public ProposalComponent() {
		setSpacing(true);
		entries = new ArrayList<ProposalEntryComponent>();
		addEntry = new Button("Add proposal");
		addEntry.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ProposalEntryComponent pec = new ProposalEntryComponent();
				entries.add(pec);
				addComponent(pec);
			}
		});
		addComponent(addEntry);
	}

	public List<ProposalEntryComponent> getEntries() {
		return entries;
	}
}
