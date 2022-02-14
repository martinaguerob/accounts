package com.nttdata.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "credit")
public class Credit {

    @Id
    private String id;
    private String code;
    private String customer;
    private String type; //Tipo de crédito: personal o empresarial
    private Float amount; //Monto prestamos
    private Float tea; //Tasa efectiva anual
    private Float monthlyTime; //Tiempo en meses
    private Float feeMonthly; //Cuota mensual -> Se calcula automáticamente
    private Boolean status;
}
