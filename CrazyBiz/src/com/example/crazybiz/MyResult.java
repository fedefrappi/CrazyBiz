package com.example.crazybiz;

public class MyResult {

	private String brand;
	private String model;
	private String status;
	
	public MyResult(String brand, String model, String status) {
		super();
		this.brand = brand;
		this.model = model;
		this.status = status;
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
	
	
}
