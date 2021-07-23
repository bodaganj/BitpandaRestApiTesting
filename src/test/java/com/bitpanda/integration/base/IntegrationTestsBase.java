package com.bitpanda.integration.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;

import java.util.Collections;

public abstract class IntegrationTestsBase {

   private static final Integer TIMEOUT = 20000;

   @AfterEach
   public void afterEachTest() {
      RestAssured.reset();
   }

   protected RequestSpecification given() {
      RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
      return getRequestSpecification().baseUri(UriProperties.HERE_GATEWAY);
   }

   private RequestSpecification getRequestSpecification() {
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
                        .filter(new ApiResponseLoggingFilter(Collections.singletonList(LogDetail.ALL)))
                        .filter(new ApiRequestLoggingFilter(LogDetail.ALL))
                        .contentType(ContentType.JSON)
                        .log().ifValidationFails();
   }
}
