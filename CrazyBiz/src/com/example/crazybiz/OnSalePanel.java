package com.example.crazybiz;

import com.vaadin.ui.GridLayout;

public class OnSalePanel extends GridLayout{

	private PostComponent postComponent;
	private ProposalComponent proposalComponent;
		
	public OnSalePanel() {     
		super(1,2);
		setSpacing(true);
		setSizeFull();
		postComponent = new PostComponent();
		postComponent.setSizeFull();
		proposalComponent = new ProposalComponent();
		proposalComponent.setSizeFull();
		addComponent(postComponent,0,0);
		addComponent(proposalComponent,0,1);
	}

	public PostComponent getPostComponent() {
		return postComponent;
	}

	public ProposalComponent getProposalComponent() {
		return proposalComponent;
	}
}
