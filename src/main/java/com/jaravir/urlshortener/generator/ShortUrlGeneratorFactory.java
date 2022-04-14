package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.config.Configuration;

/**
 * Factory to hide the complexity of the short url generator classes and their initialization logic.
 * This factory together with ShortUrlGenerator subclasses implements Factory and Strategy design patterns
 * to ensure that the source code of the existing short url generators are closed (as in SOLID's
 * Open-CLose principle) to adding new short url generators.
 *
 */
public class ShortUrlGeneratorFactory {

  private static ShortUrlGeneratorFactory instance;

  private ShortUrlGeneratorFactory() {
  }

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
      return new RandomFixedLengthShortUrlGenerator(domainName, randomShortUrlLength,
          Configuration.getInstance()
              .getAllowedChars());
    }
  }
}
