package com.stocky.batch.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.stocky.batch.model.Account;
import com.stocky.batch.model.Portfolio;

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

    public static void updateAccount(Connection conn, String userId, Account account, double changeInValue, String formattedDate, Map<String, Portfolio> portfolioMap) throws SQLException, ParseException {
        double portfolioValue = 0.0;

        if(MapUtils.isEmpty(portfolioMap)){
            System.out.println("No portfolio yet");
            portfolioValue += changeInValue;
        }else{
            System.out.println("Getting value from portfolio");
            List<Map<String, String>> stockLatest = CurrentPriceUtil.getStockPrices(new ArrayList<>(portfolioMap.keySet()));

            for(Map<String, String> m : stockLatest){
                Map.Entry<String, String> e = m.entrySet().iterator().next();
                portfolioValue += portfolioMap.get(e.getKey()).getQuantity() * Double.valueOf(e.getValue());
            }
        }


        String query = "update account set endDate=\'"+formattedDate+"\'"
                +" where userId=\'"+userId+"\' order by id limit 1";
        System.out.println("Update query : " + query);
        PreparedStatement preparedStmtUpdate = conn.prepareStatement(query);
        preparedStmtUpdate.executeUpdate();
        preparedStmtUpdate.close();

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
    }
}
