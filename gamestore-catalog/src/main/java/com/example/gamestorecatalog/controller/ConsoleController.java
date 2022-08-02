package com.example.gamestorecatalog.controller;

import com.example.gamestorecatalog.model.Console;
import com.example.gamestorecatalog.repository.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/console")
@CrossOrigin(origins = {"http://localhost:7475"})
public class ConsoleController {

    @Autowired
    ConsoleRepository consoleRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Console createConsole(@RequestBody @Valid Console console){
        consoleRepo.save(console);
        return console;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Console getConsoleById(@PathVariable("id") long consoleId){
        Optional<Console> console = consoleRepo.findById(consoleId);
        if (console == null) {
            throw new IllegalArgumentException("Console not found with this id");
        } else {
            return (console.get());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable("id") long consoleId) {
        consoleRepo.deleteById(consoleId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConsole(@RequestBody @Valid Console console){
        if (console == null || console.getId() < 1){
            throw new IllegalArgumentException("Console does not exists with this iD");
        } else if (console.getId() > 0) {
            consoleRepo.save(console);
        }
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getConsolesByManufacturer(@PathVariable("manufacturer") String manu){
        List<Console> consolesBySize = consoleRepo.findAllByManufacturer(manu);
        if (consolesBySize == null || consolesBySize.isEmpty()){
            throw new IllegalArgumentException("No Consoles with that size");
        } else {
            return consolesBySize;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getAllConsoles() {
        List<Console> allConsoles = consoleRepo.findAll();
        if (allConsoles == null || allConsoles.isEmpty()) {
            throw new IllegalArgumentException("No Consoles were found.");
        } else {
            return allConsoles;
        }
    }
}
