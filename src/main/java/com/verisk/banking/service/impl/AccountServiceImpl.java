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
import com.verisk.banking.util.SessionUtil;
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
    SessionUtil.setCurrentAccount(account);
    return accountRepository.save(account);
  }
  
  @Override
  public boolean closeAccount() {
    Account account = SessionUtil.getCurrentAccount();
    account.setDeleted(true);

    try {
      accountRepository.save(account);
      SessionUtil.setCurrentAccount(null);
      return true;
    } catch (OptimisticLockException e) {
      System.out.println("Account cannot be closed as is being used somewhere else. Try again");
      return false;
    }
  }
  
  @Override
  public TransactionResult makeTransaction(float amount, String description, 
      TransactionTypes type) {
    TransactionResult trResult = new TransactionResult();
    String resultDescription = "";
    Account account = SessionUtil.getCurrentAccount();
    if (type.equals(TransactionTypes.DEPOSIT) || type.equals(TransactionTypes.DEBIT)) {
      account.setCurrentBalance(account.getCurrentBalance() + amount);      
    } else {
      float currentBalance = account.getCurrentBalance();
      if (currentBalance < amount) {
        resultDescription = "The account does not have enough funds to support this transaction";
        trResult.setResult("FAIL");
        trResult.setDescription(resultDescription);
        System.out.println(resultDescription);
        return trResult;
      } else {
        account.setCurrentBalance(currentBalance - amount);
      }
    }
    try {
      SessionUtil.setCurrentAccount(accountRepository.save(account));
    } catch(OptimisticLockException e) {
      resultDescription = "Account is modified by someone else. Try again";
      trResult.setDescription(resultDescription);
      trResult.setResult("FAIL");
      System.out.println(resultDescription);
      return trResult;
    }
    Transaction transaction = this.createTransaction(account, description, amount, type);
    resultDescription = "Operation completed successfully";
    System.out.println(resultDescription);
    System.out.println("Your transaction id is " + transaction.getId());
    
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
        SessionUtil.setCurrentAccount(account);
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
    Account account = SessionUtil.getCurrentAccount();
    return account.getCurrentBalance();
  }
}
