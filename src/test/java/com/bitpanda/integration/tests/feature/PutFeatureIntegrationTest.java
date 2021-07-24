package com.bitpanda.integration.tests.feature;

import com.bitpanda.dto.feature.FeatureRequestDTO;
import com.bitpanda.dto.feature.FeatureResponseDTO;
import com.bitpanda.dto.feature.GeometryDTO;
import com.bitpanda.dto.feature.PropertiesDTO;
import com.bitpanda.dto.feature.XyzDTO;
import com.bitpanda.dto.space.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoint;
import com.bitpanda.integration.base.IntegrationTestsBase;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Feature(HereHubEndpoint.FEATURE_ENDPOINT)
public class PutFeatureIntegrationTest extends IntegrationTestsBase {

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

   @Description("Positive case: create new feature")
   @Test
   public void putFeature() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";

      FeatureResponseDTO response = given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .body(getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
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
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";

      given()
         .pathParam("spaceId", "not existed spaceId")
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .body(getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(404)
         .body("errorMessage", Matchers.is("The resource with this ID does not exist."));
   }

   @Description("Negative case: authorization token ('access_token') is missing.")
   @Test
   public void putFeatureMissingAccessToken() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .body(getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(403)
         .body("errorMessage", Matchers.startsWith("Insufficient rights."));
   }

   @Description("Negative case: authorization token ('access_token') is not correct.")
   @Test
   public void putFeatureWrongAccessToken() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", "wrong access token")
         .body(getFeatureBody(featureId))
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(401)
         .body("errorMessage", Matchers.is("Unauthorized"));
   }

   @Description("Negative case: invalid body format")
   @Test
   public void putFeatureInvalidBodyFormat() {
      String featureId = UUID.randomUUID().toString() + "_test_feature_id";

      given()
         .pathParam("spaceId", spaceId)
         .pathParam("featureId", featureId)
         .queryParam("access_token", getPermanentAccessToken())
         .body("Plain text body")
         .when()
         .put(HereHubEndpoint.FEATURE_ENDPOINT)
         .then()
         .statusCode(400)
         .body("message", Matchers.startsWith("[Bad Request] Json body application/json parsing error"));
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
