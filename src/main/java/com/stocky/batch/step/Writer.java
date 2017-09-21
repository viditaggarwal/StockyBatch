package com.stocky.batch.step;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.stocky.batch.model.OutputModel;
import com.stocky.batch.util.AccountUtil;
import com.stocky.batch.util.ConnectionUtil;

public class Writer implements ItemWriter<OutputModel> {

	@Override
	public void write(List<? extends OutputModel> result){
		Connection connection = new ConnectionUtil().getConnection();
		try{
			System.out.println("WRITING");
			for(OutputModel o : result){
				if(o.getPortfolioValue() != o.getOldPortfolioValue()){
					AccountUtil.updateAccount(connection, o.getUserId(), o.getAccount(), o.getPortfolioValue());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
        	try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
}
