package com.nttdata.accounts.service;

import com.nttdata.accounts.model.Credit;
import reactor.core.publisher.Mono;

public interface CreditService extends CrudService<Credit, String>{

    Mono<Credit> findByCode(String code);
}
