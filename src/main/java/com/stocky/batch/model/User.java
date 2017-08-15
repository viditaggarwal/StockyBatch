package com.stocky.batch.model;

@Entity
public class User {
	@Column(name="userId")
	private String userId;
	@Column(name="password")
	private String password;
	@Column(name="portfolioValue")
	private double portfolioValue;
	@Column(name="buyingPower")
	private double buyingPower;
	@Column(name="firstName")
	private String firstName;
	@Column(name="lastName")
	private String lastName;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getPortfolioValue() {
		return portfolioValue;
	}
	public void setPortfolioValue(double portfolioValue) {
		this.portfolioValue = portfolioValue;
	}
	public double getBuyingPower() {
		return buyingPower;
	}
	public void setBuyingPower(double buyingPower) {
		this.buyingPower = buyingPower;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
