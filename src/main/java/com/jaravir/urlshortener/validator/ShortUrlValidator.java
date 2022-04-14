package com.jaravir.urlshortener.validator;

import com.jaravir.urlshortener.config.Configuration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Provides very simple validation for long (original) URLS and time to live datetime objects.
 * It is a stub for a validation framework that would normally be used in a production project
 * (like Apache Commons Validator, etc.).
 */
public class ShortUrlValidator {
  private final Pattern VALID_URL_PATTERN = Pattern.compile("(https?://)?(www\\.)?[a-zA-Z0-9]+\\.[a-z]{2,3}/[a-zA-Z0-9/]+(/|(\\.html?))?");
  private static final String ORIGINAL_URL_VALIDATION_FAILURE_DESCRIPTION = "Invalid URL.";
  private static final String SHORT_URL_VALIDATION_FAILURE_DESCRIPTION = "Invalid short URL.";
  private static final String OLD_DATE_VALIDATION_FAILURE_DESCRIPTION = "Time to Live cannot be in the past.";

  public ValidationResult validateOriginalUrl(String originalUrl) {
    ValidationResult result = new ValidationResult();
    if (originalUrl == null || !VALID_URL_PATTERN.matcher(originalUrl).matches()) {
      result.setValidationFailureDescription(ORIGINAL_URL_VALIDATION_FAILURE_DESCRIPTION);
    }
    return result;
  }

  public ValidationResult validateTimeToLive(LocalDateTime timeToLive) {
    ValidationResult result = new ValidationResult();
    if (timeToLive != null && timeToLive.isBefore(LocalDateTime.now())) {
      result.setValidationFailureDescription(OLD_DATE_VALIDATION_FAILURE_DESCRIPTION);
    }
    return result;
  }

  public ValidationResult validateShortUrl(String shortUrl) {
    String domainName = Configuration.getInstance().getDomainName();
    ValidationResult result = new ValidationResult();
    if (shortUrl == null || shortUrl.isEmpty() || !shortUrl.startsWith(domainName)) {
      result.setValidationFailureDescription(SHORT_URL_VALIDATION_FAILURE_DESCRIPTION);
    }
    return result;
  }
}
