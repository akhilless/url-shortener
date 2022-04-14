package com.jaravir.urlshortener.service;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.store.ShortUrlStore;
import com.jaravir.urlshortener.validator.ShortUrlValidator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlShortenerServiceTest {
  final String TEST_URL = "http://example.com/abc/cde/abc.html";
  final String SEO_KEYWORD = "mySeoKeyword";

  @Test
  void testCreateShortUrl_ValidUrl_SeoKeyWordProvided_ShouldUseSeoKeyWord() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD, null);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isSuccess());
    String shortUrl = response.getShortUrl();

    Assertions.assertNotNull(shortUrl);
    Assertions.assertEquals(shortUrl, Configuration.getInstance().getDomainName()+SEO_KEYWORD);
  }

  @Test
  void testCreateShortUrl_InvalidUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    String invalidUrl = "htf:/invalidUrl";
    CreateShortUrlResponse response = service.createShortUrl(invalidUrl, SEO_KEYWORD);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isFailed());
    Assertions.assertEquals(response.getFailureDescription(), "Invalid URL.");
  }

  @Test
  void testCreateShortUrl_NullUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(null, SEO_KEYWORD);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isFailed());
    Assertions.assertEquals(response.getFailureDescription(), "Invalid URL.");
  }

  @Test
  void testCreateShortUrl_EmptyUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl("", SEO_KEYWORD);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isFailed());
    Assertions.assertEquals(response.getFailureDescription(), "Invalid URL.");
  }

  @Test
  void testCreateShortUrl_DuplicateOriginalUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertTrue(response.isSuccess());
    Assertions.assertNotNull(response.getShortUrl());

    CreateShortUrlResponse secondResponse = service.createShortUrl(TEST_URL, SEO_KEYWORD);
    Assertions.assertTrue(secondResponse.isFailed());
    Assertions.assertEquals(secondResponse.getFailureDescription(), "URL " + TEST_URL + " is already mapped to a short URL.");
  }

  @Test
  void testCreateRandomShortUrl_DuplicateOriginalUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL);
    Assertions.assertTrue(response.isSuccess());
    Assertions.assertNotNull(response.getShortUrl());

    CreateShortUrlResponse secondResponse = service.createShortUrl(TEST_URL);
    Assertions.assertTrue(secondResponse.isFailed());
    Assertions.assertEquals(secondResponse.getFailureDescription(), "URL " + TEST_URL + " is already mapped to a short URL.");
  }

  @Test
  void testCreateShortUrl_DuplicateKeyword_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
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
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL);
    Assertions.assertNotNull(response);
    Assertions.assertTrue(response.isSuccess());
    String shortUrl = response.getShortUrl();

    Assertions.assertNotNull(shortUrl);
    Assertions.assertTrue(shortUrl.startsWith(domainName));
    Assertions.assertEquals(domainName.length() + Configuration.getInstance().getRandomShortUrlLength(), shortUrl.length());
  }

  @Test
  void testGetOriginalUrl_ExistingSeoShortUrl_ShouldReturnOriginalUrl() {
    UrlShortenerService service = createUrlShortenerService();
    String seoShortUrl = service.createShortUrl(TEST_URL, SEO_KEYWORD).getShortUrl();
    GetOriginalUrlResponse seoUrlResponse = service.getOriginalUrl(seoShortUrl);
    Assertions.assertNotNull(seoUrlResponse.getShortUrl());
    Assertions.assertTrue(seoUrlResponse.isSuccess());
    Assertions.assertEquals(TEST_URL, seoUrlResponse.getOriginalUrl());
  }

  @Test
  void testGetOriginalUrl_ExistingRandomShortUrl_ShouldReturnOriginalUrl() {
    String originalUrlForRandomShortUrl = "http://example.com/someVeryLongUrl/abc1";
    UrlShortenerService service = createUrlShortenerService();
    String randomShortUrl = service.createShortUrl(originalUrlForRandomShortUrl).getShortUrl();

    GetOriginalUrlResponse randomUrlResponse = service.getOriginalUrl(randomShortUrl);
    Assertions.assertNotNull(randomUrlResponse.getShortUrl());
    Assertions.assertTrue(randomUrlResponse.isSuccess());
    Assertions.assertEquals(originalUrlForRandomShortUrl, randomUrlResponse.getOriginalUrl());
  }

  @Test
  void testGetOriginalUrl_NonExistingShortUrl_ShouldFail() {
    String shortUrl = "http://sho.com/H33K9J";
    UrlShortenerService service = createUrlShortenerService();
    GetOriginalUrlResponse response = service.getOriginalUrl(shortUrl);
    Assertions.assertTrue(response.isFailed());
    Assertions.assertNull(response.getOriginalUrl());
    Assertions.assertEquals("Original URL not found for short url " + shortUrl + ".", response.getFailureDescription());
  }

  @Test
  void testGetOriginalUrl_EmptyShortUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    GetOriginalUrlResponse emptyShortUrlResponse = service.getOriginalUrl("");
    Assertions.assertTrue(emptyShortUrlResponse.isFailed());
    Assertions.assertNull(emptyShortUrlResponse.getOriginalUrl());
    Assertions.assertEquals("Invalid short URL.", emptyShortUrlResponse.getFailureDescription());
  }

  @Test
  void testGetOriginalUrl_NullShortUrl_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    GetOriginalUrlResponse nullShortUrlResponse = service.getOriginalUrl(null);
    Assertions.assertTrue(nullShortUrlResponse.isFailed());
    Assertions.assertNull(nullShortUrlResponse.getOriginalUrl());
    Assertions.assertEquals("Invalid short URL.", nullShortUrlResponse.getFailureDescription());
  }

  @Test
  void testCreateShortUrl_InvalidTimeToLive_ShouldFail() {
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD, LocalDateTime.now().minusHours(5));
    Assertions.assertTrue(response.isFailed());
    Assertions.assertEquals("Time to Live cannot be in the past.", response.getFailureDescription());
  }

  @Test
  void testCreateShortUrl_ValidTimeToLive_ShouldCreateShortUrlWithProvidedTimeToLive() {
    LocalDateTime timeToLive = LocalDateTime.now().plusHours(10);
    UrlShortenerService service = createUrlShortenerService();
    CreateShortUrlResponse response = service.createShortUrl(TEST_URL, SEO_KEYWORD, timeToLive);
    Assertions.assertTrue(response.isSuccess());
    Assertions.assertEquals(TEST_URL, service.getOriginalUrl(response.getShortUrl()).getOriginalUrl());
  }

  private UrlShortenerService createUrlShortenerService() {
    ShortUrlValidator validator = new ShortUrlValidator();
    ShortUrlStore store = new ShortUrlStore();
    return new UrlShortenerService(store, validator);
  }
}