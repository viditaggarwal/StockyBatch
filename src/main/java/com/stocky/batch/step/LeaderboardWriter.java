package com.stocky.batch.step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.stocky.batch.model.LeaderboardModel;
import com.stocky.batch.util.ConnectionUtil;

public class LeaderboardWriter implements ItemWriter<LeaderboardModel> {

	@Override
	public void write(List<? extends LeaderboardModel> result){
		Connection connection = new ConnectionUtil().getConnection();
		try{
			System.out.println("LEADERBOARD WRITING");
			String sql = "insert into leaderboard (userId, firstName, lastName, emailId, portfolioValue, buyingPower)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			for (LeaderboardModel leaderboard: result) {
				ps.setString(1, leaderboard.getUserId());
				ps.setString(2, leaderboard.getFirstName());
				ps.setString(3, leaderboard.getLastName());
				ps.setString(4, leaderboard.getEmailId());
				ps.setDouble(5, leaderboard.getPortfolioValue());
				ps.setDouble(6, leaderboard.getBuyingPower());
				ps.addBatch();
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
