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
	private double quantity;
	@Column(name="price")
	private double price;
	@Column(name="change")
	private boolean change;
	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
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
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
}
