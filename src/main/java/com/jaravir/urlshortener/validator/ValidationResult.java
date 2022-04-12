package com.jaravir.urlshortener.validator;

public class ValidationResult {
  private String validationFailureDescription;

  public boolean isPassed() {
    return validationFailureDescription == null;
  }

  public boolean isFailed() {
    return validationFailureDescription != null;
  }

  public String getFailureDescription() {
    return validationFailureDescription;
  }

  public void setValidationFailureDescription(String validationFailureDescription) {
    this.validationFailureDescription = validationFailureDescription;
  }
}
