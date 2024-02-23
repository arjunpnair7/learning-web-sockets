package com.example.demo;

import com.example.demo.Model.Pokemon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "pokemon",
        url = "https://pokeapi.co/api/v2")
public interface PokemonProxy {
    @GetMapping("/pokemon")
    String getPokemonList();

}
