package com.nttdata.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementBankAccount {

    private String id;
    private String description;
    private Float amount;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date date;
    private Boolean status;
    private String idAccount;
}
