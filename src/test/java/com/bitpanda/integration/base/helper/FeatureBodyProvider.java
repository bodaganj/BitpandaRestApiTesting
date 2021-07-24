package com.bitpanda.integration.base.helper;

import com.bitpanda.dto.feature.FeatureRequestDTO;
import com.bitpanda.dto.feature.GeometryDTO;
import com.bitpanda.dto.feature.PropertiesDTO;
import com.bitpanda.dto.feature.XyzDTO;

public class FeatureBodyProvider {

   public static FeatureRequestDTO getFeatureBody(String featureId) {
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
