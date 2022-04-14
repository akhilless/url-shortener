package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.store.ShortUrl;
import java.util.Random;

public class RandomFixedLengthShortUrlGenerator extends ShortUrlGenerator {

  private final int SHORT_URL_LENGTH;
  private final char[] allowedChars;

  public RandomFixedLengthShortUrlGenerator(String domainName, int shortUrlLength,
      char[] allowedChars) {
    super(domainName);
    if (shortUrlLength <= 0) {
      throw new IllegalArgumentException("shortUrlLength must be a positive integer.");
    }

    this.SHORT_URL_LENGTH = shortUrlLength;
    this.allowedChars = allowedChars;
  }

  public ShortUrl generate(String originalUrl) {
    Random rand = new Random();
    StringBuilder sb = new StringBuilder();
    char c;

    for (int i = 0; i < SHORT_URL_LENGTH; ) {
      c = allowedChars[rand.nextInt(allowedChars.length)];
      sb.append(c);
      i++;
    }
    return new ShortUrl(originalUrl, Configuration.getInstance().getDomainName() + sb.toString());
  }
}
