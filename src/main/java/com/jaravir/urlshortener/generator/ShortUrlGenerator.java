package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.store.ShortUrl;

public abstract class ShortUrlGenerator {
  private final String DOMAIN_NAME;

  public ShortUrlGenerator(String domainName) {
    this.DOMAIN_NAME = domainName;
  }

  public abstract ShortUrl generate(String originalUrl);

  protected String getDomainName() {
    return DOMAIN_NAME;
  }
}
