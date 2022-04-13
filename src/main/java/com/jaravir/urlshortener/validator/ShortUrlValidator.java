package com.jaravir.urlshortener.validator;

import java.util.regex.Pattern;

public class ShortUrlValidator {
  private final Pattern VALID_URL_PATTERN = Pattern.compile("(https?://)?(www\\.)?[a-zA-Z0-9]+\\.[a-z]{2,3}/[a-zA-Z0-9/]+(/|(\\.html?))?");
  private static final String VALIDATION_FAILURE_DESCRIPTION = "Invalid URL.";

  public ValidationResult validate(String url) {
    ValidationResult result = new ValidationResult();
    if (url == null || !VALID_URL_PATTERN.matcher(url).matches()) {
      result.setValidationFailureDescription(VALIDATION_FAILURE_DESCRIPTION);
    }
    return result;
  }
}
