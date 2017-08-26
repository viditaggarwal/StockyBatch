package com.stocky.batch.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.stocky.batch.model.Account;

public class AccountUtil {

    public static Account findExistingAccount(Connection conn, String userId) throws SQLException {
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
    }

    public static void updateAccount(Connection conn, String userId, Account account, double portfolioValue) throws SQLException, ParseException {
    	String formattedDate = Utility.getCurrentDate();
    	String query = "SET SQL_SAFE_UPDATES=0";
		PreparedStatement preparedSt = conn.prepareStatement(query);
		preparedSt.execute();
		conn.commit();
        
        query = "update stocky.account set endDate=\'"+formattedDate+"\'"
                +" where userId=\'"+userId+"\' order by id desc limit 1";
        System.out.println("Update query : " + query);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);

        query = "insert into account (userId, portfolioValue, buyingPower, startDate, endDate)"
                + " values (?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, userId);
        preparedStmt.setDouble(2, portfolioValue);
        preparedStmt.setDouble(3, account.getBuyingPower());
        preparedStmt.setString(4, formattedDate);
        preparedStmt.setDate(5, null);
        preparedStmt.execute();
        preparedStmt.close();
        conn.commit();
    }
}
