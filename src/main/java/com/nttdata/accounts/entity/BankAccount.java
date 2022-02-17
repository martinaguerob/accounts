package com.nttdata.accounts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "bank-account")
public class BankAccount {

    @Id
    private String id;
    private String type; // Ahorro, corriente o fijo
    private String numberAccount; //NúmeroDeCuenta
    private String idCustomer; //Id del cliente
    private Float balance; //Saldo
    private Boolean status;
}
