package com.multiteam.core.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {

  @Value("${aws.s3.bucket-region}")
  private String s3BucketRegion;

  @Value("${aws.s3.bucket-key}")
  private String s3BucketKey;

  @Value("${aws.s3.bucket-secret}")
  private String s3BucketSecret;

  @Bean
  public AmazonS3 AutheticationS3() {
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(s3BucketRegion)
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3BucketKey, s3BucketSecret)))
        .build();
  }

}
