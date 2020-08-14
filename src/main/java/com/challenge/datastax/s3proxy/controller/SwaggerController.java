package com.challenge.datastax.s3proxy.controller;

import static org.springdoc.core.Constants.SWAGGER_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Home redirection to swagger api documentation
 */
@RestController
public class SwaggerController {

  @Value(SWAGGER_UI_PATH)
  private String swaggerUiPath;

  @GetMapping(DEFAULT_PATH_SEPARATOR)
  public Mono<Void> index(ServerHttpRequest request, ServerHttpResponse response) {
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(swaggerUiPath);
    response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
    response.getHeaders().setLocation(URI.create(uriBuilder.build().encode().toString()));
    return response.setComplete();
  }
}
