package com.verisk.banking.rest.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.verisk.banking.dto.TransactionRequest;
import com.verisk.banking.dto.TransactionResult;
import com.verisk.banking.jpa.repository.AccountRepository;
import com.verisk.banking.service.AccountService;
import com.verisk.banking.util.TransactionTypes;

@RestController
@RequestMapping("/account-management/public")
public class DebitsAndChecks {
  
  private AccountService accountService;
  
  @Autowired
  public DebitsAndChecks(AccountService accountService) {
    this.accountService = accountService;
  }     
  
  @PutMapping(path = "/debits")
  public ResponseEntity<TransactionResult> makeDeposit(TransactionRequest transactionRequest)
      throws IOException {
    if (!accountService.isValidPin(transactionRequest.getAccountNumber(),
        transactionRequest.getPin())) {
        return ResponseEntity.ok(getIncorrectPinResult());
    }

    TransactionResult result = accountService.makeTransaction(transactionRequest.getAmount(),
        transactionRequest.getDescription(), TransactionTypes.DEBIT);
    return ResponseEntity.ok(result);
  }
  
  private TransactionResult getIncorrectPinResult() {
    TransactionResult transactionResult = new TransactionResult();
    transactionResult.setDescription("Incorrect account number or PIN");
    transactionResult.setResult("FAIL");
    return transactionResult;    
  }

  @PutMapping(path = "/checks")
  public ResponseEntity<TransactionResult> makeWithdrawal(TransactionRequest transactionRequest)
      throws IOException {

    if (!accountService.isValidPin(transactionRequest.getAccountNumber(),
        transactionRequest.getPin())) {
        return ResponseEntity.ok(getIncorrectPinResult());
    }

    TransactionResult result = accountService.makeTransaction(transactionRequest.getAmount(),
        transactionRequest.getDescription(), TransactionTypes.CHECK);
    return ResponseEntity.ok(result);
  }

}
