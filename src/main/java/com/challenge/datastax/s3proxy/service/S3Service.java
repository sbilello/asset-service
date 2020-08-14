package com.challenge.datastax.s3proxy.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

  private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

  @Value("${s3.bucket-name}")
  private String bucketName;

  private final AmazonS3 amazonS3;

  @Autowired
  public S3Service(AmazonS3 amazonS3) {
    this.amazonS3 = amazonS3;
  }

  public String generateSignedUrl(UUID userId, UUID itemKey, HttpMethod httpMethod, int expirationDelaySecond) {
    Date expirationDelay = Date.from(Instant.now().plus(expirationDelaySecond, ChronoUnit.SECONDS));
    final String key = generateKey(userId, itemKey);
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, key)
            .withExpiration(expirationDelay)
            .withMethod(httpMethod);
    try {
      URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
      String urlString = url.toString();
      return urlString;
    } catch (Exception exception) {
      logger.error("It was not possible to get the URL for {}", exception.getMessage());
      throw exception;
    }
  }

  private String generateKey(UUID userId, UUID itemKey) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(userId.toString());
    stringBuilder.append("/");
    stringBuilder.append(itemKey.toString());
    return stringBuilder.toString();
  }


}