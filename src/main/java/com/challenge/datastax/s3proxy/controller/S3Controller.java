package com.challenge.datastax.s3proxy.controller;


import com.challenge.datastax.s3proxy.model.Request;
import com.challenge.datastax.s3proxy.model.Response;
import com.challenge.datastax.s3proxy.model.UserUrl;
import com.challenge.datastax.s3proxy.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/s3/")
public class S3Controller {

  private final UserService userService;

  @Autowired
  public S3Controller(UserService userService) {
    this.userService = userService;
  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Created new user_url record")
  })
  @PostMapping(value = "/url")
  public Mono<ResponseEntity<Response>> createUrl(
      @RequestParam(defaultValue = "60", name = "timeout") int timeout,
      @RequestBody Request request) {
    Mono<UserUrl> userUploadMono = userService.createUserUrl(request.getUserId(), timeout);
    return userUploadMono.map(userUpload -> ResponseEntity.ok(Response.buildResponseFromUserUrl(userUpload)));
  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update if the user url is present with the new status"),
      @ApiResponse(responseCode = "404", description = "if the url is not present"),
  })
  @PutMapping(value = "/url")
  public Mono<ResponseEntity<Response>> updateUrlStatus(@RequestBody Request request) {
    Mono<UserUrl> userUploadMono = userService.updateUserUrl(request.getUserId(), request.getItemIdentifier(), request.getStatus());
    return userUploadMono.map(userUpload -> ResponseEntity.ok(Response.buildResponseFromUserUrl(userUpload)));
  }

  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "if the status is UPLOAD_COMPLETED return download url"),
      @ApiResponse(responseCode = "404", description = "if the url has not Status UPLOAD_COMPLETED or not present"),
  })
  @GetMapping(value = "/url")
  public Mono<ResponseEntity<Response>> downloadUrl(
      @RequestParam(defaultValue = "60", name = "timeout") int timeout,
      @RequestParam(name = "itemIdentifier") UUID itemIdentifier,
      @RequestParam(name = "userId") UUID userId) {
    Mono<UserUrl> userDownloadMono = userService.downloadUrl(userId, itemIdentifier, timeout);
    return userDownloadMono.map(userDownload -> ResponseEntity.ok(Response.buildResponseFromUserUrl(userDownload)));
  }

}
