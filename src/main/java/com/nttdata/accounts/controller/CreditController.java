package com.nttdata.accounts.controller;

import com.nttdata.accounts.entity.Credit;
import com.nttdata.accounts.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credit")
public class CreditController {

    @Autowired
    CreditService creditService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Credit> getCredit(){
        System.out.println("Listar créditos");
        return creditService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Credit> saveCredit(@RequestBody Credit credit){
        System.out.println("Guardar crédito");
        return creditService.save(credit);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Credit> updateCredit(@RequestBody Credit credit){
        System.out.println("Actualizar crédito");
        return creditService.update(credit);
    }

    @PutMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Credit> deleteCredit(@PathVariable String id){
        System.out.println("Eliminar crédito");
        return creditService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  Mono<Credit> findCreditId(@PathVariable String id){
        System.out.println("Buscar crédito");
        return creditService.findById(id);
    }
}