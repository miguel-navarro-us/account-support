package com.verisk.banking.dto;

public class AccountRequest {
  
  private String firstName;
  private String lastName;
  private int pin;
  private long accountHolderIdNumber;
  
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public int getPin() {
    return pin;
  }
  public void setPin(int pin) {
    this.pin = pin;
  }
  public long getAccountHolderIdNumber() {
    return accountHolderIdNumber;
  }
  public void setAccountHolderIdNumber(long accountHolderIdNumber) {
    this.accountHolderIdNumber = accountHolderIdNumber;
  }    
}
