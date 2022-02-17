package com.nttdata.accounts.repository;

import com.nttdata.accounts.entity.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Mono<BankAccount> findByIdCustomer(String idCustomer);
}
