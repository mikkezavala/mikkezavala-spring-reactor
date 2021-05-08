package com.mikkezavala.reactor.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Bootstrap.
 */
@Configuration
public class Bootstrap {

  /**
   * Object mapper object mapper.
   * <p>
   * En este object mapper, lo configuramos de una forma que pueda leer "snake_case" del json y
   * pueda traducir a cameCase. Si algunas propiedades no estan mapeadas solo ignoramos no queremos
   * el mapper eleve una excepcion.
   *
   * @return the object mapper
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
