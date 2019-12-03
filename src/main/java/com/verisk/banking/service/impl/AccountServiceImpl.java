package com.verisk.banking.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.verisk.banking.dto.AccountRequest;
import com.verisk.banking.dto.TransactionResult;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.jpa.Transaction;
import com.verisk.banking.jpa.repository.AccountRepository;
import com.verisk.banking.jpa.repository.TransactionRepository;
import com.verisk.banking.service.AccountService;
import com.verisk.banking.util.CurrentAccountHolder;
import com.verisk.banking.util.TransactionTypes;

@Service
public class AccountServiceImpl implements AccountService {
  
  @Autowired
  private AccountRepository accountRepository;
  
  @Autowired
  private TransactionRepository transactionRepository;
  
  @Override
  public Account createAccount(AccountRequest accountInfo) {
    Account account = new Account();
    account.setCurrentBalance(0);
    account.setFirstName(accountInfo.getFirstName());
    account.setLastName(accountInfo.getLastName());
    account.setPin(accountInfo.getPin());
    account.setHolderIdNumber(accountInfo.getAccountHolderIdNumber());
    CurrentAccountHolder.setCurrentAccount(account);
    return accountRepository.save(account);
  }
  
  @Override
  public boolean closeAccount() {
    Account account = CurrentAccountHolder.getCurrentAccount();
    account.setDeleted(true);

    try {
      accountRepository.save(account);
      CurrentAccountHolder.setCurrentAccount(null);
      return true;
    } catch (OptimisticLockException e) {
      System.out.println("Account cannot be closed as is being used somewhere else. Try again");
      return false;
    }
  }
  
  @Override
  public void makeTransactionFromConsole(float amount, String description, 
      TransactionTypes type) {
    Account account = CurrentAccountHolder.getCurrentAccount();
    
    boolean updatedBalance = this.updateAccountBalance(account, type, amount);
    if (updatedBalance) {
      try {
        CurrentAccountHolder.setCurrentAccount(accountRepository.save(account));
      } catch(OptimisticLockException e) {
        System.out.println("Account is being modified by someone else. Try again");
      }
    } else {
      System.out.println("The account does not have enough funds to support this transaction");    
      return;
    }
    
    Transaction transaction = this.createTransaction(account, description, amount, type);
    System.out.println("Operation completed successfully");
    System.out.println("Your transaction id is " + transaction.getId());
  }
  
  private boolean updateAccountBalance(Account account, TransactionTypes type, float amount) {
    if (type.equals(TransactionTypes.DEBIT) || type.equals(TransactionTypes.DEPOSIT)) {
      account.setCurrentBalance(account.getCurrentBalance() + amount);
      return true;
    } else {
      float currentBalance = account.getCurrentBalance();
      if (currentBalance < amount) {
        return false;
      } else {
        account.setCurrentBalance(currentBalance - amount);
        return true;
      }      
    }    
  }
  
  @Override
  public TransactionResult makeTransactionFromEndpoint(Account account, float amount, String description, 
      TransactionTypes type) {
    TransactionResult trResult = new TransactionResult();
    String resultDescription = "";
    
    boolean balanceUpdated = this.updateAccountBalance(account, type, amount);
    if (balanceUpdated) {
      try {
        accountRepository.save(account);
      } catch(OptimisticLockException e) {
        resultDescription = "Account is being modified by someone else. Try again";
        trResult.setDescription(resultDescription);
        trResult.setResult("FAIL");
        System.out.println(resultDescription);
        return trResult;
      }
    } else {
      resultDescription = "The account does not have enough funds to support this transaction";
      trResult.setResult("FAIL");
      trResult.setDescription(resultDescription);
      return trResult;      
    }
    
    Transaction transaction = this.createTransaction(account, description, amount, type);
    resultDescription = "Operation completed successfully";    
    trResult.setResult("SUCCESS");
    trResult.setDescription(resultDescription);
    trResult.setTransactionId(transaction.getId());
    
    return trResult;
  }

  @Override
  public boolean isValidPin(long accountNumber, int pin) {
    Optional<Account> opt = accountRepository.findById(accountNumber);
    if (opt.isPresent()) {
      Account account = opt.get();
      if (account.getPin() == pin && !account.isDeleted()) {
        CurrentAccountHolder.setCurrentAccount(account);
        return true;
      }
    }
    return false;
  }
  
  private Transaction createTransaction(Account account, String description, float amount,
      TransactionTypes type) {
    Transaction transaction = new Transaction();
    transaction.setAmount(amount);
    transaction.setType(type.getLabel());
    transaction.setDescription(description);
    transaction.setDate(Timestamp.valueOf(LocalDateTime.now()));
    transaction.setAccount(account);
    transactionRepository.save(transaction);
    return transaction;
  }
  
  public float getCurrentBalance() {
    Account account = CurrentAccountHolder.getCurrentAccount();
    return account.getCurrentBalance();
  }
}
