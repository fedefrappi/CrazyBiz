package it.crazybiz.server.domain;

import java.util.List;

public class Item {
	private int id;
	private String source;
	private Item.Status status;
	private Purchase purchase;
	private Sale sale;
	private Model model;
	private List<Proposal> proposals;
	private List<Post> posts;

	public static enum Status{NONE, WATCHED, BOUGHT, SHIPPED, ONSALE, SOLD};
}
