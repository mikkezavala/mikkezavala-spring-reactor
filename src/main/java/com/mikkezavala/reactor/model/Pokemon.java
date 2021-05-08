package com.mikkezavala.reactor.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Pokemon.PokemonBuilder.class)
public class Pokemon {

  private int id;

  private int height;

  private int weight;

  private String name;

  private int baseExperience;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PokemonBuilder {
    // Soporte para Lombok Builder
  }

}
