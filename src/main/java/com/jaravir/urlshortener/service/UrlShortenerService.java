package com.jaravir.urlshortener.service;

import com.jaravir.urlshortener.generator.SeoShortUrlGenerator;
import com.jaravir.urlshortener.store.DuplicateOriginalUrlException;
import com.jaravir.urlshortener.store.DuplicateShortUrlException;
import com.jaravir.urlshortener.store.ShortUrl;
import com.jaravir.urlshortener.store.ShortUrlStore;
import com.jaravir.urlshortener.validator.ShortUrlValidator;
import com.jaravir.urlshortener.validator.ValidationResult;

public class UrlShortenerService {
  private final ShortUrlStore store;
  private final ShortUrlValidator validator;

  public UrlShortenerService(ShortUrlStore store, ShortUrlValidator validator) {
    this.store = store;
    this.validator = validator;
  }

  public CreateShortUrlResponse createShortUrl(String originalUrl, String seoKeyword) {
    CreateShortUrlResponse response = new CreateShortUrlResponse(originalUrl);
    ValidationResult validationResult = validator.validate(originalUrl);

    if (validationResult.isFailed()) {
      response.setFailureDescription(validationResult.getFailureDescription());
      return response;
    }

    SeoShortUrlGenerator generator = new SeoShortUrlGenerator();
    ShortUrl shortUrl = generator.generate(originalUrl, seoKeyword);

    try {
      store.save(originalUrl, shortUrl);
    } catch (DuplicateShortUrlException ex) {
      response.setFailureDescription("Keyword " + seoKeyword + " is already in use. Please use a different one.");
    } catch (DuplicateOriginalUrlException ex) {
      response.setFailureDescription("URL " + originalUrl + " is already mapped to a short URL.");
    }

    if (response.isSuccess()) {
      response.setShortUrl(shortUrl.getShortUrl());
    }

    return response;
  }
}