package com.jaravir.urlshortener.job;

import com.jaravir.urlshortener.store.ShortUrlStore;

/**
 * Periodic job to delete expired short urls. There are no tests for it because it just delegates
 * to the {@link ShortUrlStore#deleteExpiredShortUrls()} which is tested under its own unit tests.
 */
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
