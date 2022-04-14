package com.jaravir.urlshortener.store;

import java.time.LocalDateTime;
import java.util.Objects;

public class ShortUrl {
  private final String originalUrl;
  private final String shortUrl;
  private LocalDateTime timeToLive;

  public ShortUrl(String originalUrl, String shortUrl) {
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public LocalDateTime getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(LocalDateTime timeToLive) {
    this.timeToLive = timeToLive;
  }

  public boolean isExpired() {
    return timeToLive != null && timeToLive.isBefore(LocalDateTime.now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShortUrl shortUrl1 = (ShortUrl) o;
    return originalUrl.equals(shortUrl1.originalUrl) && shortUrl.equals(shortUrl1.shortUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalUrl, shortUrl);
  }
}
