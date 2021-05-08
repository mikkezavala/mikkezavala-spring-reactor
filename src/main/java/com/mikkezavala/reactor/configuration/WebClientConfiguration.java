package com.mikkezavala.reactor.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import javax.net.ssl.SSLException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

/**
 * The type Web client configuration.
 */
@Configuration
public class WebClientConfiguration {

  private static final int MAX_CLIENT_CONNECTIONS = 50;

  /**
   * Gets ssl context.
   *
   * @return the ssl context
   */
  @Bean
  SslContext getSSLContext() {
    try {
      return SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
    } catch (SSLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Http connector reactor client http connector.
   *
   * Permitimos que el WebClient que el cliente web, se conecte a SSL configurando un trusted context en este servlet
   * inseguro por supuesto, por que acepta todos los certificados.
   *
   * Configuramos la concurrencia este webclient tendra. en nuestro caso 50
   *
   * @param sslContext the ssl context
   * @return the reactor client http connector
   */
  @Bean
  ReactorClientHttpConnector httpConnector(SslContext sslContext) {
    HttpClient httpClient = HttpClient.create(
        ConnectionProvider.create("MIKKEZAVALA", MAX_CLIENT_CONNECTIONS)
    ).followRedirect(true).secure(t -> t.sslContext(sslContext));
    return new ReactorClientHttpConnector(httpClient);
  }

  /**
   * Web client web client.
   *
   * Se configura la capa del HTTP y de los codecs, para que la respuesta del webclient (json), pueda transformar
   * las respuestas de JSON hacia los POJOS generados por JAXB. Adicionalmente, cambiamos el mapper default
   * al que configuramos que soporta "snake_case".
   * Agregamos un filtro (que funciona mas como interceptor). para poder leer las peticiones.
   *
   * @param httpConnector the http connector
   * @param mapper the mapper
   * @return the web client
   */
  @Bean
  public WebClient webClient(ReactorClientHttpConnector httpConnector, ObjectMapper mapper) {
    return WebClient.builder().clientConnector(httpConnector).exchangeStrategies(
        ExchangeStrategies.builder().codecs(configurer -> {
          configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
        }).build()
    ).filter(logFilter()).build();
  }

  private ExchangeFilterFunction logFilter() {
    return ExchangeFilterFunction.ofRequestProcessor(request -> {
      // Un logger
      return Mono.just(request);
    });
  }
}
