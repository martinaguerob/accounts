package com.nttdata.accounts.repository;

import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Flux<BankAccount> findByIdCustomer(String idCustomer);

    Mono<BankAccount> findByIdCustomerAndType(String idCustomer, String type);

    Mono<BankAccount> findByIdCustomerAndCodProfile(String idCustomer, String codProfile);
}
