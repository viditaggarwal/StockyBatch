package com.stocky.batch.model;

import java.sql.Date;

public class Coin {
	private Double value;
	private Date timeStamp;
	
	public Coin(Double value, Date timeStamp) {
		super();
		this.value = value;
		this.timeStamp = timeStamp;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
