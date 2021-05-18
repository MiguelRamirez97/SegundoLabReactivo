package com.example.demo.controller;

import com.example.demo.model.Card;
import com.example.demo.model.validation;
import com.example.demo.repo.CardRepository;
import com.example.demo.service.CardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CardController.class)
class CardControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private CardService cardService;

    @Captor
    private ArgumentCaptor<Mono<Card>> argumentCaptor;

    @MockBean
    private CardRepository repository;

    @ParameterizedTest
    @CsvSource({"06123,debito,121"})
    void post(String number, String title,String code) {

//        if(times == 0) {
//            when(repository.findByName(name)).thenReturn(Mono.just(new Person()));
//        }
//
//        if(times == 1) {
//            when(repository.findByName(name)).thenReturn(Mono.empty());
//        }

        var card = new Card(title,number,code);
        card.setType(validation.asignarType(number));
        var request = Mono.just(card);
        webTestClient.post()
                .uri("/card")
                .body(request, Card.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        verify(cardService).insertar(argumentCaptor.capture());
        //verify(repository, times(times)).save(any());

        card = argumentCaptor.getValue().block();

        Assertions.assertEquals(number, card.getNumber());

    }
}