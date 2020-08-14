package com.challenge.datastax.s3proxy.controller;

import com.challenge.datastax.s3proxy.TestConfig;
import com.challenge.datastax.s3proxy.configuration.EmbeddedCassandraInitializer;
import com.challenge.datastax.s3proxy.model.Request;
import com.challenge.datastax.s3proxy.model.Response;
import com.challenge.datastax.s3proxy.model.Status;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = {ConfigFileApplicationContextInitializer.class,
    EmbeddedCassandraInitializer.class}, classes = TestConfig.class)
public class S3ControllerTest {

  private static final String BASE_URL = "/api/v1/s3/url";

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void missingDownloadParameters() {
    webTestClient.get().uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  public void notFoundDownloadUrl() {
    String userId = UUID.randomUUID().toString();
    String itemIdentifier = UUIDGen.getTimeUUID().toString();
    String requestUri = String.format(BASE_URL + "?userId=%s&itemIdentifier=%s", userId, itemIdentifier);
    webTestClient.get()
        .uri(requestUri)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.errorMessage").isNotEmpty();
  }

  @Test
  public void createUploadUrl() {
    UUID userId = UUID.randomUUID();
    Request request = Request.builder().withUserId(userId).build();
    webTestClient.post()
        .uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(request))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.url").isNotEmpty()
        .jsonPath("$.itemIdentifier").isNotEmpty()
        .jsonPath("$.status").isEqualTo(Status.TO_UPLOAD.name());
  }

  @Test
  public void createUpdateAndDownload() {
    // Create
    UUID userId = UUID.randomUUID();
    Request request = Request.builder().withUserId(userId).build();
    Response postResponse = webTestClient.post()
        .uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(request))
        .exchange()
        .returnResult(Response.class)
        .getResponseBody()
        .blockFirst();
    Assert.that(postResponse.getStatus().equals(Status.TO_UPLOAD));
    Assert.notNull(postResponse);
    UUID itemIdentifier = postResponse.getItemIdentifier();
    Assert.notNull(itemIdentifier);
    // Update
    Request updateRequest = Request.builder()
                                   .withUserId(userId)
                                   .withItemIdentifier(itemIdentifier)
                                   .withStatus(Status.UPLOAD_COMPLETED)
                                   .build();
    Response updateResponse = webTestClient.put()
        .uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(updateRequest))
        .exchange()
        .returnResult(Response.class)
        .getResponseBody()
        .blockFirst();
    Assert.that(updateResponse.getStatus().equals(Status.UPLOAD_COMPLETED));
    // Download
    String requestUri = String.format(BASE_URL + "?userId=%s&itemIdentifier=%s", userId.toString(), itemIdentifier.toString());
    webTestClient.get()
        .uri(requestUri)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.url").isNotEmpty();
  }

  @Test
  public void updateNotExistingUserUrl() {
    Request updateRequest = Request.builder()
        .withUserId(UUID.randomUUID())
        .withItemIdentifier(UUIDGen.getTimeUUID())
        .withStatus(Status.UPLOAD_COMPLETED)
        .build();
    webTestClient.put()
        .uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(updateRequest))
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.errorMessage").isNotEmpty();
  }

}
