package com.stocky.batch.model;

public class OutputModel {
	private Account account;
	private String userId;
	private double portfolioValue;
	
	public OutputModel(Account account, String userId, double portfolioValue) {
		super();
		this.account = account;
		this.userId = userId;
		this.portfolioValue = portfolioValue;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
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
}
