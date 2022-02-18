package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.config.WebClientConfig;
import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.model.MovementBankAccount;
import com.nttdata.accounts.repository.BankAccountRepository;
import com.nttdata.accounts.repository.CreditCardRepository;
import com.nttdata.accounts.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        System.out.println("Id: "+ webClientConfig.getCustomerById(entity.getId()));
        return webClientConfig.getCustomerById(entity.getId())
                .flatMap(c -> {
                            System.out.println("Se entró al get Map: "+ c.getName());
                           return c.getCodProfile().equals("vip001") && creditCardRepository.findById(entity.getId()).equals(null)
                                    ? this.save(entity)
                                    : !(c.getCodProfile().equals("vip001")) ? this.save(entity) : Mono.empty();
                        }
                ).switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<BankAccount> saveCurrentAccount(BankAccount entity) {
        //Lógica para guardar en cuenta corriente
        System.out.println("Se guarda en corriente");
        return webClientConfig.getCustomerById(entity.getId())
                .flatMap(c -> {
                            System.out.println("Se entró al get Map: "+ c.getName());
                            return c.getCodProfile().equals("pyme001") && creditCardRepository.findById(entity.getId()).equals(null)
                                    ? this.save(entity)
                                    : !(c.getCodProfile().equals("pyme001")) ? this.save(entity) : Mono.empty();
                        }
                ).switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<BankAccount> saveFixedTerm(BankAccount entity) {
        //Lógica para guardar en cuenta plazo fijo
        System.out.println("Se guarda en plazo fijo");
        return this.save(entity);
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
