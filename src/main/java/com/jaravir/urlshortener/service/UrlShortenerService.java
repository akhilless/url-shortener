package com.jaravir.urlshortener.service;

import com.jaravir.urlshortener.config.Configuration;
import com.jaravir.urlshortener.generator.ShortUrlGenerator;
import com.jaravir.urlshortener.generator.ShortUrlGeneratorFactory;
import com.jaravir.urlshortener.store.DuplicateOriginalUrlException;
import com.jaravir.urlshortener.store.DuplicateShortUrlException;
import com.jaravir.urlshortener.store.ShortUrl;
import com.jaravir.urlshortener.store.ShortUrlNotFoundException;
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
    ValidationResult validationResult = validator.validateOriginalUrl(originalUrl);

    if (validationResult.isFailed()) {
      response.setFailureDescription(validationResult.getFailureDescription());
      return response;
    }

    ShortUrlGenerator generator = ShortUrlGeneratorFactory.getInstance().createShortUrlGenerator(seoKeyword);
    ShortUrl shortUrl = generator.generate(originalUrl);

    //regenerate if random short url and failed to save due to duplication
    try {
      store.save(shortUrl);
    } catch (DuplicateShortUrlException ex) {
      if (seoKeyword != null && !seoKeyword.isEmpty()) {
        response.setFailureDescription(
            "Keyword " + seoKeyword + " is already in use. Please use a different one.");
      } else {
        response.setFailureDescription(
            "Failed to create a short url. Please try again later.");
      }
    } catch (DuplicateOriginalUrlException ex) {
      response.setFailureDescription("URL " + originalUrl + " is already mapped to a short URL.");
    }

    if (response.isSuccess()) {
      response.setShortUrl(shortUrl.getShortUrl());
    }

    return response;
  }

  public GetOriginalUrlResponse getOriginalUrl(String shortUrl) {
    GetOriginalUrlResponse response = new GetOriginalUrlResponse(shortUrl);
    ValidationResult validationResult = validator.validateShortUrl(shortUrl);

    if (validationResult.isFailed()) {
      response.setFailureDescription(validationResult.getFailureDescription());
      return response;
    }

    try {
      response.setOriginalUrl(store.getOriginalUrl(shortUrl));
    } catch (ShortUrlNotFoundException ex) {
      response.setFailureDescription("Original URL not found for short url " + shortUrl + ".");
    }

    return response;
  }
}