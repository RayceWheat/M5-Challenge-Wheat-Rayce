package com.example.gamestoreinvoicing.util.feign;

import com.example.gamestoreinvoicing.model.Console;
import com.example.gamestoreinvoicing.model.Game;
import com.example.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gamestore-catalog")
public interface GameStoreCatalog {

    @GetMapping(value = "/game")
    public String getAllGames();

    @GetMapping(value = "/game/{id}")
    public Game getGameById(@PathVariable("id") Long gameId);

    @GetMapping(value = "/console/{id}")
    public Console getConsoleById(@PathVariable("id") Long consoleId);

    @GetMapping(value = "/tshirt/{id}")
    public TShirt getTShirtById(@PathVariable("id") Long tshirtId);

}
