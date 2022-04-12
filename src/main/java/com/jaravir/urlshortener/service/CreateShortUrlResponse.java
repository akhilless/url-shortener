package com.jaravir.urlshortener.service;

public class CreateShortUrlResponse {
  private final String originalUrl;
  private String shortUrl;
  private String failureDescription;

  public CreateShortUrlResponse(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setFailureDescription(String failureDescription) {
    this.failureDescription = failureDescription;
  }

  public String getFailureDescription() {
    return failureDescription;
  }

  public boolean isFailed() {
    return failureDescription != null;
  }

  public boolean isSuccess() {
    return !isFailed();
  }
}
