package com.bitpanda.integration.tests.feature;

import com.bitpanda.dto.space.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoints;
import com.bitpanda.integration.base.IntegrationTestsBase;
import com.bitpanda.integration.base.helper.FeatureBodyProvider;
import com.bitpanda.setup.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Feature(HereHubEndpoints.FEATURE_ENDPOINT)
public class DeleteFeatureIntegrationTest extends IntegrationTestsBase {

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

   @Description("Positive case: delete existed feature")
   @Test
   public void deleteFeature() {
      String featureId = getTestFeatureId();
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(204);
   }

   @Description("Negative case: not existed spaceId")
   @Test
   public void deleteFeatureWrongSpaceId() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, wrongTestData)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body(Constants.ERROR_MESSAGE, Matchers.is(Constants.RESOURCE_DOES_NOT_EXIST));
   }

   @Description("Negative case: not existed featureId")
   @Test
   public void deleteFeatureWrongFeatureId() {
      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, wrongTestData)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body(Constants.ERROR_MESSAGE, Matchers.is("The requested resource does not exist."));
   }

   @Description("Negative case: missing access token")
   @Test
   public void deleteFeatureMissingAccessToken() {
      String featureId = getTestFeatureId();
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .when()
         .delete(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(403)
         .body(Constants.ERROR_MESSAGE, Matchers.startsWith(Constants.INSUFFICIENT_RIGHTS));
   }

   @Description("Negative case: wrong access token")
   @Test
   public void deleteFeatureWrongAccessToken() {
      String featureId = getTestFeatureId();

      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, wrongTestData)
         .when()
         .delete(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(401)
         .body(Constants.ERROR_MESSAGE, Matchers.is(Constants.UNAUTHORIZED));
   }

   private void createFeatureToBeDeleted(String featureId) {
      given()
         .pathParam(Constants.SPACE_ID, spaceId)
         .pathParam(Constants.FEATURE_ID, featureId)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .body(FeatureBodyProvider.getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoints.FEATURE_ENDPOINT)
         .then()
         .statusCode(200);
   }
}
