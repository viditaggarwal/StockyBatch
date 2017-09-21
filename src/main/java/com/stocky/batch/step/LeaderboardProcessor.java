package com.stocky.batch.step;

import org.springframework.batch.item.ItemProcessor;

import com.stocky.batch.model.LeaderboardModel;

public class LeaderboardProcessor implements ItemProcessor<LeaderboardModel, LeaderboardModel> {
	@Override
	public LeaderboardModel process(LeaderboardModel data) throws Exception {
		if(data != null)
			return data;
		return null;
	}
}
