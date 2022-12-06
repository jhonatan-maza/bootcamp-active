package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.repository.ActiveBusinessRepository;
import com.nttdata.bootcamp.repository.ActiveCreditCardRepository;
import com.nttdata.bootcamp.repository.ActiveStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Service implementation
@Service
public class ActiveStaffServiceImpl implements ActiveStaffService {

    @Autowired
    private ActiveStaffRepository activeStaffRepository;

    @Override
    public Flux<ActiveStaff> findAllStaff() {
        Flux<ActiveStaff> actives = activeStaffRepository.findAll();
        return actives;

    }

    @Override
    public Flux<ActiveStaff> findByCustomerStaff(String dni) {
        Flux<ActiveStaff> actives = activeStaffRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni));
        return actives;

    }

    @Override
    public Mono<ActiveStaff> findByAccountNumberStaff(String accountNumber) {
        Mono<ActiveStaff> active = activeStaffRepository
                .findAll()
                .filter(x -> x.getAccountNumber().equals(accountNumber))
                .next();
        return active;

    }

    @Override
    public Mono<ActiveStaff> saveStaff(ActiveStaff dataActiveStaff) {
        Mono<ActiveStaff> activeStaffMono = Mono.empty();

        if(dataActiveStaff.getTypeCustomer().equals("PERSONAL")){
            activeStaffMono = this.findByCustomerStaff(dataActiveStaff.getDni()).next();
        }
        return activeStaffMono
                .flatMap(__ -> Mono.<ActiveStaff>error(new Error("The customer have a staff account")))
                .switchIfEmpty(activeStaffRepository.save(dataActiveStaff));
    }


    @Override
    public Mono<ActiveStaff> updateStaff(ActiveStaff dataActiveStaff) {
        Mono<ActiveStaff> activeMono = findByAccountNumberStaff(dataActiveStaff.getAccountNumber());
        //.delayElement(Duration.ofMillis(1000));
        try {
            dataActiveStaff.setDni(activeMono.block().getDni());
            dataActiveStaff.setCreationDate(activeMono.block().getCreationDate());
            return activeStaffRepository.save(dataActiveStaff);
        }catch (Exception e){
            return Mono.<ActiveStaff>error(new Error("La cuenta activa personal con numero " + dataActiveStaff.getAccountNumber() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Void> deleteStaff(String accountNumber) {
        Mono<ActiveStaff> activeMono = findByAccountNumberStaff(accountNumber);
        try{
            ActiveStaff activeStaff = activeMono.block();
            return activeStaffRepository.delete(activeStaff);
        }catch (Exception e){
            return Mono.<Void>error(new Error("La cuenta activa personal con numero \" + dataActive.getAccountNumber() + \" NO EXISTE"));
        }

    }


}
