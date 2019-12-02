package com.verisk.banking.dto;

public class TransactionResult {
  
  private String result;
  private String description;
  private long transactionId;
  public String getResult() {
    return result;
  }
  public void setResult(String result) {
    this.result = result;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public long getTransactionId() {
    return transactionId;
  }
  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }
  
  

}
