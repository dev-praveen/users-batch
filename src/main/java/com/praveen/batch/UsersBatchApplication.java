package com.praveen.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersBatchApplication {

  public static void main(String[] args) {
    System.exit(SpringApplication.exit(SpringApplication.run(UsersBatchApplication.class, args)));
  }
}
