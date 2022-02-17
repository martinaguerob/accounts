package com.nttdata.accounts.controller;

import com.nttdata.accounts.entity.BankAccount;
import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;

    @GetMapping("/bank-account")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BankAccount> getBankAccount(){
        System.out.println("Listar cuentas bancarias");
        return bankAccountService.findAll();
    }

    @PostMapping("/bank-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BankAccount> saveBankAccount(@RequestBody BankAccount bankAccount){
        if (bankAccount.getType() == "ahorro"){
            System.out.println("Guardar cuenta de ahorros");
            return bankAccountService.saveSavingAccount(bankAccount);
        }else if(bankAccount.getType() == "corriente"){
            System.out.println("Guardar cuenta corriente");
            return bankAccountService.saveSavingAccount(bankAccount);
        }else{
            System.out.println("Guardar cuenta a plazo fijo");
            return bankAccountService.saveSavingAccount(bankAccount);
        }

    }

    @PutMapping("/bank-account/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount> updateBankAccount(@RequestBody BankAccount bankAccount){
        System.out.println("Actualizar cuenta bancaria");
        return bankAccountService.update(bankAccount);
    }

    @PutMapping("/bank-account/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount> deleteBankAccount(@PathVariable String id){
        System.out.println("Eliminar cuenta bancaria");
        return bankAccountService.deleteById(id);
    }

    @GetMapping("/bank-account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  Mono<BankAccount> findAccountBankId(@PathVariable String id){
        System.out.println("Buscar cuenta bancaria");
        return bankAccountService.findById(id);
    }
    @GetMapping("/bank-account/customers")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Customers> getCustomers(){
        System.out.println("Listar clientes");
        return bankAccountService.getCustomers();
    }
}