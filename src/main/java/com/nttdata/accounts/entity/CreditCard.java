package com.nttdata.accounts.entity;

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
    private Double balance; //Saldo inicial
    private Double stop; //Tope
    private String idCustomer;
    private String codProfile;
    private Boolean status;
}