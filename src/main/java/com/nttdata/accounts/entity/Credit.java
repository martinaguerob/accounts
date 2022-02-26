package com.nttdata.accounts.entity;

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
    private String type; //Tipo de crédito: personal o empresarial
    private Double amount; //Monto prestamos
    private Double tea; //Tasa efectiva anual
    private Double feeNumber; //Tiempo en meses
    private Double feeMonthly; //Cuota mensual -> Se calcula automáticamente
    private Boolean status;
    private String idCustomer;
}
