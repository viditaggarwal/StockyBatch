package com.stocky.batch.model;


public class ItemModel {
	private User user;
	private Account account;
	
	public ItemModel(User user, Account account){
		this.user = user;
		this.account = account;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
