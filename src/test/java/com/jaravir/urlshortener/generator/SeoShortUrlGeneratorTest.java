package com.jaravir.urlshortener.generator;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.store.ShortUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SeoShortUrlGeneratorTest {
  @Test
  void testGenerate() {
    String originalUrl = "http://example.com/longUrl/withMultiple/Parts.html";
    String seoKeyword = "mySeoKeyword";
    SeoShortUrlGenerator generator = new SeoShortUrlGenerator(Configuration.getInstance()
        .getDomainName(), seoKeyword);
    ShortUrl shortUrl = generator.generate(originalUrl);
    Assertions.assertEquals(shortUrl.getShortUrl(), "http://sho.com/"+seoKeyword);
  }
}