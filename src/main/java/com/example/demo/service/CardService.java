package com.example.demo.service;

import com.example.demo.model.Card;
import com.example.demo.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

import static com.example.demo.model.validation.asignarType;

@Service
public class CardService {

    @Autowired
    private CardRepository repository;

    public Mono<Card> insertar(Mono<Card> cardMono) {
        return cardMono.flatMap(card -> {
                    card.setType(asignarType(card.getNumber()));
                    return repository.save(card);
                });
    }

    public Mono<Card> getCardById(String id){
        return repository.findById(id);
    }
}
