package com.nttdata.accounts.repository;

import com.nttdata.accounts.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

    Mono<Credit> findByCode(String code);

}
