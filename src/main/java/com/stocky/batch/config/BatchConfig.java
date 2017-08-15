package com.stocky.batch.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stocky.batch.listener.JobCompletionListener;
import com.stocky.batch.model.Account;
import com.stocky.batch.model.ItemModel;
import com.stocky.batch.model.OutputModel;
import com.stocky.batch.model.User;
import com.stocky.batch.step.Processor;
import com.stocky.batch.step.Writer;
import com.stocky.batch.util.AccountUtil;
import com.stocky.batch.util.ConnectionUtil;
import com.stocky.batch.util.Constants;
import com.stocky.batch.util.ResultSetMapper;

@Configuration
public class BatchConfig {
	private static Connection connection = ConnectionUtil.getInstance().getConnection();
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public Job processJob() {
		return jobBuilderFactory.get("processJob")
				.incrementer(new RunIdIncrementer()).listener(listener())
				.flow(orderStep1()).end().build();
	}
	
	@Bean
	public ListItemReader<ItemModel> reader(){
		ListItemReader<ItemModel> reader = new ListItemReader<>(getUserAndAccount());
		return reader;
	}
	
	
	private List<ItemModel> getUserAndAccount() {
		List<ItemModel> list = new ArrayList<>();
		try {
            String query = "select distinct(userId) from user";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ResultSetMapper<User> resultSetMapper = new ResultSetMapper<User>();
            List<User> userList = resultSetMapper.mapResultSetToObjects(rs, User.class);
            for(User u : userList){
                Account account = AccountUtil.findExistingAccount(connection, u.getUserId());
                list.add(new ItemModel(u, account)); 
            }
            connection.commit();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return null;
	}

	@Bean
	public Step orderStep1() {
		return stepBuilderFactory.get("orderStep1").<ItemModel, OutputModel> chunk(1)
				.reader(reader()).processor(new Processor())
				.writer(new Writer()).build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

}
