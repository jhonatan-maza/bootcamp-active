package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface ActiveCreditCardService {


    public Flux<ActiveCreditCard> findAllCreditCard();
    public Mono<ActiveCreditCard> findByAccountNumberCreditCard(String accountNumber);
    public Flux<ActiveCreditCard> findByCustomerCreditCard(String dni);
    public Mono<ActiveCreditCard> saveCreditCard(ActiveCreditCard activeCreditCard);
    public Mono<ActiveCreditCard> updateCreditCard(ActiveCreditCard activeCreditCard);
    public Mono<Void> deleteCreditCard(String accountNumber);

}
