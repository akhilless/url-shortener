package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.store.ShortUrl;

public class SeoShortUrlGenerator {
  private static final String DOMAIN_NAME = "http://sho.com/";

  public ShortUrl generate(String originalUrl, String seoKeyword) {
    return new ShortUrl(originalUrl, DOMAIN_NAME + seoKeyword);
  }
}
