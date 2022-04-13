package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.config.Configuration;

public class ShortUrlGeneratorFactory {
  private static ShortUrlGeneratorFactory instance;
  private ShortUrlGeneratorFactory() {}

  public static ShortUrlGeneratorFactory getInstance() {
    if (instance == null) {
      instance = new ShortUrlGeneratorFactory();
    }
    return instance;
  }

  public ShortUrlGenerator createShortUrlGenerator(String seoKeyword) {
    String domainName = Configuration.getInstance().getDomainName();
    int randomShortUrlLength = Configuration.getInstance().getRandomShortUrlLength();

    if (seoKeyword != null && !seoKeyword.isEmpty()) {
      return new SeoShortUrlGenerator(domainName, seoKeyword);
    } else {
      return new RandomFixedLengthShortUrlGenerator(domainName, randomShortUrlLength, Configuration.getInstance()
          .getAllowedChars());
    }
  }
}
