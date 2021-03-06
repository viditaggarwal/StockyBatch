package com.stocky.batch.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.stocky.batch.model.Account;

public class AccountUtil {

    public static Account findExistingAccount(Connection conn, String userId){
    	try{
	        Account account = null;
	        String query = "select * from account where userId =\""
	                + userId +"\" order by startDate desc limit 1";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();
	        ResultSetMapper<Account> resultSetMapper = new ResultSetMapper<Account>();
	        List<Account> accountList = resultSetMapper.mapResultSetToObjects(rs, Account.class);
	        if (!CollectionUtils.isEmpty(accountList)) {
	            account = accountList.get(0);
	        }
	        return account;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }

    public static void updateAccount(Connection conn, String userId, Account account, double portfolioValue){
    	try{
	    	String formattedDate = Utility.getCurrentDate();
	    	String query = "SET SQL_SAFE_UPDATES=0";
			PreparedStatement preparedSt = conn.prepareStatement(query);
			preparedSt.execute();
			conn.commit();
	        
	        query = "update stocky.account set endDate=\'"+formattedDate+"\'"
	                +" where userId=\'"+userId+"\' order by id desc limit 1";
	        Statement stmt = conn.createStatement();
	        stmt.executeUpdate(query);
	        
	        query = "update stocky.user set portfolioValue="+account.getPortfolioValue()+", buyingPower="+account.getBuyingPower()
	                +" where userId=\'"+userId+"\'";
	        PreparedStatement preparedStmt = conn.prepareStatement(query);
	        preparedStmt.executeUpdate();
	        preparedStmt.close();
	        
	        query = "insert into account (userId, portfolioValue, buyingPower, startDate, endDate)"
	                + " values (?, ?, ?, ?, ?)";
	
	        preparedStmt = conn.prepareStatement(query);
	        preparedStmt.setString(1, userId);
	        preparedStmt.setDouble(2, portfolioValue);
	        preparedStmt.setDouble(3, account.getBuyingPower());
	        preparedStmt.setString(4, formattedDate);
	        preparedStmt.setDate(5, null);
	        preparedStmt.execute();
	        preparedStmt.close();
	        conn.commit();
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }
}
