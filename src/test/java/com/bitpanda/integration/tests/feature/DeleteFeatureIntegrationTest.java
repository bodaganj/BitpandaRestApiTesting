package com.bitpanda.integration.tests.feature;

import com.bitpanda.dto.feature.FeatureRequestDTO;
import com.bitpanda.dto.feature.GeometryDTO;
import com.bitpanda.dto.feature.PropertiesDTO;
import com.bitpanda.dto.feature.XyzDTO;
import com.bitpanda.dto.space.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoint;
import com.bitpanda.integration.base.IntegrationTestsBase;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Feature(HereHubEndpoint.FEATURE_ENDPOINT)
public class DeleteFeatureIntegrationTest extends IntegrationTestsBase {

   private static String spaceId;

   @BeforeAll
   public static void beforeAllTests() {
      SpaceDTO[] spaceResponse = given()
         .queryParam("access_token", getPermanentAccessToken())
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      spaceId = spaceResponse[0].getId();
   }

   @Description("Positive case: delete existed feature")
   @Test
   public void deleteFeature() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(204);
   }

   @Description("Negative case: not existed spaceId")
   @Test
   public void deleteFeatureWrongSpaceId() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam("spaceId", "wrong space id")
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body("errorMessage", Matchers.is("The resource with this ID does not exist."));
   }

   @Description("Negative case: not existed featureId")
   @Test
   public void deleteFeatureWrongFeatureId() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", "wrong feature id")
         .queryParam("access_token", getPermanentAccessToken())
         .when()
         .delete(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body("errorMessage", Matchers.is("The requested resource does not exist."));
   }

   @Description("Negative case: missing access token")
   @Test
   public void deleteFeatureMissingAccessToken() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .when()
         .delete(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(403)
         .body("errorMessage", Matchers.startsWith("Insufficient rights. Token access"));
   }

   @Description("Negative case: wrong access token")
   @Test
   public void deleteFeatureWrongAccessToken() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";
      createFeatureToBeDeleted(featureId);

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", "wrong access token")
         .when()
         .delete(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(401)
         .body("errorMessage", Matchers.is("Unauthorized"));
   }

   private void createFeatureToBeDeleted(String featureId) {
      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .body(getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(200);
   }

   private FeatureRequestDTO getFeatureBody(String featureId) {
      return FeatureRequestDTO.builder()
                              .type("Feature")
                              .id(featureId)
                              .geometry(GeometryDTO.builder()
                                                   .type("Point")
                                                   .coordinates(new Double[]{-2.960847, 53.430828})
                                                   .build())
                              .properties(PropertiesDTO.builder()
                                                       .name("Anfield")
                                                       .xyz(XyzDTO.builder()
                                                                  .tags(new String[]{"football", "stadium"})
                                                                  .build())
                                                       .amenity("Football Stadium")
                                                       .capacity(55000)
                                                       .description("Home of Liverpool Football Club")
                                                       .build())
                              .build();
   }
}
