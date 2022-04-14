package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.store.ShortUrl;

/**
 * Abstract class serving as the root of the inheritance tree of short url generators.
 * Contains common fields and methods.
 */
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
