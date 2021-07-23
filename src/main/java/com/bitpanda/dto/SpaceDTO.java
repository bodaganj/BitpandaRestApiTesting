package com.bitpanda.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceDTO {

   private String id;
   private String title;
   private String description;
   private String owner;
   private String[] rights;
}
