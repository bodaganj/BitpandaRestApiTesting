package com.bitpanda.integration.tests.space;

import com.bitpanda.dto.space.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoints;
import com.bitpanda.integration.base.IntegrationTestsBase;
import com.bitpanda.setup.Constants;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@Feature(HereHubEndpoints.SPACE_ENDPOINT)
public class GetSpacesIntegrationTest extends IntegrationTestsBase {

   @Description("Positive case: default 'owner' and 'includeRights' parameter's values")
   @Test
   public void getSpaceWithDefaultParams() {
      SpaceDTO[] spaceResponse = given()
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
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
         .queryParam(Constants.INCLUDE_RIGHTS, true)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
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
         .queryParam(Constants.OWNER, "others")
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
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
         .queryParam(Constants.OWNER, wrongTestData)
         .queryParam(Constants.ACCESS_TOKEN, getPermanentAccessToken())
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
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
         .get(HereHubEndpoints.SPACE_ENDPOINT)
         .then()
         .statusCode(403)
         .body(Constants.ERROR_MESSAGE, Matchers.is("Accessing this resource with an anonymous token is not possible."));
   }

   @Description("Negative case: authorization token ('access_token') is not correct.")
   @Test
   public void getSpaceWrongAccessToken() {
      given()
         .queryParam(Constants.ACCESS_TOKEN, wrongTestData)
         .when()
         .get(HereHubEndpoints.SPACE_ENDPOINT)
         .then()
         .statusCode(401)
         .body(Constants.ERROR_MESSAGE, Matchers.is(Constants.UNAUTHORIZED));
   }
}
