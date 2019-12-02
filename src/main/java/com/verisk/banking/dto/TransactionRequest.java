package com.verisk.banking.dto;

public class TransactionRequest {
  
  private long accountNumber;
  private float amount;
  private String description;
  private int pin;
    
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public long getAccountNumber() {
    return accountNumber;
  }
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }
  public float getAmount() {
    return amount;
  }
  public void setAmount(float amount) {
    this.amount = amount;
  }
  public int getPin() {
    return pin;
  }
  public void setPin(int pin) {
    this.pin = pin;
  }   
}
