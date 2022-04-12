package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.store.ShortUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SeoShortUrlGeneratorTest {
  @Test
  void testGenerate() {
    String originalUrl = "http://example.com/longUrl/withMultiple/Parts.html";
    String seoKeyword = "mySeoKeyword";
    SeoShortUrlGenerator generator = new SeoShortUrlGenerator();
    ShortUrl shortUrl = generator.generate(originalUrl, seoKeyword);
    Assertions.assertEquals(shortUrl.getShortUrl(), "http://sho.com/"+seoKeyword);
  }
}