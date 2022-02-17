package com.nttdata.accounts.controller;

import com.nttdata.accounts.entity.CreditCard;
import com.nttdata.accounts.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit-card")
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CreditCard> getCreditCard(){
        System.out.println("Listar tarjetas de crédito");
        return creditCardService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCard> saveCreditCard(@RequestBody CreditCard creditCard){
        System.out.println("Guardar tarjeta de crédito");
        return creditCardService.save(creditCard);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditCard> updateCreditCard(@RequestBody CreditCard creditCard, @PathVariable String id){
        System.out.println("Actualizar tarjeta de crédito");
        return creditCardService.update(creditCard);
    }

    @PutMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditCard> deleteCreditCard(@PathVariable String id){
        System.out.println("Eliminar tarjeta de crédito");
        return creditCardService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  Mono<CreditCard> findCreditCardId(@PathVariable String id){
        System.out.println("Buscar tarjetas de crédito");
        return creditCardService.findById(id);
    }
}