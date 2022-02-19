package com.nttdata.accounts.config;

import com.nttdata.accounts.model.Customers;
import com.nttdata.accounts.model.MovementBankAccount;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    private final WebClient.Builder webClientBuilder = WebClient.builder();

    public Flux<Customers> getCustomers(){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/customers")
                .retrieve()
                .bodyToFlux(Customers.class);
    }

    public Mono<Customers> getCustomerById(@PathVariable String id){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/customers/"+id)
                .retrieve()
                .bodyToMono(Customers.class);
    }

    public Mono<MovementBankAccount> saveMovementBankAccount(MovementBankAccount movementBankAccount){
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8080/movements/bank-account", movementBankAccount)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(movementBankAccount))
                .retrieve()
                .bodyToMono(MovementBankAccount.class);
    }
}
