package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.model.BankAccount;
import com.nttdata.accounts.repository.BankAccountRepository;
import com.nttdata.accounts.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public Flux<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> save(BankAccount entity) {
        entity.setStatus(true);
        return bankAccountRepository.save(entity);
    }

    @Override
    public Mono<BankAccount> update(BankAccount entity) {
        return  bankAccountRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setCustomer(entity.getCustomer());
                    origin.setStatus(entity.getStatus());
                    origin.setTypeCustomer(entity.getTypeCustomer());
                    origin.setNumberAccount(entity.getNumberAccount());
                    origin.setType(entity.getType());
                    return bankAccountRepository.save(origin);
                });
    }

    @Override
    public Mono<BankAccount> deleteById(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(false);
                    return bankAccountRepository.save(origin);
                });
    }

    @Override
    public Mono<BankAccount> findById(String id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public Mono<BankAccount> findByTypeAndCustomer(String type, String customer) {
        return bankAccountRepository.findByTypeAndCustomer(type, customer);
    }
}
