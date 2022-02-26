package com.nttdata.accounts.service;

@FunctionalInterface
public interface PagoMensual {

    double calcular(Double amount, Double tea, Double time);

}
