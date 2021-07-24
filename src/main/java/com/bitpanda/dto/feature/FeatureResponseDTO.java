package com.bitpanda.dto.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureResponseDTO {

   private String id;
   private PropertiesDTO properties;
}
