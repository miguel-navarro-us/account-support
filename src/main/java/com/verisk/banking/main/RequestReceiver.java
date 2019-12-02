package com.verisk.banking.main;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.verisk.banking.dto.AccountRequest;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.service.AccountService;
import com.verisk.banking.util.SessionUtil;
import com.verisk.banking.util.TransactionTypes;

@Service
public class RequestReceiver {
  
  @Autowired
  AccountService accountService;

  private void displayInitialMenu(Scanner scanner) {
    System.out.println("Select an action : ");
    System.out.println("1 - Create new Account");
    System.out.println("2 - Open existing Account");
    System.out.println("3 - Exit");

    String selection = scanner.next();
    if (selection.equals("1")) {
      this.createNewAccount(scanner);
    } else if (selection.equals("2")) {
      this.accessExistingAccount(scanner);
    } else if (selection.equals("3")) {
      System.exit(0);
    }
  }
  
  private void accessExistingAccount(Scanner scanner) {
    System.out.println("Type your accountNumber ");
    long accountNumber = scanner.nextLong();
    System.out.println("Capture your PIN " );
    int pin = scanner.nextInt();
    if (accountService.isValidPin(accountNumber, pin)) {
      this.displayAccountMenu(scanner);
    } else {
      System.out.println("Some of the captured values are incorrect. Select an option below");
      System.out.println("1 - Try again");
      System.out.println("2 - Go back to previouse menu");
      String selection = scanner.next();
      if (selection.equals("1")) {
        accessExistingAccount(scanner);
      } else {
        this.displayInitialMenu(scanner);
      }
    }
  }
  
  private void displayAccountMenu(Scanner scanner) {
    System.out.println("Select a choise:");
    System.out.println("1 - Make a deposit");
    System.out.println("2 - Make a withdrawal");
    System.out.println("3 - Return current balance");
    System.out.println("4 - Close the account");
    System.out.println("5 - Exit from this account");
    
    String choice = scanner.next();
    switch(choice) {
      case "1":
        this.makeTransaction(TransactionTypes.DEPOSIT, scanner);
        break;
      case "2": 
        this.makeTransaction(TransactionTypes.WITHDRAWAL, scanner);
      case "3":
        System.out.println("Your current balance is " + accountService.getCurrentBalance());
      case "4":
        this.closeAccount(scanner);
      case "5":
        this.logout(scanner);
      
    }
  }
  
  private void logout(Scanner scanner) {
    SessionUtil.setCurrentAccount(null);
    this.displayInitialMenu(scanner);
  }
  
  private void closeAccount(Scanner scanner) {
    System.out.println("Your account will be closed. Please confirm");
    System.out.println("1 - Confirm");
    System.out.println("2 - Cancel");
    String selection = scanner.next();
    if (selection.equals("1")) {
      accountService.closeAccount();
    } else if (selection.equals("2")) {
      this.displayAccountMenu(scanner);
    }
  }
  
  private void makeTransaction(TransactionTypes type, Scanner scanner) {
    System.out.println("Capture the amount of the operation ");
    float amount = scanner.nextFloat();
    System.out.println("Type the description of the operation");
    String description = scanner.next();
    accountService.makeTransaction(amount, description, type);    
  }
  
  private void createNewAccount(Scanner scanner) {
    AccountRequest accountRequest = new AccountRequest();
    System.out.println("Capture the firstName");
    accountRequest.setFirstName(scanner.next());
    System.out.println("Capture the lastName");
    accountRequest.setLastName(scanner.next());
    System.out.println("Capture a 4 digit PIN");
    accountRequest.setPin(scanner.nextInt()); 
    System.out.println("Capture your id number");
    accountRequest.setAccountHolderIdNumber(scanner.nextLong());
    Account account = accountService.createAccount(accountRequest);
    System.out.println("Your account number is " + account.getAccountNumber());
    this.displayAccountMenu(scanner);
  }
  

}
