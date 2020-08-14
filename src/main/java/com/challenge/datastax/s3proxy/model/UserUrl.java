package com.challenge.datastax.s3proxy.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("user_url")
@Data
@Builder(setterPrefix = "with")
public class UserUrl implements Serializable {


  private static final long serialVersionUID = 764445677116659906L;

  @PrimaryKey("user_id")
  private UUID userId;

  @Column("item_identifier")
  private UUID itemIdentifier;

  @Column("url")
  private String url;

  @Column("status")
  private Status status;

  public static UserUrl buildUserUrl(UUID userId, UUID itemIdentifier, String url, Status status) {
    return UserUrl.builder()
        .withUserId(userId)
        .withItemIdentifier(itemIdentifier)
        .withStatus(status)
        .withUrl(url)
        .build();
  }
}
