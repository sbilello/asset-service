package com.challenge.datastax.s3proxy.controller;

import com.challenge.datastax.s3proxy.exception.NotFoundCompleteUploadException;
import com.challenge.datastax.s3proxy.exception.NotFoundUploadException;
import com.challenge.datastax.s3proxy.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;


@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorMessage> handle(NotFoundUploadException exception) {
    return genericErrorResponseEntity(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorMessage> handle(NotFoundCompleteUploadException exception) {
    return genericErrorResponseEntity(exception, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorMessage> handle(Exception exception) {
    return genericErrorResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorMessage> handle(ServerWebInputException exception) {
    return genericErrorResponseEntity(exception, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorMessage> genericErrorResponseEntity(Exception ex, HttpStatus httpStatus) {
    return new ResponseEntity(new ErrorMessage(ex.getMessage()), httpStatus);
  }

}
