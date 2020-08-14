package com.challenge.datastax.s3proxy;


import static org.mockito.Mockito.mock;

import com.amazonaws.services.s3.AmazonS3;
import java.net.MalformedURLException;
import java.net.URL;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootApplication
public class TestConfig {

  @Bean
  AmazonS3 amazonS3() throws MalformedURLException {
    AmazonS3 amazonS3 = mock(AmazonS3.class, Mockito.RETURNS_DEEP_STUBS);
    URL url = new URL("http://mockSignedUrl/");
    Mockito.when(amazonS3.generatePresignedUrl(Mockito.any())).thenReturn(url);
    return amazonS3;
  }

}
