package com.verisk.banking.jpa;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "account")
public class Account {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long accountNumber;
  private String firstName;
  private String lastName;
  private int pin;
  private long holderIdNumber;
  private float currentBalance;
  @Version
  private Integer version;  
  
  @OneToMany(mappedBy="account", fetch = FetchType.LAZY)
  private List<Transaction> transactions;

  private boolean deleted;
  
  public long getAccountNumber() {
    return accountNumber;
  }
  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }
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
  public long getHolderIdNumber() {
    return holderIdNumber;
  }
  public void setHolderIdNumber(long holderIdNumber) {
    this.holderIdNumber = holderIdNumber;
  }
  public float getCurrentBalance() {
    return currentBalance;
  }
  public void setCurrentBalance(float currentBalance) {
    this.currentBalance = currentBalance;
  }
  public List<Transaction> getTransactions() {
    return transactions;
  }
  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }
  public boolean isDeleted() {
    return deleted;
  }
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
  public Integer getVersion() {
    return version;
  }
  public void setVersion(Integer version) {
    this.version = version;
  }    

}
