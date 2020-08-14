package com.challenge.datastax.s3proxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

@NoArgsConstructor
@Data
@Builder(setterPrefix = "with")
@JsonInclude(Include.NON_NULL)
public class Request implements Serializable {

  private static final long serialVersionUID = 764445677116249906L;

  @JsonCreator
  public Request(@JsonProperty("userId") UUID userId) {
    this.userId = userId;
  }

  @JsonCreator
  public Request(@JsonProperty("userId") UUID userId,
      @JsonProperty("itemIdentifier") UUID itemIdentifier,
      @JsonProperty("status") Status status) {
    this.userId = userId;
    this.itemIdentifier = itemIdentifier;
    this.status = status;
  }

  @JsonSerialize(using = UUIDSerializer.class)
  @JsonDeserialize(using = UUIDDeserializer.class)
  private UUID userId;

  @JsonSerialize(using = UUIDSerializer.class)
  @JsonDeserialize(using = UUIDDeserializer.class)
  private UUID itemIdentifier;

  @JsonProperty("status")
  private Status status;

}
