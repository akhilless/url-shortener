package com.jaravir.urlshortener.service;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.store.ShortUrlStore;
import com.jaravir.urlshortener.validator.ShortUrlValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlShortenerServiceTest {
  final String TEST_URL = "http://example.com/abc/cde/abc.html";
  final String SEO_KEYWORD = "mySeoKeyword";

  @Test
  void testCreateShortUrl_ValidUrl_SeoKeyWordProvided_ShouldUseSeoKeyWord() {
    UrlShortenerService service = createShortUrlService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isSuccess());
    String shortUrl = response.getShortUrl();

    Assertions.assertNotNull(shortUrl);
    Assertions.assertEquals(shortUrl, Configuration.getInstance().getDomainName()+SEO_KEYWORD);
  }

  @Test
  void testCreateShortUrl_InvalidUrl_ShouldFail() {
    UrlShortenerService service = createShortUrlService();
    String invalidUrl = "htf:/invalidUrl";
    CreateShortUrlResponse response = service.createShortUrl(invalidUrl, SEO_KEYWORD);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isFailed());
    Assertions.assertEquals(response.getFailureDescription(), "Invalid URL.");
  }

  @Test
  void testCreateShortUrl_DuplicateOriginalUrl_ShouldFail() {
    UrlShortenerService service = createShortUrlService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertTrue(response.isSuccess());
    Assertions.assertNotNull(response.getShortUrl());

    CreateShortUrlResponse secondResponse = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertTrue(secondResponse.isFailed());
    Assertions.assertEquals(secondResponse.getFailureDescription(), "URL " + TEST_URL + " is already mapped to a short URL.");
  }

  @Test
  void testCreateShortUrl_DuplicateKeyword_ShouldFail() {
    UrlShortenerService service = createShortUrlService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertTrue(response.isSuccess());
    Assertions.assertNotNull(response.getShortUrl());

    CreateShortUrlResponse secondResponse = service.createShortUrl("http://example2.com/test/long/url", SEO_KEYWORD);
    Assertions.assertTrue(secondResponse.isFailed());
    Assertions.assertEquals(secondResponse.getFailureDescription(), "Keyword " + SEO_KEYWORD + " is already in use. Please use a different one.");
  }

  @Test
  void testCreateShortUrl_NoKeyword_ShouldGenerateRandomShortUrl() {
    String domainName = Configuration.getInstance().getDomainName();
    UrlShortenerService service = createShortUrlService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, null);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isSuccess());
    String shortUrl = response.getShortUrl();

    Assertions.assertNotNull(shortUrl);
    Assertions.assertTrue(shortUrl.startsWith(domainName));
    Assertions.assertEquals(domainName.length() + Configuration.getInstance().getRandomShortUrlLength(), shortUrl.length());
  }

  private UrlShortenerService createShortUrlService() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ShortUrlStore store = new ShortUrlStore();
    return new UrlShortenerService(store, validator);
  }
}