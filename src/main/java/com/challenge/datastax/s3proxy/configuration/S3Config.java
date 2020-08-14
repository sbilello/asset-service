package com.challenge.datastax.s3proxy.configuration;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class S3Config {

  @Value("${localstack.url}")
  private String localS3Endpoint;

  @Value("${localstack.region}")
  private String region;

  @Bean
  public AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder
        .standard()
        .withEndpointConfiguration(new EndpointConfiguration(localS3Endpoint, region))
        .enableForceGlobalBucketAccess()
        .enablePathStyleAccess()
        .build();
  }

}
