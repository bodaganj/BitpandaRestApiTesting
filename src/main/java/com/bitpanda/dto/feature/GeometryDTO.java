package com.bitpanda.dto.feature;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeometryDTO {

   private final String type;
   private final Double[] coordinates;
}
