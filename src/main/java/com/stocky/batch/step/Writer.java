package com.stocky.batch.step;

import java.sql.Connection;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.stocky.batch.model.OutputModel;
import com.stocky.batch.util.AccountUtil;
import com.stocky.batch.util.ConnectionUtil;
import com.stocky.batch.util.Constants;

public class Writer implements ItemWriter<OutputModel> {
	private static Connection conn = ConnectionUtil.getConnection(Constants.STOCKYDATABASE);

	@Override
	public void write(List<? extends OutputModel> result) throws Exception {
		for(OutputModel o : result){
			AccountUtil.updateAccount(conn, o.getUserId(), o.getAccount(), o.getPortfolioValue());
		}
	}
}
