package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.entity.CreditCard;
import com.nttdata.accounts.repository.CreditCardRepository;
import com.nttdata.accounts.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;


    @Override
    public Flux<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    @Override
    public Mono<CreditCard> save(CreditCard entity) {
        entity.setStatus(true);
        return creditCardRepository.save(entity);
    }

    @Override
    public Mono<CreditCard> update(CreditCard entity) {
        return  creditCardRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(entity.getStatus());
                    origin.setBalance(entity.getBalance());
                    return creditCardRepository.save(origin);
                });
    }

    @Override
    public Mono<CreditCard> deleteById(String id) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(false);
                    return creditCardRepository.save(origin);
                });
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public Mono<CreditCard> findByIdCustomer(String idCustomer) {
        return creditCardRepository.findByIdCustomer(idCustomer);
    }
}
