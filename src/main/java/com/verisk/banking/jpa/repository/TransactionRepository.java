package com.verisk.banking.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.verisk.banking.jpa.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
