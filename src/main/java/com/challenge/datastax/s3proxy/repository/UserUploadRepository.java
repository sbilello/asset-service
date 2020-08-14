package com.challenge.datastax.s3proxy.repository;


import com.challenge.datastax.s3proxy.model.UserUrl;
import java.util.UUID;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserUploadRepository extends ReactiveCassandraRepository<UserUrl, UUID> {

  Mono<UserUrl> findUserUploadByUserIdAndAndItemIdentifier(UUID userId, UUID timeUUID);

}