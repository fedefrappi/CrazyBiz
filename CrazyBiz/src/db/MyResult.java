package db;

public class MyResult {

	private int itemID;
	private String brand;
	private String model;
	private String status;
	private String source;
	
	public MyResult(int itemID, String brand, String model, String status, String source) {
		super();
		this.itemID = itemID;
		this.brand = brand;
		this.model = model;
		this.status = status;
		this.source = source;
	}
	
	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public String getStatus() {
		return status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}