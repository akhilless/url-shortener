package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.store.ShortUrl;

public class SeoShortUrlGenerator extends ShortUrlGenerator {
  private final String SEO_KEYWORD;

  public SeoShortUrlGenerator(String domainName, String seoKeyWord) {
    super(domainName);
    this.SEO_KEYWORD = seoKeyWord;
  }

  public ShortUrl generate(String originalUrl) {
    return new ShortUrl(originalUrl, getDomainName() + SEO_KEYWORD);
  }
}
