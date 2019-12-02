package com.verisk.banking.jpa.repository;

import static org.junit.Assert.assertEquals;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import com.verisk.banking.config.JpaConfig;
import com.verisk.banking.jpa.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class AccountRepositoryTest {

  @Resource
  private TransactionRepository transactionRepository;

  @Resource
  private AccountRepository accountRepository;

  @Test
  public void accountCheck() {

    Account account = new Account();
    account.setCurrentBalance(0.0F);
    account.setDeleted(false);
    account.setHolderIdNumber(1111);
    account.setLastName("lastname");
    account.setFirstName("firstName");
    account.setPin(222);
    accountRepository.save(account);

    Account accountFound = accountRepository.findById(1l).get();
    assertEquals(accountFound.getFirstName(), account.getFirstName());
  }
}
