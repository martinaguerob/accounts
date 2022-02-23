package com.nttdata.accounts.repository;

import com.nttdata.accounts.entity.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

    Mono<CreditCard>findByIdCustomer(String idCustomer);
    Mono<CreditCard>findByIdCustomerAndCodProfile(String idCustomer, String codProfile);
}
