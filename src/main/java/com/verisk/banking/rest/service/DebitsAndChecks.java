package com.verisk.banking.rest.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.verisk.banking.dto.TransactionRequest;
import com.verisk.banking.dto.TransactionResult;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.jpa.repository.AccountRepository;
import com.verisk.banking.service.AccountService;
import com.verisk.banking.util.TransactionTypes;

@RestController
@RequestMapping("/account-management/public")
public class DebitsAndChecks {
  
  private AccountService accountService;
  private AccountRepository accountRepository;
  
  @Autowired
  public DebitsAndChecks(AccountService accountService, AccountRepository accountRepository) {
    this.accountService = accountService;
    this.accountRepository = accountRepository;
  }     
  
  @PutMapping(path = "/debits")
  public ResponseEntity<TransactionResult> makeDeposit(TransactionRequest transactionRequest)
      throws IOException {
    return this.performTransaction(transactionRequest, TransactionTypes.DEBIT);
  }
  
  private TransactionResult getIncorrectPinResult() {
    TransactionResult transactionResult = new TransactionResult();
    transactionResult.setDescription("Incorrect account number or PIN");
    transactionResult.setResult("FAIL");
    return transactionResult;    
  }
  
  private TransactionResult getTransactionError() {
    TransactionResult transactionResult = new TransactionResult();
    transactionResult.setDescription("Something went wrong with the operation. Try again later");
    transactionResult.setResult("FAIL");
    return transactionResult;    
  }
  
  private ResponseEntity<TransactionResult> performTransaction(
      TransactionRequest transactionRequest, TransactionTypes type) throws IOException {
    
   if (!accountService.isValidPin(transactionRequest.getAccountNumber(),
        transactionRequest.getPin())) {
      return ResponseEntity.ok(getIncorrectPinResult());
    }

    TransactionResult result = null;
    try {
      Account account = accountRepository.findById(transactionRequest.getAccountNumber()).get();
      result = accountService.makeTransactionFromEndpoint(account, transactionRequest.getAmount(),
          transactionRequest.getDescription(), type);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getTransactionError());
    }
    return ResponseEntity.ok(result);
  }
    

  @PutMapping(path = "/checks")
  public ResponseEntity<TransactionResult> makeWithdrawal(TransactionRequest transactionRequest)
      throws IOException {
     return this.performTransaction(transactionRequest, TransactionTypes.CHECK);
  }

}
