package com.challenge.datastax.s3proxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class ErrorMessage implements Serializable {


  private static final long serialVersionUID = 123245677116249906L;

  @Getter
  private String errorMessage;

  @JsonCreator
  public ErrorMessage(@JsonProperty("errorMessage") String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
