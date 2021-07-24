package com.bitpanda.integration.tests.feature;

import com.bitpanda.dto.feature.FeatureResponseDTO;
import com.bitpanda.dto.space.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoints;
import com.bitpanda.integration.base.IntegrationTestsBase;
import com.bitpanda.integration.base.helper.FeatureBodyProvider;
import com.bitpanda.setup.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Feature(HereHubEndpoints.FEATURE_ENDPOINT)
public class PutFeatureIntegrationTest extends IntegrationTestsBase {

   private static String spaceId;

   @BeforeAll
   public static void beforeAllTests() {
      SpaceDTO[] spaceResponse = given()
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      spaceId = spaceResponse[0].getId();
   }

   @Description("Positive case: create new feature")
   @Test
   public void putFeature() {
      String featureId = getTestFeatureId();

      FeatureResponseDTO response = given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .body(FeatureBodyProvider.getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(FeatureResponseDTO.class);

      Assertions.assertThat(response.getId())
                .as("Feature should have appropriate id.")
                .isEqualTo(featureId);
   }

   @Description("Negative case: not existed spaceId")
   @Test
   public void putFeatureNotExistedSpaceId() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, wrongTestData)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .body(FeatureBodyProvider.getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body(Constants.ERROR_MESSAGE, Matchers.is(Constants.RESOURCE_DOES_NOT_EXIST));
   }

   @Description("Negative case: authorization token ('access_token') is missing.")
   @Test
   public void putFeatureMissingAccessToken() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .body(FeatureBodyProvider.getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(403)
         .body(Constants.ERROR_MESSAGE, Matchers.startsWith(Constants.INSUFFICIENT_RIGHTS));
   }

   @Description("Negative case: authorization token ('access_token') is not correct.")
   @Test
   public void putFeatureWrongAccessToken() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, wrongTestData)
         .body(FeatureBodyProvider.getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(401)
         .body(Constants.ERROR_MESSAGE, Matchers.is(Constants.UNAUTHORIZED));
   }

   @Description("Negative case: invalid body format")
   @Test
   public void putFeatureInvalidBodyFormat() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .body("Plain text body")
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(400)
         .body(Constants.MESSAGE, Matchers.startsWith("[Bad Request] Json body application/json parsing error"));
   }
}
