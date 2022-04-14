package com.jaravir.urlshortener.store;

import com.jaravir.urlshortener.config.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShortUrlStoreTest {
  private final String domainName = Configuration.getInstance().getDomainName();

  @Test
  void testSave_ShortUrlDoesNotExist_ShouldSucceed() throws Exception {
    ShortUrl shortUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl");
    ShortUrlStore store = new ShortUrlStore();
    Assertions.assertTrue(store.save(shortUrl));
    Assertions.assertEquals(shortUrl.getOriginalUrl(), store.getOriginalUrl(shortUrl.getShortUrl()));
  }

  @Test
  void testSave_DuplicateShortUrl_ShouldFail() throws Exception {
    ShortUrl shortUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl");
    ShortUrlStore store = new ShortUrlStore();
    Assertions.assertTrue(store.save(shortUrl));

    ShortUrl duplicateShortUrl = new ShortUrl("http://example.com/testOriginalUrl/adbc", domainName+"testShortUrl");
    Assertions.assertThrows(DuplicateShortUrlException.class, () -> {
      store.save(duplicateShortUrl);
    });
  }

  @Test
  void testSave_DuplicateOriginalUrl_ShouldFail() throws Exception {
    ShortUrl shortUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl");
    ShortUrlStore store = new ShortUrlStore();
    Assertions.assertTrue(store.save(shortUrl));

    ShortUrl duplicateOriginalUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl23");
    Assertions.assertThrows(DuplicateOriginalUrlException.class, () -> {
      store.save(duplicateOriginalUrl);
    });
  }

  @Test
  void testGetOriginalUrl_ShortUrlDoesNotExist_ShouldFail() {
    ShortUrl shortUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl");
    ShortUrlStore store = new ShortUrlStore();
    Assertions.assertThrows(ShortUrlNotFoundException.class, () -> {
      store.getOriginalUrl(shortUrl.getShortUrl());
    });
  }
}