package com.verisk.banking;

import java.util.InputMismatchException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Profile;
import com.verisk.banking.dto.AccountRequest;
import com.verisk.banking.jpa.Account;
import com.verisk.banking.service.AccountService;
import com.verisk.banking.util.CurrentAccountHolder;
import com.verisk.banking.util.TransactionTypes;

@SpringBootApplication
@Profile("!test")
public class AccountManagement extends SpringBootServletInitializer implements CommandLineRunner{
  
  Scanner scanner;
  
  @Autowired
  private AccountService accountService;

  public static void main(String[] args) {
    SpringApplication.run(AccountManagement.class, args);
  }
  
  @Override
  public void run(String...args) throws Exception {
    this.displayInitialMenu();
  }  
  
  private void displayInitialMenu() {
    scanner = new Scanner(System.in);
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
    } else {
      System.out.println("Invalid option");
      this.displayInitialMenu();
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
      } else if (selection.equals("2")) {
        this.displayInitialMenu();
      } else {
        System.out.println("Invalid option");
      }
    }
  }
  
  private void displayAccountMenu(Scanner scanner) {
    System.out.println("Select a choice:");
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
        break;
      case "3":
        System.out.println("Your current balance is " + accountService.getCurrentBalance());
        this.displayAccountMenu(scanner);
        break;
      case "4":
        this.closeAccount(scanner);
        break;
      case "5":
        this.logout(scanner);
        break;
      default: 
        System.out.println("Invalid option");
        this.displayAccountMenu(scanner);
        break;
    }
  }
  
  private void logout(Scanner scanner) {
    CurrentAccountHolder.setCurrentAccount(null);
    this.displayInitialMenu();
  }
  
  private void closeAccount(Scanner scanner) {
    System.out.println("Your account will be closed. Please confirm");
    System.out.println("1 - Confirm");
    System.out.println("2 - Cancel");
    String selection = scanner.next();
    if (selection.equals("1")) {
      boolean isClosed = accountService.closeAccount();
      if (isClosed) {
        this.displayInitialMenu();        
      } else {
        this.displayAccountMenu(scanner);
      }
    } else if (selection.equals("2")) {
      this.displayAccountMenu(scanner);
    } else {
      System.out.println("Invalid Option");
      this.closeAccount(scanner);
    }
  }
  
  private void makeTransaction(TransactionTypes type, Scanner scanner) {
    System.out.println("Capture the amount of the operation ");
    float amount = 0;
    while (amount == 0) {
      try {
        amount = scanner.nextFloat();      
      } catch(InputMismatchException e) {
        System.out.println("For amount, capture a numeric value greather than 0");
        scanner = new Scanner(System.in);
      }
    }
    System.out.println("Type the description of the operation");
    String description = scanner.next();
    try {
      accountService.makeTransactionFromConsole(amount, description, type);          
    } catch(Exception e ) {
      System.out.println("Transaction could not be performed. Try again later ");
    }
    this.displayAccountMenu(scanner);
  }
  
  private void createNewAccount(Scanner scanner) {
    AccountRequest accountRequest = new AccountRequest();
    System.out.println("Capture the firstName");
    accountRequest.setFirstName(scanner.next());
    System.out.println("Capture the lastName");
    accountRequest.setLastName(scanner.next());
    System.out.println("Capture a 4 digit PIN");
    try {
      int pin = scanner.nextInt();
      while (pin < 1000 || pin > 9999) {
        System.out.println("PIN should be 4 digits. Please type a correct PIN");
        pin = scanner.nextInt();
      }
      accountRequest.setPin(pin); 
      System.out.println("Capture your id number");
      accountRequest.setAccountHolderIdNumber(scanner.nextLong());
    } catch(InputMismatchException e) {
      System.out.println("Account could not be created. Use numeric values for the Id number and the PIN");
      scanner = new Scanner(System.in);
      this.createNewAccount(scanner);
    }
    Account account = null;
    try {
      account = accountService.createAccount(accountRequest);      
    } catch(Exception e) {
      System.out.println("Account could not be created. Try again later");
      this.displayInitialMenu();
    }
    System.out.println("Your account number is " + account.getAccountNumber());
    this.displayAccountMenu(scanner);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(AccountManagement.class);
  }
  

}
