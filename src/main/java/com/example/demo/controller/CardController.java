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

}
