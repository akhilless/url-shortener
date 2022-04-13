package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.store.ShortUrl;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RandomFixedLengthShortUrlGeneratorTest {
  private final String DOMAIN_NAME = Configuration.getInstance().getDomainName();
  private final int LENGTH = Configuration.getInstance().getRandomShortUrlLength();
  private final String TEST_URL = "http://somews.com/abc/1/page.html";

  @Test
  void testGenerate_ShortUrlShouldContainAllowedCharsOnly() {
    char[] allowedChars = Configuration.getInstance().getAllowedChars();
    RandomFixedLengthShortUrlGenerator generator = new RandomFixedLengthShortUrlGenerator(DOMAIN_NAME, LENGTH, allowedChars);
    ShortUrl shortUrl = generator.generate(TEST_URL);
    Assertions.assertEquals(TEST_URL, shortUrl.getOriginalUrl());
    String url = shortUrl.getShortUrl();
    String randomPart = url.substring(url.lastIndexOf("/")+1);
    Assertions.assertEquals(LENGTH, randomPart.length());

    Set<Character> allowedCharsCache = new HashSet<>();

    for (char c : allowedChars) {
      allowedCharsCache.add(c);
    }

    for (char c : randomPart.toCharArray()) {
      Assertions.assertTrue(allowedCharsCache.contains(c));
    }
  }

  @Test
  void testGenerate_TwoInvocationsShouldProduceDifferentShortUrls() {
    char[] allowedChars = Configuration.getInstance().getAllowedChars();
    RandomFixedLengthShortUrlGenerator generator = new RandomFixedLengthShortUrlGenerator(DOMAIN_NAME,LENGTH, allowedChars);
    Assertions.assertNotEquals(generator.generate(TEST_URL), generator.generate(TEST_URL));
  }
}