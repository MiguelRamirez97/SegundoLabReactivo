package com.example.demo.service;

import com.example.demo.model.Card;
import com.example.demo.repo.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

import static com.example.demo.model.validation.asignarType;

@Service
public class CardService {
    private final BiFunction<CardRepository, Card, Mono<Card>> validateBeforeInsert
            = (repo, card) -> repo.findByNumber(card.getNumber());

    @Autowired
    private CardRepository repository;

    //public Flux<Card> listAll() {
    //    return repository.findAll();
    //}

    public Mono<Card> insertar(Mono<Card> cardMono) {
//        return cardMono
//                .flatMap(card -> validateBeforeInsert.apply(repository, card))
//                .switchIfEmpty(Mono.defer(() ->cardMono.flatMap(card -> {
//                            card.setType(asignarType(card.getNumber()));
//                            return repository.save(card);
//                        })));
        return cardMono.flatMap(card -> {
                    card.setType(asignarType(card.getNumber()));
                    return repository.save(card);
                });
    }
}
