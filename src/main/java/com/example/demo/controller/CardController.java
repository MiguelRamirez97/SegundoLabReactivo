package com.example.demo.controller;

import com.example.demo.model.Card;
import com.example.demo.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/card")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping(value = "/create")
    public Mono<Card> create(@RequestBody Mono<Card> cardMono) {
        return cardService.insertar(cardMono);
    }

    @GetMapping(value = "/getCard/{id}")
    public Mono<Card> getCardById(@PathVariable("id") String id){
        return cardService.getCardById(id);
    }

    @PutMapping(value = "/update")
    public Mono<Card> update(@RequestBody Mono<Card> cardMono) {
        return cardService.insertar(cardMono);
    }

    @DeleteMapping("/deleteCard/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return cardService.deleteById(id);
    }
}
