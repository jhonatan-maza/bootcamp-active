package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface ActiveStaffService {

    public Flux<ActiveStaff> findAllStaff();
    public Mono<ActiveStaff> findByAccountNumberStaff(String accountNumber);
    public Flux<ActiveStaff> findByCustomerStaff(String dni);
    public Mono<ActiveStaff> saveStaff(ActiveStaff activeStaff);
    public Mono<ActiveStaff> updateStaff(ActiveStaff dataActiveStaff);
    public Mono<Void> deleteStaff(String accountNumber);


}
