package com.example.demo.controller;

import com.example.demo.model.Card;
import com.example.demo.model.validation;
import com.example.demo.repo.CardRepository;
import com.example.demo.service.CardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import reactor.core.publisher.Flux;
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
        Card prueba = new Card("debito", "06123","123");
        prueba.setType(validation.asignarType(prueba.getNumber()));
        var request = Mono.just(prueba);
        when(repository.save(any(Card.class))).thenReturn(request);
        webTestClient.post()
                .uri("/card/create")
                .body(request, Card.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult();

        verify(cardService).insertar(argumentCaptor.capture());

        var card = argumentCaptor.getValue().block();

        Assertions.assertEquals(number, card.getNumber());

    }

    @Test
    void get() {
        Card prueba = new Card("debito", "06123","123");
        prueba.setType(validation.asignarType(prueba.getNumber()));
        when(repository.findById(anyString())).thenReturn(Mono.just(prueba));
        webTestClient.get()
                .uri("/card/getCard/06123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Card.class)
                .consumeWith(cardEntityExchangeResult -> {
                    var card = cardEntityExchangeResult.getResponseBody();
                    //assert card != null;
                    Assertions.assertEquals("VISA", card.getType());
                    System.out.println(card);
                });
    }
    @Test
    void update() {
        Card prueba = new Card("hola","03123","111");
        prueba.setType(validation.asignarType(prueba.getNumber()));
        var request = Mono.just(prueba);
        when(repository.save(any(Card.class))).thenReturn(request);
        webTestClient.put()
                .uri("/card/update")
                .body(request, Card.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().returnResult();
    }
    @Test
    void delete(){
        webTestClient.delete()
                .uri("/card/deleteCard/12888")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
    @Test
    void list() {
        var list = Flux.just(
                new Card("sodexo","060606","123"),
                new Card("debito","121212","321")
        );
        when(repository.findAll()).thenReturn(list);

        webTestClient.get()
                .uri("/card/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("sodexo")
                .jsonPath("$[0].number").isEqualTo("060606")
                .jsonPath("$[0].code").isEqualTo("123")
                .jsonPath("$[1].title").isEqualTo("debito")
                .jsonPath("$[1].number").isEqualTo("121212")
                .jsonPath("$[1].code").isEqualTo("321");

        verify(cardService).listAll();
        verify(repository).findAll();
    }
    @Test
    void findByTipe() {
        var list = Flux.just(
                new Card("sodexo","060606","123"),
                new Card("debito","121212","321")
        );
        when(repository.findByType("PRIME")).thenReturn(list);

        webTestClient.get()
                .uri("/card/getType/PRIME")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[1].title").isEqualTo("debito")
                .jsonPath("$[1].number").isEqualTo("121212")
                .jsonPath("$[1].code").isEqualTo("321");

        verify(cardService).getCardForType("PRIME");
        verify(repository).findByType("PRIME");
    }
}