package com.challenge.datastax.s3proxy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Json {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final Logger logger = LoggerFactory.getLogger(Json.class);

  public static String toJsonString(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (Exception exception) {
      logger.error("It has not been possible to serialize {}", value);
    }
    return null;
  }

}

