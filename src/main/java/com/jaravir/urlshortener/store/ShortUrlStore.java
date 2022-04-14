package com.jaravir.urlshortener.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Models data storage for short urls. It uses locks to provide thread-safety as its instance is expected
 * to be shared between multiple threads. {@link ShortUrlStore#store} is a stub for a database table
 * `shorturl` and {@link ShortUrlStore#short2originalCache} is a stub for an index on shorturl on that
 * table while locks are a stub for the transaction isolation mechanism of the database. In a real-life
 * situation when the class uses database for storage none of the maps and locks
 * would be necessary if the class does not cache any data. Then the class will rely on transaction isolation
 * mechanism of the database.
 */
public class ShortUrlStore {

  private Map<String, ShortUrl> store = new HashMap<>();
  private Map<String, String> short2originalCache = new HashMap<>();
  private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  public boolean save(ShortUrl shortUrl)
      throws DuplicateOriginalUrlException, DuplicateShortUrlException {
    readWriteLock.writeLock().lock();
    try {
      //original url is already mapped or the short url is already used
      if (store.containsKey(shortUrl.getOriginalUrl())) {
        throw new DuplicateOriginalUrlException();
      }

      if (short2originalCache.containsKey(shortUrl.getShortUrl())) {
        throw new DuplicateShortUrlException();
      }

      store.put(shortUrl.getOriginalUrl(), shortUrl);
      short2originalCache.put(shortUrl.getShortUrl(), shortUrl.getOriginalUrl());
      return true;
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  public String getOriginalUrl(String shortUrl) throws ShortUrlNotFoundException {
    readWriteLock.readLock().lock();
    try {
      String originalUrl = short2originalCache.get(shortUrl);

      if (originalUrl == null) {
        throw new ShortUrlNotFoundException();
      }

      return originalUrl;
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  public void deleteExpiredShortUrls() {
    readWriteLock.readLock().lock();
    List<ShortUrl> expiredShortUrls;

    try {
      expiredShortUrls = store.values().stream().filter(ShortUrl::isExpired).collect(
          Collectors.toList());
    } finally {
      readWriteLock.readLock().unlock();
    }

    for (ShortUrl expiredShortUrl : expiredShortUrls) {
      readWriteLock.writeLock().lock();
      try {
        store.remove(expiredShortUrl.getOriginalUrl());
        short2originalCache.remove(expiredShortUrl.getShortUrl());
      } finally {
        readWriteLock.writeLock().unlock();
      }
    }
  }
}
