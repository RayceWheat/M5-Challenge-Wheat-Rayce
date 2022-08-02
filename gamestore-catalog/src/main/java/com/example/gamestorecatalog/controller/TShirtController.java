package com.example.gamestorecatalog.controller;

import com.example.gamestorecatalog.model.TShirt;
import com.example.gamestorecatalog.repository.TShirtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tshirt")
@CrossOrigin(origins = {"http://localhost:7475"})
public class TShirtController {

    @Autowired
    TShirtRepository tShirtRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TShirt createTShirt(@RequestBody @Valid TShirt tShirt){
        TShirt tShirt2 = tShirtRepository.save(tShirt);
        return tShirt2;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TShirt getTShirtById(@PathVariable("id") long TShirtId){
        Optional<TShirt> tShirt = tShirtRepository.findById(TShirtId);
        if (tShirt == null) {
            return null;
        } else {
            return (tShirt.get());
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirt tShirt){
        if (tShirt == null || tShirt.getId() < 1){
            throw new IllegalArgumentException("TShirt does not exist");
        } else if (tShirt.getId() > 0) {
            tShirtRepository.save(tShirt);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable("id") long tShirtId) {
        tShirtRepository.deleteById(tShirtId);
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsBySize(@PathVariable("size") String size){
        List<TShirt> tShirtsBySize = tShirtRepository.findAllBySize(size);
        if (tShirtsBySize == null || tShirtsBySize.isEmpty()){
            throw new IllegalArgumentException("No TShirts with that size");
        } else {
            return tShirtsBySize;
        }
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsByColor(@PathVariable("color") String color){
        List<TShirt> tShirtsByColor = tShirtRepository.findAllByColor(color);
        if (tShirtsByColor == null || tShirtsByColor.isEmpty()){
            throw new IllegalArgumentException("No TShirts with that color");
        } else {
            return tShirtsByColor;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getAllTShirts(){
        List<TShirt> allTShirts = tShirtRepository.findAll();
        if (allTShirts == null || allTShirts.isEmpty()){
            throw new IllegalArgumentException("No TShirts were found.");
        } else {
            return allTShirts;
        }
    }


}
