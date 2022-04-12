package com.jaravir.urlshortener.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShortUlrValidatorTest {
  @Test
  void testValidation_InvalidUrl_ShouldFail() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validate("htp:/invalidurl");
    Assertions.assertTrue(result.isFailed());
  }

  @Test
  void testValidUrl_ValidUrl_ShouldValidateSuccessfully() {
    String validUrl = "http://example.com/longUrl/sh0uldBe/Validated.html";
    ShortUrlValidator validator = new ShortUrlValidator();
    ValidationResult result = validator.validate(validUrl);
    Assertions.assertTrue(result.isPassed());
  }
}
