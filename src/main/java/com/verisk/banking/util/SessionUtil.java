package com.verisk.banking.util;

import org.springframework.stereotype.Component;
import com.verisk.banking.jpa.Account;

@Component
public class SessionUtil {

  private static Account currentAccount;
  
  public static Account getCurrentAccount() {
     return currentAccount;
  }
  
  public static void setCurrentAccount(Account account) {
    currentAccount = account;
  }

}
