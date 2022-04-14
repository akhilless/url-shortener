package com.jaravir.urlshortener.validator;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShortUlrValidatorTest {
  @Test
  void testValidateOriginalUrl_InvalidUrl_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateOriginalUrl("htp:/invalidurl");
    Assertions.assertTrue(result.isFailed());
    Assertions.assertEquals("Invalid URL.", result.getFailureDescription());
  }

  @Test
  void testValidateOriginalUrl_ValidUrl_ShouldValidateSuccessfully() {
    String validUrl = "http://example.com/longUrl/sh0uldBe/Validated.html";
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateOriginalUrl(validUrl);
    Assertions.assertTrue(result.isPassed());
    Assertions.assertNull(result.getFailureDescription());
  }

  @Test
  void testValidateShortUrl_ValidShortUrl_ShouldValidateSuccessfully() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateShortUrl("http://sho.com/H33K9J");
    Assertions.assertTrue(result.isPassed());
    Assertions.assertNull(result.getFailureDescription());
  }

  @Test
  void testValidateShortUrl_NullShortUrl_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateShortUrl(null);
    Assertions.assertTrue(result.isFailed());
    Assertions.assertEquals("Invalid short URL.", result.getFailureDescription());
  }

  @Test
  void testValidateShortUrl_EmptyShortUrl_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateShortUrl("");
    Assertions.assertTrue(result.isFailed());
    Assertions.assertEquals("Invalid short URL.", result.getFailureDescription());
  }

  @Test
  void testValidateShortUrl_WrongDomainName_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateShortUrl("http://example.com/H33K9J");
    Assertions.assertTrue(result.isFailed());
    Assertions.assertEquals("Invalid short URL.", result.getFailureDescription());
  }

  @Test
  void testValidateTimeToLive_OldDate_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validateTimeToLive(LocalDateTime.now().minusMinutes(2));
    Assertions.assertEquals("Time to Live cannot be in the past.", result.getFailureDescription());
  }
}
