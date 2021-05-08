//package com.mikkezavala.reactor;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.mikkezavala.reactor.constants.AbilityType;
//import com.mikkezavala.reactor.constants.GetAbilitiesRequest;
//import com.mikkezavala.reactor.constants.GetAbilitiesResponse;
//import com.mikkezavala.reactor.constants.GetHeldItemsRequest;
//import com.mikkezavala.reactor.constants.GetHeldItemsResponse;
//import com.mikkezavala.reactor.constants.GetIdRequest;
//import com.mikkezavala.reactor.constants.GetIdResponse;
//import com.mikkezavala.reactor.constants.GetLocalAreaEncountersRequest;
//import com.mikkezavala.reactor.constants.GetLocalAreaEncountersResponse;
//import com.mikkezavala.reactor.constants.GetNameRequest;
//import com.mikkezavala.reactor.constants.GetNameResponse;
//import com.mikkezavala.reactor.constants.GetPokemonRequest;
//import com.mikkezavala.reactor.constants.GetPokemonResponse;
//import com.mikkezavala.reactor.constants.HeldItemType;
//import com.mikkezavala.reactor.constants.HeldItemsVersionDetail;
//import com.mikkezavala.reactor.constants.PokemonHeldItems;
//import com.mikkezavala.reactor.endpoint.PokemonEndpoint;
//import com.github.tomakehurst.wiremock.WireMockServer;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.io.UncheckedIOException;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.util.FileCopyUtils;
//
//
//@SpringBootTest
//@ActiveProfiles("test")
//@ContextConfiguration(initializers = {WireMockInitializer.class})
//class EndpointTest {
//
//  @Autowired
//  private PokemonEndpoint endpoint;
//
//  @Autowired
//  private WireMockServer wireMockServer;
//
//  ResourceLoader resourceLoader = new DefaultResourceLoader();
//
//  @BeforeEach
//  public void testSetUp() {
//    String mockResponse = getTestResource("mockPokemon.json");
//    wireMockServer.stubFor(get("/ditto").willReturn(
//        aResponse().withStatus(HttpStatus.OK.value())
//            .withHeader("content-type", "application/json")
//            .withBody(mockResponse)
//        )
//    );
//  }
//
//  @Test
//  public void shouldReturnPokemonResponse() {
//
//    GetPokemonResponse response = endpoint.getPokemon(new GetPokemonRequest().withName("ditto"));
//    assertThat(response).isNotNull();
//    assertThat(response.getPokemon().getId()).isEqualTo(132);
//    assertThat(response.getPokemon().getName()).isEqualTo("ditto");
//    assertThat(response.getPokemon().getBaseExperience()).isEqualTo(101);
//    assertThat(response.getPokemon().getAbilities().getAbility()).hasSize(2);
//    assertThat(response.getPokemon().getHeldItems().getHeldItem()).hasSize(2);
//    assertThat(response.getPokemon().getLocationAreaEncounters())
//        .isEqualTo("https://pokeapi.co/api/v2/pokemon/132/encounters");
//  }
//
//  @Test
//  public void shouldReturnAbilitiesResponse() {
//
//    GetAbilitiesResponse response = endpoint.getPokemonAbilities(new GetAbilitiesRequest().withPokemonName("ditto"));
//    assertThat(response).isNotNull();
//    assertThat(response.getAbilities().getAbility()).hasSize(2);
//
//    AbilityType responseAbility = response.getAbilities().getAbility().get(0);
//
//    assertThat(responseAbility.isHidden()).isFalse();
//    assertThat(responseAbility.getSlot()).isEqualTo(1);
//    assertThat(responseAbility.getAbility().getName()).isEqualTo("limber");
//    assertThat(responseAbility.getAbility().getUrl()).isEqualTo("https://pokeapi.co/api/v2/ability/7/");
//  }
//
//  @Test
//  public void shouldReturnHeldItemsResponse() {
//
//    GetHeldItemsResponse response = endpoint.getPokemonHeldItems(new GetHeldItemsRequest().withPokemonName("ditto"));
//    assertThat(response).isNotNull();
//    Assertions.assertThat(response.getHeldItems()).hasSize(1);
//
//    PokemonHeldItems responseHeldItems = response.getHeldItems().get(0);
//    assertThat(responseHeldItems.getHeldItem()).hasSize(2);
//
//    HeldItemType responseHeldType = responseHeldItems.getHeldItem().get(0);
//
//    assertThat(responseHeldType.getVersionDetails().getVersion()).hasSize(22);
//    assertThat(responseHeldType.getItem().getName()).isEqualTo("metal-powder");
//    assertThat(responseHeldType.getItem().getUrl()).isEqualTo("https://pokeapi.co/api/v2/item/234/");
//
//    HeldItemsVersionDetail responseHeldTypeVersions = responseHeldType.getVersionDetails().getVersion().get(1);
//
//    assertThat(responseHeldTypeVersions.getRarity()).isEqualTo(5);
//    assertThat(responseHeldTypeVersions.getVersion().getName()).isEqualTo("sapphire");
//    assertThat(responseHeldTypeVersions.getVersion().getUrl()).isEqualTo("https://pokeapi.co/api/v2/version/8/");
//  }
//
//  @Test
//  public void shouldReturnPokemonId() {
//    GetIdResponse response = endpoint.getPokemonId(new GetIdRequest().withPokemonName("ditto"));
//    assertThat(response).isNotNull();
//    assertThat(response.getId()).isEqualTo(132);
//  }
//
//  @Test
//  public void shouldReturnPokemonName() {
//    GetNameResponse response = endpoint.getPokemonName(new GetNameRequest().withPokemonName("ditto"));
//    assertThat(response).isNotNull();
//    assertThat(response.getName()).isEqualTo("ditto");
//  }
//
//  @Test
//  public void shouldReturnPokemonLocalAreaEncounters() {
//    GetLocalAreaEncountersResponse response = endpoint
//        .getPokemonLocationAreaEncounters(new GetLocalAreaEncountersRequest().withPokemonName("ditto"));
//    assertThat(response).isNotNull();
//    assertThat(response.getLocationAreaEncounters()).isEqualTo("https://pokeapi.co/api/v2/pokemon/132/encounters");
//  }
//
//  public String getTestResource(String file) {
//    Resource resource = resourceLoader.getResource("classpath:" + file);
//    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
//      return FileCopyUtils.copyToString(reader);
//    } catch (IOException e) {
//      throw new UncheckedIOException(e);
//    }
//  }
//}
//
