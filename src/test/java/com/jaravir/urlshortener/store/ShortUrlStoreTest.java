package com.jaravir.urlshortener.store;

import com.jaravir.urlshortener.config.Configuration;
import java.time.LocalDateTime;
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

  @Test
  void testDeleteExpiredShortUrls_ShouldOnlyDeleteExpiredUrls() throws Exception {
    ShortUrl expiredShortUrl = new ShortUrl("http://example.com/testOriginalUrl", domainName+"testShortUrl");
    expiredShortUrl.setTimeToLive(LocalDateTime.now().minusHours(3));

    ShortUrl shortUrlWithoutExpiration = new ShortUrl("http://example.com/testOriginalUrl/2abc", domainName+"testShortUrl2");

    ShortUrl shortUrlWithTimeToLiveInTheFuture = new ShortUrl("http://example.com/testOriginalUrl/3cde", domainName+"testShortUrl3");
    shortUrlWithTimeToLiveInTheFuture.setTimeToLive(LocalDateTime.now().plusMinutes(20));

    ShortUrlStore store = new ShortUrlStore();

    Assertions.assertTrue(store.save(expiredShortUrl));
    Assertions.assertTrue(store.save(shortUrlWithoutExpiration));
    Assertions.assertTrue(store.save(shortUrlWithTimeToLiveInTheFuture));

    store.deleteExpiredShortUrls();

    Assertions.assertEquals(shortUrlWithoutExpiration.getOriginalUrl(), store.getOriginalUrl(shortUrlWithoutExpiration.getShortUrl()));
    Assertions.assertEquals(shortUrlWithTimeToLiveInTheFuture.getOriginalUrl(), store.getOriginalUrl(shortUrlWithTimeToLiveInTheFuture.getShortUrl()));
    Assertions.assertThrows(ShortUrlNotFoundException.class, () -> {
      store.getOriginalUrl(expiredShortUrl.getShortUrl());
    });
  }
}