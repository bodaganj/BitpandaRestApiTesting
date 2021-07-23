package com.bitpanda.integration.space.positive;

import com.bitpanda.dto.SpaceDTO;
import com.bitpanda.integration.base.HereHubEndpoint;
import com.bitpanda.integration.base.IntegrationTestsBase;
import org.junit.jupiter.api.Test;

public class GetSpaceIntegrationTest extends IntegrationTestsBase {

   @Test
   public void getSpace() {
//      SpaceDTO spaceResponse = given()
      given()
         .queryParam("includeRights", "false")
         .queryParam("owner", "me")
         .queryParam("access_token", "ANEviVy1SganXgGyqXhmEwA")
         .when()
         .get(HereHubEndpoint.SPACE_ENDPOINT)
         .then()
         .statusCode(200);
//         .extract().body().as(SpaceDTO.class);
   }
}
