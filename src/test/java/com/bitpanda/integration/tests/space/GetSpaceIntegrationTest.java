package com.bitpanda.integration.tests.space;

import com.bitpanda.dto.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoint;
import com.bitpanda.integration.base.IntegrationTestsBase;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@Feature(HereHubEndpoint.SPACE_ENDPOINT)
public class GetSpaceIntegrationTest extends IntegrationTestsBase {

   @Description("Positive case: default 'owner' and 'includeRights' parameter's values")
   @Test
   public void getSpaceWithDefaultParams() {
      SpaceDTO[] spaceResponse = given()
         .queryParam("access_token", "AGHgflBFSwirWU6swBQ_XQA")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      Assertions.assertThat(spaceResponse)
                .as("Response should contain some spaces.")
                .isNotEmpty();
   }

   @Description("Positive case: 'includeRights=true' parameter")
   @Test
   public void getSpaceWithIncludeRights() {
      SpaceDTO[] spaceResponse = given()
         .queryParam("includeRights", true)
         .queryParam("access_token", "AGHgflBFSwirWU6swBQ_XQA")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      SoftAssertions softAssertions = new SoftAssertions();
      softAssertions.assertThat(spaceResponse)
                    .as("Response should contain some spaces.")
                    .isNotEmpty();

      softAssertions.assertThat(spaceResponse)
                    .as("Each space should contain some rights.")
                    .extracting(SpaceDTO::getRights)
                    .allMatch(rights -> rights.length > 0);
      softAssertions.assertAll();
   }

   @Description("Positive case: 'owner=others' parameter")
   @Test
   public void getSpaceWithOwner() {
      SpaceDTO[] spaceResponse = given()
         .queryParam("owner", "others")
         .queryParam("access_token", "AGHgflBFSwirWU6swBQ_XQA")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      Assertions.assertThat(spaceResponse)
                .as("There should be spaces of other users in response.")
                .isNotEmpty();
   }

   @Description("Negative case: 'owner=not real owner' parameter")
   @Test
   public void getSpaceWithNotRealOwner() {
      SpaceDTO[] spaceResponse = given()
         .queryParam("owner", "not real owner")
         .queryParam("access_token", "AGHgflBFSwirWU6swBQ_XQA")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200)
         .extract().body().as(SpaceDTO[].class);

      Assertions.assertThat(spaceResponse)
                .as("There should not be spaces of incorrect user in response.")
                .isEmpty();
   }

   @Description("Negative case: authorization token ('access_token') is missing.")
   @Test
   public void getSpaceMissingAccessToken() {
      given()
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(403)
         .body("errorMessage", Matchers.is("Accessing this resource with an anonymous token is not possible."));
   }

   @Description("Negative case: authorization token ('access_token') is not correct.")
   @Test
   public void getSpaceWrongAccessToken() {
      given()
         .queryParam("access_token", "wrong access token")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(401)
         .body("errorMessage", Matchers.is("Unauthorized"));
   }
}
