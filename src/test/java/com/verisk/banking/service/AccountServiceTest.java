package com.verisk.banking.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import com.verisk.banking.dto.TransactionResult;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.jpa.repository.AccountRepository;
import com.verisk.banking.jpa.repository.TransactionRepository;
import com.verisk.banking.service.impl.AccountServiceImpl;
import com.verisk.banking.util.CurrentAccountHolder;
import com.verisk.banking.util.TransactionTypes;

@RunWith(SpringRunner.class)
//@PrepareForTest({ SessionUtil.class })
public class AccountServiceTest {
  
  @TestConfiguration
  static class AccountServiceTestContextConfiguration {
      @Bean
      public AccountService accountService() {
          return new AccountServiceImpl();
      }
  }  
  
  @Autowired
  private AccountService accountService;  
  
  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private TransactionRepository transactionRepository;
  

  @Before
  public void setUp() {
    Account account = new Account();
    account.setPin(1111);
    
    Mockito.when(accountRepository.findById(1L))
    .thenReturn(Optional.of(account));
  }  
  
  @Test
  public void testInvalidPin(){
    
    boolean response = accountService.isValidPin(1L, 2222);
    assertFalse(response);
  }

  @Test
  public void testValidPin(){
    
    boolean response = accountService.isValidPin(1L, 1111);
    assertTrue(response);
  }

  @Test
  public void testCloseAccount(){
    
    Account account = new Account();
    account.setDeleted(false);
    CurrentAccountHolder.setCurrentAccount(account);
        
    accountService.closeAccount();

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.isDeleted());
    
  }

  @Test
  public void testDepositEndpoint(){
    
    Account account = new Account();
    account.setDeleted(false);
    
    TransactionResult result = accountService.makeTransactionFromEndpoint(account, 20f, "deposito", TransactionTypes.DEPOSIT);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 20f);
    assertTrue(result.getDescription().equals("Operation completed successfully"));
    
    
  }

  @Test
  public void testWithdrawalEndpointNotEnoghFunds(){
    
    Account account = new Account();
    account.setDeleted(false);
    account.setCurrentBalance(20);
    
    TransactionResult result = accountService.makeTransactionFromEndpoint(account, 30f, "withdrawal", TransactionTypes.WITHDRAWAL);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 20f);
    assertTrue(result.getDescription().equals("The account does not have enough funds to support this transaction"));
    
  }

  @Test
  public void testWithdrawalEndpointEnoghFunds(){
    
    Account account = new Account();
    account.setDeleted(false);
    account.setCurrentBalance(30);
        
    TransactionResult result = accountService.makeTransactionFromEndpoint(account, 20f, "withdrawal", TransactionTypes.WITHDRAWAL);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 10f);
    assertTrue(result.getDescription().equals("Operation completed successfully"));
    
  }

  @Test
  public void testDepositConsoleEndpoint(){
    
    Account account = new Account();
    account.setDeleted(false);
    CurrentAccountHolder.setCurrentAccount(account);
    
    accountService.makeTransactionFromConsole(20f, "deposito", TransactionTypes.DEPOSIT);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 20f);
  }

  @Test
  public void testWithdrawalConsoleNotEnoghFunds(){
    
    Account account = new Account();
    account.setDeleted(false);
    account.setCurrentBalance(20);
    CurrentAccountHolder.setCurrentAccount(account);
    
    accountService.makeTransactionFromConsole(30f, "withdrawal", TransactionTypes.WITHDRAWAL);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 20f);
  }

  @Test
  public void testWithdrawalConsoleEnoghFunds(){
    
    Account account = new Account();
    account.setDeleted(false);
    account.setCurrentBalance(30);
    CurrentAccountHolder.setCurrentAccount(account);
        
    accountService.makeTransactionFromConsole(20f, "withdrawal", TransactionTypes.WITHDRAWAL);

    Mockito.when(accountRepository.save(account))
    .thenReturn(account);
    
    assertTrue(account.getCurrentBalance() == 10f);
    
  }

  @Test
  public void testCurrentBalance(){
    
    Account account = new Account();
    account.setCurrentBalance(30);
    
    CurrentAccountHolder.setCurrentAccount(account);
    
    assertTrue(accountService.getCurrentBalance() == 30);
    
  }

}
