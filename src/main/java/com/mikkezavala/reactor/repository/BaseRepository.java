package com.mikkezavala.reactor.repository;

import com.mikkezavala.reactor.configuration.AppConfig;
import com.mikkezavala.reactor.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Un repositorio base, simple que solo sirve de proxy para las llamadas del webclient.
 * <p>
 * Spring Boot WS no tiene un Message parser para SOAP que soporte acciones asincronas, por eso las
 * llamadas del web client aqui se bloquean. Use este ejemplo pues se puede implementar un servlet
 * que soporte async Message mapping El repositorio debes no conocer como es consumido.
 */
@Service
public class BaseRepository implements Repository<String, Mono<Pokemon>> {

  private final WebClient webClient;

  private final AppConfig configuration;

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepository.class);

  /**
   * Instantiates a new Base repository.
   *
   * @param webClient the web client
   */
  public BaseRepository(WebClient webClient, AppConfig configuration) {
    this.configuration = configuration;
    this.webClient = webClient.mutate().baseUrl(configuration.getPokemonService().getServer())
        .build();
  }

  @Override
  public Mono<Pokemon> findById(String name) {

    LOGGER.info("Inicio consulta: {}/{}", configuration.getPokemonService().getServer(), name);
    return webClient.get().uri("/{name}", name).retrieve().bodyToMono(Pokemon.class);
  }
}
