package com.jaravir.urlshortener.job;

import com.jaravir.urlshortener.store.ShortUrlStore;

public class DeleteExpiredShortUrlsJob implements Runnable {
  private ShortUrlStore store;

  public DeleteExpiredShortUrlsJob(ShortUrlStore store) {
    this.store = store;
  }

  @Override
  public void run() {
    store.deleteExpiredShortUrls();
  }
}
