package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.config.WebClientConfig;
import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.entity.CreditCard;
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
        System.out.println("Listar todos");
        return bankAccountRepository.findAll();
    }

    @Override
    public Mono<BankAccount> save(BankAccount entity) {
        entity.setDate(new Date());
        entity.setStatus(true);
        this.saveMovementBankAccount(entity.getNumberAccount(), entity.getBalance()).subscribe();
        return bankAccountRepository.save(entity);
    }

    @Override
    public Mono<BankAccount> update(BankAccount entity, String id) {
        return  bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setType(entity.getType()==null ? origin.getType() : entity.getType());
                    origin.setNumberAccount(entity.getNumberAccount()==null ? origin.getNumberAccount() : entity.getNumberAccount());
                    origin.setIdCustomer(entity.getIdCustomer()==null ? origin.getIdCustomer() : entity.getIdCustomer());
                    origin.setCodProfile(entity.getCodProfile()==null ? origin.getCodProfile() : entity.getCodProfile());
                    origin.setBalance(entity.getBalance()==null ? origin.getBalance() : entity.getBalance());
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
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());
        Mono<BankAccount>personalb001 = bankAccountRepository.findByIdCustomerAndCodProfile(entity.getIdCustomer(), "personalb001");
        Mono<CreditCard>creditCard01 = creditCardRepository.findByIdCustomerAndCodProfile(entity.getIdCustomer(), "vip001");


        return customer
                .filter(c -> c.getCodProfile().equals("vip001") || c.getCodProfile().equals("personalb001"))
                .flatMap(cc -> {
                    entity.setCodProfile(cc.getCodProfile());
                            return cc.getCodProfile().equals("personalb001")
                                    ? personalb001.switchIfEmpty(save(entity))
                                /*: cc.getCodProfile() == "vip001"
                                ? creditCard01.flatMap(cc->()).switchIfEmpty(save(entity)) */
                                    : Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT));

                        }
                );
    }


    @Override
    public Mono<BankAccount> saveCurrentAccount(BankAccount entity) {
        //Lógica para guardar en cuenta corriente
        System.out.println("Se guarda en corriente");
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());
        Mono<BankAccount> personalb001 = bankAccountRepository.findByIdCustomerAndCodProfile(entity.getIdCustomer(), "personalb001");
        Mono<BankAccount> vip001 = bankAccountRepository.findByIdCustomerAndCodProfile(entity.getIdCustomer(), "vip001");
        Mono<CreditCard>pyme001 = creditCardRepository.findByIdCustomerAndCodProfile(entity.getIdCustomer(), "pyme001");

        return customer
                .flatMap(c ->
                        {
                            entity.setCodProfile(c.getCodProfile());
                            return c.getCodProfile() == "personalb001" ? personalb001.switchIfEmpty(save(entity))
                                    : c.getCodProfile() == "vip001" ? vip001.switchIfEmpty(save(entity))
                                    : c.getCodProfile() == "empresarialb001" ? save(entity)
                                    /*: c.getCodProfile() == "pyme001" ?  pyme001.switchIfEmpty(save(entity))*/
                                    : Mono.empty();
                        }

                );


    }

    @Override
    public Mono<BankAccount> saveFixedTerm(BankAccount entity) {
        //Lógica para guardar en cuenta plazo fijo
        System.out.println("Se guarda en plazo fijo");
        Mono<Customers> customer = webClientConfig.getCustomerById(entity.getIdCustomer());
        return customer
                .filter(c -> c.getCodProfile().equals("vip001") || c.getCodProfile().equals("personalb001"))
                .flatMap(cu-> {
                    System.out.println("Se entró al flatmap:" + cu.getCodProfile());
                    entity.setCodProfile(cu.getCodProfile());
                    Mono<BankAccount> bankAccountMono = bankAccountRepository.findByIdCustomerAndType(entity.getIdCustomer(), entity.getType());
                    return bankAccountMono
                            .switchIfEmpty(save(entity));
                });


    }

    @Override
    public Mono<BankAccount> findByIdCustomerAndType(String idCustomer, String type) {
        return findByIdCustomerAndType(idCustomer, type);
    }



    @Override
    public Flux<BankAccount> findByIdCustomer(String idCustomer) {
        return bankAccountRepository.findByIdCustomer(idCustomer);
    }

    @Override
    public Mono<BankAccount> findByNumberAccount(String numberAccount) {
        return bankAccountRepository.findByNumberAccount(numberAccount);
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
    public Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount) {
        System.out.println("Se llegó a la función guardar movimiento");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Apertura de cuenta");
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
