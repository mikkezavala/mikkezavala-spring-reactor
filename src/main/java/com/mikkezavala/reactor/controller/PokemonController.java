package com.mikkezavala.reactor.controller;

import com.mikkezavala.reactor.model.Pokemon;
import com.mikkezavala.reactor.repository.BaseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class PokemonController {

  private final BaseRepository repository;

  public PokemonController(BaseRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/pokemon/{name}")
  public Mono<Pokemon> getPokemon(@PathVariable String name) {
    return repository.findById(name);
  }
}
