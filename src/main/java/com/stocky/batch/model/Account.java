package com.stocky.batch.model;

import java.sql.Date;

@Entity
public class Account {
	@Column(name="userId")
	private String userId;
	@Column(name="portfolioValue")
	private Double portfolioValue;
	@Column(name="buyingPower")
	private Double buyingPower;
	@Column(name="startDate")
	private Date startDate;
	@Column(name="endDate")
	private Date endDate;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getPortfolioValue() {
		return portfolioValue;
	}
	public void setPortfolioValue(Double portfolioValue) {
		this.portfolioValue = portfolioValue;
	}
	public Double getBuyingPower() {
		return buyingPower;
	}
	public void setBuyingPower(Double buyingPower) {
		this.buyingPower = buyingPower;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
