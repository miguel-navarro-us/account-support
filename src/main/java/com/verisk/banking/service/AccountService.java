package com.verisk.banking.service;

import com.verisk.banking.dto.AccountRequest;
import com.verisk.banking.dto.TransactionResult;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.util.TransactionTypes;

public interface AccountService {
  
  public Account createAccount(AccountRequest accountInfo);
  public boolean closeAccount ();
  public void makeTransactionFromConsole(float amount, String description, TransactionTypes type);
  public TransactionResult makeTransactionFromEndpoint(Account account, float amount, String description, TransactionTypes type);
  public float getCurrentBalance();
  public boolean isValidPin(long accountNumber, int pin);

}
