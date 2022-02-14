package com.nttdata.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "credit-card")
public class CreditCard {

    @Id
    private String id;
    private Float balance; //Saldo inicial
    private String customer;
    private Boolean status;
}
