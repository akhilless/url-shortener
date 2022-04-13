package com.jaravir.urlshortener.config;

public class Configuration {
  private static Configuration instance;
  private final String DOMAIN_NAME = "http://sho.com/";
  private final int RANDOM_SHORT_URL_LENGTH = 6;
  private char[] allowedChars;

  private Configuration() {
    populateAllowedChars();
  }

  public static Configuration getInstance() {
    if (instance == null) {
      instance = new Configuration();
    }

    return instance;
  }

  public String getDomainName() {
    return DOMAIN_NAME;
  }

  public int getRandomShortUrlLength() {
    return RANDOM_SHORT_URL_LENGTH;
  }

  public char[] getAllowedChars() {
    return allowedChars;
  }

  private void populateAllowedChars() {
    allowedChars = new char[62];
    int j = 0;

    for (int i = 0; i < 10; i++, j++) { // add digits 0-9
      allowedChars[j] = Character.forDigit(i, 10);
    }

    for (int i = 'A'; i <= 'Z'; i++, j++) { // add upper-case letters
      allowedChars[j] = (char) i;
    }

    for (int i = 'a'; i <= 'z'; i++, j++) { // add lowercase letters
      allowedChars[j] = (char) i;
    }
  }
}
