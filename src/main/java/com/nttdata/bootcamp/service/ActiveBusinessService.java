package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface ActiveBusinessService {


    public Flux<ActiveBusiness> findAllBusiness();
    public Mono<ActiveBusiness> findByAccountNumberBusiness(String accountNumber);
    public Flux<ActiveBusiness> findByCustomerBusiness(String dni);
    public Mono<ActiveBusiness> saveBusiness(ActiveBusiness activeBusiness);
    public Mono<ActiveBusiness> updateBusiness(ActiveBusiness activeBusiness);
    public Mono<Void> deleteBusiness(String accountNumber);


}
