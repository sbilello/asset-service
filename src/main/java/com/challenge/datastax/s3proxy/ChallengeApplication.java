package com.challenge.datastax.s3proxy;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChallengeApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChallengeApplication.class, args);
  }

  @Bean
  public OpenAPI s3ProxyApi() {
    return new OpenAPI()
        .info(new Info().title("S3 API").version("0.0.1-SNAPSHOT")
        .license(new License().name("Apache 2.0").url("http://github.com/sbilello")));
  }


  @Bean
  public GroupedOpenApi s3Api() {
    String[] paths = {"/api/v1/s3/**"};
    return GroupedOpenApi.builder()
                         .group("s3_proxy")
                         .pathsToMatch(paths)
                         .packagesToScan("com.challenge.datastax.s3proxy.controller")
                         .build();
  }

}
