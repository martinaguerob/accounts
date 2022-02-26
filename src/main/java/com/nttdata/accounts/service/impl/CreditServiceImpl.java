package com.nttdata.accounts.service.impl;

import com.nttdata.accounts.entity.Credit;
import com.nttdata.accounts.repository.CreditRepository;
import com.nttdata.accounts.service.CreditService;
import com.nttdata.accounts.service.PagoMensual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    CreditRepository creditRepository;

    @Override
    public Flux<Credit> findAll() {
        return creditRepository.findAll();
    }

    @Override
    public Mono<Credit> save(Credit entity) {
        PagoMensual op = (n1, n2, n3) -> {
            double interes = Math.pow(1+(n2/100), 1/12) - 1;
            System.out.printf("Interes: "+ interes);
            System.out.printf("Calculo: "+ (n1/n3)*(1+interes));
            return (n1/n3)*(1+interes);
        };
        System.out.printf("Mensual sin interes: " + entity.getAmount()/entity.getFeeMonthly());
        Double pagoMensual = op.calcular(entity.getAmount(), entity.getTea(), entity.getFeeMonthly());
        System.out.printf("Mensual con interes: " + pagoMensual);
        entity.setFeeMonthly(pagoMensual);
        entity.setStatus(true);
        return creditRepository.save(entity);
    }

    @Override
    public Mono<Credit> update(Credit entity, String id) {
        PagoMensual op = (n1, n2, n3) -> {

            //Corregir en calculo
            Float interes =  (float)Math.pow(1+(n2/100), 1/12) - 1;
            System.out.printf("  cal: "+ (float)Math.pow(1+(n2/100), 1/12));
            System.out.printf("  Interes: "+ interes);
            System.out.printf("  Calculo: "+ (n1/n3)*(1+interes));
            return (n1/n3)*(1+interes);
        };
        System.out.printf("   Mensual sin interes: " + entity.getAmount()/entity.getFeeMonthly());
        Double pagoMensual = op.calcular(entity.getAmount(), entity.getTea(), entity.getFeeMonthly());
        System.out.printf("   Mensual con interes: " + pagoMensual);


        return  creditRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setIdCustomer(entity.getIdCustomer());
                    origin.setType(entity.getType());
                    origin.setCode(entity.getCode());
                    origin.setAmount(entity.getAmount());
                    origin.setTea(entity.getTea());
                    origin.setFeeMonthly(entity.getFeeMonthly());
                    origin.setFeeMonthly(pagoMensual);
                    origin.setStatus(entity.getStatus());
                    return creditRepository.save(origin);
                });
    }

    @Override
    public Mono<Credit> deleteById(String id) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(false);
                    return creditRepository.save(origin);
                });
    }

    @Override
    public Mono<Credit> findById(String id) {
        return creditRepository.findById(id);
    }

    @Override
    public Mono<Credit> findByCode(String code) {
        return creditRepository.findByCode(code);
    }
}
