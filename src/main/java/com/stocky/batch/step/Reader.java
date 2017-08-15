package com.stocky.batch.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.stocky.batch.model.Account;
import com.stocky.batch.model.ItemModel;
import com.stocky.batch.model.User;
import com.stocky.batch.util.AccountUtil;
import com.stocky.batch.util.ConnectionUtil;
import com.stocky.batch.util.Constants;
import com.stocky.batch.util.ResultSetMapper;

public class Reader implements ItemReader<ItemModel> {
	private static Connection conn = ConnectionUtil.getConnection(Constants.STOCKYDATABASE);
	private List<ItemModel> list = getUserAndAccount();
	
	private int count = 0;
	@Override
	public ItemModel read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		if (count < list.size()) {
			return list.get(count++);
		} else {
			count = 0;
		}
		return null;
	}

	private List<ItemModel> getUserAndAccount() {
		List<ItemModel> list = new ArrayList<>();
		try {
            String query = "select distinct(userId) from user";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ResultSetMapper<User> resultSetMapper = new ResultSetMapper<User>();
            List<User> userList = resultSetMapper.mapResultSetToObjects(rs, User.class);
            for(User u : userList){
                Account account = AccountUtil.findExistingAccount(conn, u.getUserId());
                list.add(new ItemModel(u, account)); 
            }
            conn.commit();
            conn.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return null;
	}

}
