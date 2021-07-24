package com.bitpanda.dto.feature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertiesDTO {

   private String name;
   @JsonProperty(value = "@ns:com:here:xyz")
   private XyzDTO xyz;
   private String amenity;
   private Integer capacity;
   private String description;
}
