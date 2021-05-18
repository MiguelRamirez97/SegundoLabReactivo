package com.example.demo.repo;

import com.example.demo.model.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface CardRepository extends ReactiveMongoRepository<Card,String> {
          Flux<Card> findByType(String type);
}
