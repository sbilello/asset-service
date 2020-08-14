package com.challenge.datastax.s3proxy.exception;

import java.util.UUID;

public class NotFoundCompleteUploadException extends Exception {

  public NotFoundCompleteUploadException(UUID itemIdentifier) {
    super("Upload not completed for " + itemIdentifier.toString());
  }
}
