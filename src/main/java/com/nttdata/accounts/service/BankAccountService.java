package com.nttdata.accounts.service;

import com.nttdata.accounts.model.BankAccount;
import reactor.core.publisher.Mono;

public interface BankAccountService extends CrudService<BankAccount, String>{

    Mono<BankAccount> findByTypeAndCustomer(String type, String customer);
}
