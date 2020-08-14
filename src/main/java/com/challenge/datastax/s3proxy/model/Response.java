package com.challenge.datastax.s3proxy.model;

import java.io.Serializable;import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@NoArgsConstructor
@Builder(setterPrefix = "with")
@JsonInclude(Include.NON_NULL)
@Data
public class Response implements Serializable {

  private static final long serialVersionUID = 762245677116249906L;

  private String url;
  private UUID itemIdentifier;
  private Status status;

  @JsonCreator
  public Response(@JsonProperty("url") String url,
      @JsonProperty("itemIdentifier") UUID itemIdentifier,
      @JsonProperty("status") Status status) {
    this.url = url;
    this.itemIdentifier = itemIdentifier;
    this.status = status;
  }

  public static Response buildResponseFromUserUrl(UserUrl userUrl) {
    return buildResponse(userUrl.getItemIdentifier(), userUrl.getUrl(), userUrl.getStatus());
  }

  private static Response buildResponse(UUID itemIdentifier, String url, Status status) {
    return Response.builder()
        .withItemIdentifier(itemIdentifier)
        .withUrl(url)
        .withStatus(status)
        .build();
  }

}