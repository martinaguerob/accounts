package com.nttdata.accounts.service;

import com.nttdata.accounts.entity.CreditCard;
import reactor.core.publisher.Mono;

public interface CreditCardService extends CrudService<CreditCard, String>{

    Mono<CreditCard> findByIdCustomer(String idCustomer);

}
