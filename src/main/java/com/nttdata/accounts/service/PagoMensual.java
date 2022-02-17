package com.nttdata.accounts.service;

@FunctionalInterface
public interface PagoMensual {

    double calcular(float amount, float tea, float time);

}
