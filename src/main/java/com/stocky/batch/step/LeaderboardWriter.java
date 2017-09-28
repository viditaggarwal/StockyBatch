package com.stocky.batch.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.stocky.batch.model.LeaderboardModel;
import com.stocky.batch.util.ConnectionUtil;

public class LeaderboardWriter implements ItemWriter<LeaderboardModel> {

	private float rank;
	
	public LeaderboardWriter(Float x) {
		this.rank = x;
	}
	
	@Override
	public void write(List<? extends LeaderboardModel> result){
		Connection connection = new ConnectionUtil().getConnection();
		try{
			System.out.println("LEADERBOARD WRITING");
			PreparedStatement ps = null;
			for (LeaderboardModel leaderboard: result) {
				String sql = "update stocky.user set rank="+(rank)+
						", portfolioValue="+leaderboard.getPortfolioValue()+
						", buyingPower="+leaderboard.getBuyingPower()+
						" where userId=\""+leaderboard.getUserId()+"\"";
				ps = connection.prepareStatement(sql);
				ps.addBatch();
				rank++;
			}
			ps.executeBatch();
			ps.close();
			connection.commit();
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
