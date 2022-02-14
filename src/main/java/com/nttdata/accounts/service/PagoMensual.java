package com.nttdata.accounts.service;

import com.nttdata.accounts.model.Credit;

@FunctionalInterface
public interface PagoMensual {

    double calcular(float amount, float tea, float time);

}
