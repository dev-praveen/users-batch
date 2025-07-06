package com.praveen.batch.config;

import com.praveen.batch.model.UserDbRecord;
import com.praveen.batch.model.UserCsvRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class BatchConfig {

  @Bean
  public FlatFileItemReader<UserCsvRecord> reader(@Value("${input.file}") String inputFile) {

    return new FlatFileItemReaderBuilder<UserCsvRecord>()
        .name("userItemReader")
        .resource(new ClassPathResource(inputFile))
        .delimited()
        .names("First Name", "Last Name", "Sex", "Email", "Phone", "Date of birth", "Job Title")
        .linesToSkip(1)
        .lineMapper(getLineMapper())
        .build();
  }

  @Bean
  public UserItemProcessor processor() {
    return new UserItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<UserDbRecord> writer(DataSource dataSource) {

    return new JdbcBatchItemWriterBuilder<UserDbRecord>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO users (first_name, last_name, full_name, sex, email, phone, date_of_birth, job_title) "
                + "VALUES (:firstName, :lastName, :fullName, :sex, :email, :phone, :dateOfBirth, :jobTitle)")
        .beanMapped()
        .build();
  }

  @Bean
  public Job importUserJob(
      JobRepository jobRepository, Step step1, JobCompletionListener listener) {

    return new JobBuilder("importUserJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(step1)
        .listener(listener)
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    return new VirtualThreadTaskExecutor("batch-task-executor-");
  }

  @Bean
  public Step step1(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<UserCsvRecord> reader,
      JdbcBatchItemWriter<UserDbRecord> writer) {

    return new StepBuilder("step1", jobRepository)
        .<UserCsvRecord, UserDbRecord>chunk(100, transactionManager)
        .reader(reader)
        .processor(processor())
        .writer(writer)
        .taskExecutor(taskExecutor())
        .faultTolerant()
        .skipPolicy(skipPolicy())
        .build();
  }

  @Bean
  public SkipPolicy skipPolicy() {
    return (throwable, skipCount) -> {
      log.error("Skipping record due to error: {}", throwable.getMessage());
      return true;
    };
  }

  private LineTokenizer getLineTokenizer() {

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setNames(
        "firstName", "lastName", "sex", "email", "phone", "dateOfBirth", "jobTitle");
    lineTokenizer.setIncludedFields(2, 3, 4, 5, 6, 7, 8);
    return lineTokenizer;
  }

  private FieldSetMapper<UserCsvRecord> getFieldSetMapper() {
    return new RecordFieldSetMapper<>(UserCsvRecord.class);
  }

  private LineMapper<UserCsvRecord> getLineMapper() {

    DefaultLineMapper<UserCsvRecord> defaultLineMapper = new DefaultLineMapper<>();
    defaultLineMapper.setLineTokenizer(getLineTokenizer());
    defaultLineMapper.setFieldSetMapper(getFieldSetMapper());
    defaultLineMapper.afterPropertiesSet();
    return defaultLineMapper;
  }
}
