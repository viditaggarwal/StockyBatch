package com.stocky.batch.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.item.ItemProcessor;

import com.stocky.batch.model.Account;
import com.stocky.batch.model.ItemModel;
import com.stocky.batch.model.OutputModel;
import com.stocky.batch.model.Portfolio;
import com.stocky.batch.model.User;
import com.stocky.batch.util.ConnectionUtil;
import com.stocky.batch.util.CurrentPriceUtil;
import com.stocky.batch.util.ResultSetMapper;

public class Processor implements ItemProcessor<ItemModel, OutputModel> {
	private static Connection connection = ConnectionUtil.getInstance().getConnection();
	
	@Override
	public OutputModel process(ItemModel data) throws Exception {
		if(data != null)
			return getAndUpdateCurrentPortfolioValue(data.getUser(), 0, data.getAccount());
		return null;
	}
	
	private OutputModel getAndUpdateCurrentPortfolioValue(User u, double portfolioValue, Account account){
		try{
	        String query = "select * from portfolio where userId='"+u.getUserId()+"'";
	        PreparedStatement ps = connection.prepareStatement(query);
	        ResultSet rs = ps.executeQuery();
	        ResultSetMapper<Portfolio> resultSetMapper = new ResultSetMapper<Portfolio>();
	        List<Portfolio> portfolioList = resultSetMapper.mapResultSetToObjects(rs, Portfolio.class);
	        Map<String, Portfolio> portfolioMap = new HashMap<String, Portfolio>();
	        if(!CollectionUtils.isEmpty(portfolioList)){
	            portfolioMap = portfolioList.stream().collect(
	                    Collectors.toMap(Portfolio::getStockId, p -> p));
	        }
	
	        List<Map<String, String>> stockLatest = CurrentPriceUtil.getStockPrices(new ArrayList<>(portfolioMap.keySet()));
	
	        for(Map<String, String> m : stockLatest){
	            Map.Entry<String, String> e = m.entrySet().iterator().next();
	            portfolioValue += portfolioMap.get(e.getKey()).getQuantity() * Double.valueOf(e.getValue());
	        }
	        return new OutputModel(account, u.getUserId(), portfolioValue, u.getPortfolioValue());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
    }

}
