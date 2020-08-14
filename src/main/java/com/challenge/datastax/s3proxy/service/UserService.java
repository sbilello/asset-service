package com.challenge.datastax.s3proxy.service;

import com.amazonaws.HttpMethod;
import com.challenge.datastax.s3proxy.exception.NotFoundCompleteUploadException;
import com.challenge.datastax.s3proxy.exception.NotFoundUploadException;
import com.challenge.datastax.s3proxy.model.Status;
import com.challenge.datastax.s3proxy.model.UserUrl;
import com.challenge.datastax.s3proxy.repository.UserUploadRepository;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

  private final UserUploadRepository userUploadRepository;
  private final S3Service s3Service;

  @Autowired
  public UserService(UserUploadRepository userUploadRepository, S3Service s3Service) {
    this.userUploadRepository = userUploadRepository;
    this.s3Service = s3Service;
  }

  public Mono<UserUrl> createUserUrl(UUID userId, int timeout) {
    return Mono.fromCallable(() -> {
      UUID itemIdentifier = UUIDGen.getTimeUUID();
      String url = s3Service.generateSignedUrl(userId, itemIdentifier, HttpMethod.POST, timeout);
      UserUrl userUrl = UserUrl.buildUserUrl(userId, itemIdentifier, url, Status.TO_UPLOAD);
      return userUrl;
    }).flatMap(userUploadRepository::save);
  }

  public Mono<Boolean> isUserUploadCompleted(UUID userId, UUID itemIdentifier) {
    return getUserUpload(userId, itemIdentifier)
        .map(userUpload -> Status.UPLOAD_COMPLETED.equals(userUpload.getStatus()));
  }

  public Mono<UserUrl> getUserUpload(UUID userId, UUID itemIdentifier) {
    return userUploadRepository.findUserUploadByUserIdAndAndItemIdentifier(userId, itemIdentifier)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundUploadException(itemIdentifier))));
  }

  public Mono<UserUrl> updateUserUrl(UUID userId, UUID itemIdentifier, Status status) {
    return getUserUpload(userId, itemIdentifier)
        .map(userUpload -> {
          userUpload.setStatus(status);
          return userUpload;
        })
        .flatMap(userUploadRepository::save)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundUploadException(itemIdentifier))));
  }

  public Mono<UserUrl> downloadUrl(UUID userId, UUID itemIdentifier, int timeout) {
    return isUserUploadCompleted(userId, itemIdentifier)
        .flatMap(uploadComplete -> {
          if (Boolean.TRUE.equals(uploadComplete)) {
            return Mono.fromCallable(() -> {
              String url = s3Service.generateSignedUrl(userId, itemIdentifier, HttpMethod.GET, timeout);
              UserUrl userDownload = UserUrl.buildUserUrl(userId, itemIdentifier, url, null);
              return userDownload;
            });
          } else {
            return Mono.empty();
          }
        })
        .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundCompleteUploadException(itemIdentifier))));
  }
}
