package com.verisk.banking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;


@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = CommandLineRunner.class))
@EnableAutoConfiguration
public class AccountManagementTest {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(AccountManagementTest.class);
    System.out.println("profile***");
    
    app.setLogStartupInfo(false);
    app.run(args);
  }
}