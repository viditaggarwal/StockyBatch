package com.stocky.batch.model;

@Entity
public class Portfolio {
	public Portfolio() {
		
	}
	
	public Portfolio(String stockId, int quantity, double price) {
		this.stockId = stockId;
		this.quantity = quantity;
		this.price = price;
	}
	@Column(name="userId")
	private String userId;
	@Column(name="stockId")
	private String stockId;
	@Column(name="quantity")
	private int quantity;
	@Column(name="price")
	private double price;
	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
