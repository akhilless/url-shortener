package com.jaravir.urlshortener.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
