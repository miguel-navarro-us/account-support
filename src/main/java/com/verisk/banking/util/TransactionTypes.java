package com.verisk.banking.util;

public enum TransactionTypes {
  
  
    DEPOSIT("deposit"), WITHDRAWAL("withdrawal"), DEBIT("debit"), CHECK("check");
  
  private final String label;

  TransactionTypes(String label) {
    this.label = label;
  }
  
  public String getLabel() {
    return label;
  }
}
