package com.challenge.datastax.s3proxy.exception;

import java.util.UUID;

public class NotFoundUploadException extends Exception {

  public NotFoundUploadException(UUID itemIdentifier) {
    super("Not found upload for " + itemIdentifier.toString());
  }
}
