package com.bitpanda.integration.base;

import com.bitpanda.setup.EnvProperties;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;

import java.util.UUID;

public abstract class IntegrationTestsBase {

   @Getter
   private static final String permanentAccessToken = EnvProperties.getProperty("permanent.access.token");
   private static final Integer TIMEOUT = 20000;
   private static final String HERE_GATEWAY = EnvProperties.getProperty("url.here.gateway");

   protected String wrongTestData = "wrong test data";

   protected static RequestSpecification given() {
      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
      return getRequestSpecification().baseUri(HERE_GATEWAY);
   }

   private static RequestSpecification getRequestSpecification() {
      SSLConfig sslConfig = RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
      HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig()
                                                          .setParam("http.connection.stalecheck", true)
                                                          .setParam("http.connection.timeout", TIMEOUT)
                                                          .setParam("http.socket.timeout", TIMEOUT)
                                                          .setParam("http.connection-manager.timeout", Long.valueOf(TIMEOUT));

      RestAssuredConfig config = RestAssured.config().httpClient(httpClientConfig).sslConfig(sslConfig);
      return RestAssured.given()
                        .config(config)
                        .filter(new AllureRestAssured())
                        .filter(new ApiResponseLoggingFilter(LogDetail.ALL, System.out, Matchers.any(Integer.class)))
                        .filter(new ApiRequestLoggingFilter(LogDetail.ALL, System.out, true))
                        .contentType(ContentType.JSON)
                        .log().ifValidationFails();
   }

   @AfterEach
   public void afterEachTest() {
      RestAssured.reset();
   }

   protected String getTestFeatureId() {
      return UUID.randomUUID().toString() + "_test_feature_id";
   }
}
