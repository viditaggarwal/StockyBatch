package com.stocky.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardScheduledTasks {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
    JobLauncher jobLauncher;
 
    @Autowired
    Job processLeaderboardJob;
 
    @Scheduled(initialDelay=120000, fixedRate=120000)
    public String handle() throws Exception {
		System.out.println("Leaderboard Job started at " + sdf.format(new Date()));
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addLong("time", System.currentTimeMillis())
    			.toJobParameters();
    	jobLauncher.run(processLeaderboardJob, jobParameters);
        return "Leaderboard Batch job has been invoked";
    }
}
