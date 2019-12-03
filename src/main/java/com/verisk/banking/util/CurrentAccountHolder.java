package com.verisk.banking.util;

import com.verisk.banking.jpa.Account;

public class CurrentAccountHolder {

  private static Account currentAccount;
  
  public static Account getCurrentAccount() {
     return currentAccount;
  }
  
  public static void setCurrentAccount(Account account) {
    currentAccount = account;
  }

}
