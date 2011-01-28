package domain;

import java.math.BigDecimal;

public class Model {
	private int id;
	private String name;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	private BigDecimal retailPrice;
	
	public Model(int id, String name, BigDecimal retailPrice) {
		super();
		this.id = id;
		this.name = name;
		this.retailPrice = retailPrice;
	}
	
}
