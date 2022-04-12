package com.jaravir.urlshortener.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ShortUrlStore {
  private Map<String, ShortUrl> store = new HashMap<>();
  private Map<String, String> short2originalCache = new HashMap<>();
  private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  public boolean save(String originalUrl, ShortUrl shortUrl) throws DuplicateOriginalUrlException, DuplicateShortUrlException {
    readWriteLock.writeLock().lock();
    try {
      //original url is already mapped or the short url is already used
      if (store.containsKey(originalUrl)) {
        throw new DuplicateOriginalUrlException();
      }

      if (short2originalCache.containsKey(shortUrl.getShortUrl())) {
        throw new DuplicateShortUrlException();
      }

      store.put(originalUrl, shortUrl);
      short2originalCache.put(shortUrl.getShortUrl(), originalUrl);
      return true;
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }
}
