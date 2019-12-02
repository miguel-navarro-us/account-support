package com.verisk.banking.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.verisk.banking.jpa.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
  
}
