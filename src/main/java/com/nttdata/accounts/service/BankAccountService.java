package com.nttdata.accounts.service;

import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.model.MovementBankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService extends CrudService<BankAccount, String>{

    //Guardar Cuenta Ahorros
    Mono<BankAccount> saveSavingAccount(BankAccount entity);
    //Guardar Cuenta Corriente
    Mono<BankAccount> saveCurrentAccount(BankAccount entity);
    //Guardar Cuenta Plazo Fijo
    Mono<BankAccount> saveFixedTerm(BankAccount entity);
    //Buscar cuenta por cliente y tipo de cuenta
    Mono<BankAccount> findByIdCustomerAndType(String idCustomer, String type);
    //Listar cuentas por cliente
    Flux<BankAccount> findByIdCustomer(String idCustomer);
    //Buscar cuenta por número de cuenta
    Mono<BankAccount> findByNumberAccount(String numberAccount);


    //Customers
    Flux<Customers> getCustomers();
    Mono<Customers> getCustomerById(String id);

    //Movement
    Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount);
}
