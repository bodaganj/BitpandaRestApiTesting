package com.bitpanda.dto.feature;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureRequestDTO {

   private final String type;
   private final String id;
   private final GeometryDTO geometry;
   private final PropertiesDTO properties;
}
