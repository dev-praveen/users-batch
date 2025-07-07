package com.praveen.batch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

  private static final Logger logger = LoggerFactory.getLogger(JobCompletionListener.class);

  @Override
  public void beforeJob(JobExecution jobExecution) {
    logger.info("Job {} is starting.", jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      logger.info("!!! JOB FINISHED! Time to verify the results");
    }
  }
}
