package com.jaravir.urlshortener.service;

public class GetOriginalUrlResponse {
  private final String shortUrl;
  private String originalUrl;
  private String failureDescription;

  public GetOriginalUrlResponse(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
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
