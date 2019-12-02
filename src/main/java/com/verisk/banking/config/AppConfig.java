package com.verisk.banking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.jpa.Transaction;

@Configuration
public class AppConfig extends RepositoryRestConfigurerAdapter {
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Account.class);
    config.exposeIdsFor(Transaction.class);
  }

}
