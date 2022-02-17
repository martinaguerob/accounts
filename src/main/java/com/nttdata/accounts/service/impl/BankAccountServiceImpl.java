package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.model.MovementBankAccount;
import com.nttdata.accounts.repository.BankAccountRepository;
import com.nttdata.accounts.repository.CreditCardRepository;
import com.nttdata.accounts.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    WebClient webClient;

    @Override
    public Flux<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> save(BankAccount entity) {
        System.out.println("Se llegó a la función guardar");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(entity.getBalance());
        movementBankAccount.setDescription("Apertura de cuenta");
        movementBankAccount.setIdAccount(entity.getNumberAccount());
        movementBankAccount.setStatus(true);
        this.saveMovementBankAccount(movementBankAccount);
        entity.setStatus(true);
        return bankAccountRepository.save(entity);
    }

    @Override
    public Mono<BankAccount> update(BankAccount entity) {
        return  bankAccountRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(entity.getStatus());
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
    public Mono<BankAccount> saveSavingAccount(BankAccount entity) {
        //Lógica para guardar cuenta de ahorros
        System.out.println("Se guarda en ahorros");
        return save(entity);

    }

    @Override
    public Mono<BankAccount> saveCurrentAccount(BankAccount entity) {
        System.out.println("Se guarda en corriente");
        return save(entity);
    }

    @Override
    public Mono<BankAccount> saveFixedTerm(BankAccount entity) {
        System.out.println("Se guarda en fija");
        return save(entity);
    }

    @Override
    public Flux<Customers> getCustomers() {
        Flux<Customers> customers = webClient
                .get()
                .uri("http://localhost:8080/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Customers.class);
        return customers;
    }

    @Override
    public Mono<Customers> getCustomer(String idCustomer) {
        Mono<Customers> customer = webClient
                .get()
                .uri("http://localhost:8080/customers/{idCustomer}", idCustomer)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customers.class);
        return customer;
    }

    @Override
    public Mono<MovementBankAccount> saveMovementBankAccount(MovementBankAccount movementBankAccount) {
        System.out.println("Se llegó a la función guardar movimiento");
        Mono<MovementBankAccount> movement = webClient
                .post()
                .uri("http://localhost:8080/movements/bank-account", movementBankAccount)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MovementBankAccount.class);
        return movement;
    }
}
