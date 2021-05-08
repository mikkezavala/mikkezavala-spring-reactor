package com.mikkezavala.reactor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class AppConfig {

  private PokemonService pokemonService;

  @Data
  public static class PokemonService {
    private String server;
  }
}
