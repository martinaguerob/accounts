package com.nttdata.accounts.repository;

import com.nttdata.accounts.model.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

    Mono<BankAccount> findByTypeAndCustomer(String type, String customer);
}
