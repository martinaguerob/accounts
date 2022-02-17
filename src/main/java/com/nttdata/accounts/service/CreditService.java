package com.nttdata.accounts.service;

import com.nttdata.accounts.entity.Credit;
import reactor.core.publisher.Mono;

public interface CreditService extends CrudService<Credit, String>{

    Mono<Credit> findByCode(String code);
}
