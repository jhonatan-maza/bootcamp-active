package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface ActiveService {

    public Flux<ActiveStaff> findAllStaff();
    public Mono<ActiveStaff> findByAccountNumberStaff(String accountNumber);
    public Flux<ActiveStaff> findByCustomerStaff(String dni);
    public Mono<ActiveStaff> saveStaff(ActiveStaff activeStaff);
    public Mono<ActiveStaff> updateStaff(ActiveStaff dataActiveStaff);
    public Mono<Void> deleteStaff(String accountNumber);

    public Flux<ActiveBusiness> findAllBusiness();
    public Mono<ActiveBusiness> findByAccountNumberBusiness(String accountNumber);
    public Flux<ActiveBusiness> findByCustomerBusiness(String dni);
    public Mono<ActiveBusiness> saveBusiness(ActiveBusiness activeBusiness);
    public Mono<ActiveBusiness> updateBusiness(ActiveBusiness activeBusiness);
    public Mono<Void> deleteBusiness(String accountNumber);

    public Flux<ActiveCreditCard> findAllCreditCard();
    public Mono<ActiveCreditCard> findByAccountNumberCreditCard(String accountNumber);
    public Flux<ActiveCreditCard> findByCustomerCreditCard(String dni);
    public Mono<ActiveCreditCard> saveCreditCard(ActiveCreditCard activeCreditCard);
    public Mono<ActiveCreditCard> updateCreditCard(ActiveCreditCard activeCreditCard);
    public Mono<Void> deleteCreditCard(String accountNumber);

}
