package com.jaravir.urlshortener.store;

import java.util.Objects;

public class ShortUrl {
  private final String originalUrl;
  private final String shortUrl;

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
