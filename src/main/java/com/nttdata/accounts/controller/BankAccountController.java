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
        return bankAccount.getType().equals("ahorros")
                ?  bankAccountService.saveSavingAccount(bankAccount)
            : bankAccount.getType().equals("cuenta corriente")
                ? bankAccountService.saveCurrentAccount(bankAccount)
                : bankAccountService.saveFixedTerm(bankAccount);
    }

    @PutMapping("/bank-account/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount> updateBankAccount(@RequestBody BankAccount bankAccount, @PathVariable String id){
        System.out.println("Actualizar cuenta bancaria");
        return bankAccountService.update(bankAccount, id);
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

    @GetMapping("/bank-account/number/{numberAccount}")
    @ResponseStatus(HttpStatus.OK)
    public  Mono<BankAccount> findAccountBankByNumberAccount(@PathVariable String numberAccount){
        System.out.println("Buscar cuenta bancaria por n??mero de cuenta");
        return bankAccountService.findByNumberAccount(numberAccount);
    }

    @GetMapping("/bank-account/customers")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Customers> getCustomers(){
        System.out.println("Listar clientes");
        return bankAccountService.getCustomers();
    }
    @GetMapping("/bank-account/{idCustomer}/{type}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount> getCustomerByIdAndType(@PathVariable String idCustomer, String type){
        System.out.println("Listar cliente por id: "+ idCustomer);
        return bankAccountService.findByIdCustomerAndType(idCustomer, type);
    }

    @GetMapping("/bank-account/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Customers> getCustomerById(@PathVariable String id){
        System.out.println("Listar cliente por id: "+ id);
        return bankAccountService.getCustomerById(id);
    }
}