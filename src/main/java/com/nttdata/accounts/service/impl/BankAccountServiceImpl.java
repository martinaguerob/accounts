package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.config.WebClientConfig;
import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.model.MovementBankAccount;
import com.nttdata.accounts.repository.BankAccountRepository;
import com.nttdata.accounts.repository.CreditCardRepository;
import com.nttdata.accounts.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    CreditCardRepository creditCardRepository;
    private final WebClientConfig webClientConfig = new WebClientConfig();

    @Override
    public Flux<BankAccount> findAll() {
        System.out.println(bankAccountRepository.findAll());
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> save(BankAccount entity) {
        System.out.println("Se llegó a la función guardar");
        entity.setDate(new Date());
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
        System.out.println("Id: "+ webClientConfig.getCustomerById(entity.getIdCustomer()));
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());
        return customer
                .filter(c -> (c.getCodProfile().equals("vip001") || c.getCodProfile().equals("personalb001")) && bankAccountRepository.findByIdCustomer(c.getId())== null )
                .flatMap(origin -> origin.getCodProfile().equals("vip001") && creditCardRepository.findByIdCustomer(entity.getIdCustomer())==null
                        ? this.save(entity)
                        : !(origin.getCodProfile().equals("vip001"))
                        ? this.save(entity) : Mono.empty())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<BankAccount> saveCurrentAccount(BankAccount entity) {
        //Lógica para guardar en cuenta corriente
        System.out.println("Se guarda en corriente");
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());
        return customer
                .filter(c ->
                        (((c.getCodProfile().equals("personalb001")) || (c.getCodProfile().equals("vip001")))
                                && bankAccountRepository.findByIdCustomer(entity.getIdCustomer()).equals(""))
                                || (c.getCodProfile().equals("pyme001") || c.getCodProfile().equals("empresarialb001")))
                .flatMap(origin -> origin.getCodProfile().equals("pyme001") && creditCardRepository.findByIdCustomer(entity.getIdCustomer())==null
                        ? this.save(entity)
                        : !(origin.getCodProfile().equals("pyme001"))
                        ? this.save(entity) : Mono.empty()
                ).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<BankAccount> saveFixedTerm(BankAccount entity) {
        //Lógica para guardar en cuenta plazo fijo
        System.out.println("Se guarda en plazo fijo");
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());

        return customer
                .filter(c ->
                        ((c.getCodProfile().equals("personalb001")) || (c.getCodProfile().equals("vip001")))
                         && bankAccountRepository.findByIdCustomer(c.getId())==null
                )
                .flatMap(origin->this.save(entity))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT)));
    }

    @Override
    public Flux<Customers> getCustomers() {
        return webClientConfig.getCustomers();
    }

    @Override
    public Mono<Customers> getCustomerById(String id) {
        return webClientConfig.getCustomerById(id);
    }

    @Override
    public Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Float amount) {
        System.out.println("Se llegó a la función guardar movimiento");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Apertura de cuenta");
        movementBankAccount.setIdAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
