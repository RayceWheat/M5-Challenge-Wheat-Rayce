package com.example.gamestorecatalog.controller;

import com.example.gamestorecatalog.model.Game;
import com.example.gamestorecatalog.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/game")
@CrossOrigin(origins = {"http://localhost:7475"})
public class GameController {

    @Autowired
    GameRepository gameRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody @Valid Game game){
        Game game2 = gameRepo.save(game);
        return game2;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Game getGameById(@PathVariable("id") long gameId){
        Optional<Game> game = gameRepo.findById(gameId);
        if(game == null){
            throw new IllegalArgumentException("Game does not exists");
        } else {
            return (game.get());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGame(@RequestBody @Valid Game game){
        if (game == null || game.getId() < 1){
            throw new IllegalArgumentException("Games does not exits");
        } else if (game.getId() > 0){
            gameRepo.save(game);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("id") long gameId){
        gameRepo.deleteById(gameId);
    }

    @GetMapping("/esrb/{esrb}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByEsrbRating(@PathVariable("esrb") String esrb){
        List<Game> gamesByEsrb = gameRepo.findAllByEsrbRating(esrb);
        if (gamesByEsrb == null || gamesByEsrb.isEmpty()){
            throw new IllegalArgumentException("No games with that rating");
        } else {
            return gamesByEsrb;
        }
    }

    @GetMapping("/studio/{studio}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByStudio(@PathVariable("studio") String studio){
        List<Game> gamesByStudio = gameRepo.findAllByStudio(studio);
        if (gamesByStudio == null || gamesByStudio.isEmpty()){
            throw new IllegalArgumentException("No games with that studio");
        } else {
            return gamesByStudio;
        }
    }

    @GetMapping("/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getGamesByTitle(@PathVariable("title") String title){
        List<Game> gamesByTitle = gameRepo.findAllByTitle(title);
        if (gamesByTitle == null || gamesByTitle.isEmpty()){
            throw new IllegalArgumentException("No games with that title");
        } else {
            return gamesByTitle;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Game> getAllConsoles(){
        List<Game> allGames = gameRepo.findAll();
        if (allGames == null || allGames.isEmpty()) {
            throw new IllegalArgumentException("No games were found");
        } else {
            return allGames;
        }
    }


}
