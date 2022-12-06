package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.repository.ActiveBusinessRepository;
import com.nttdata.bootcamp.repository.ActiveCreditCardRepository;
import com.nttdata.bootcamp.repository.ActiveStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Service implementation
@Service
public class ActiveBusinessServiceImpl implements ActiveBusinessService {

    @Autowired
    private ActiveBusinessRepository activeBusinessRepository;



    @Override
    public Flux<ActiveBusiness> findAllBusiness() {
        Flux<ActiveBusiness> actives = activeBusinessRepository.findAll();
        return actives;

    }

    @Override
    public Flux<ActiveBusiness> findByCustomerBusiness(String dni) {
        Flux<ActiveBusiness> actives = activeBusinessRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni));
        return actives;

    }

    @Override
    public Mono<ActiveBusiness> findByAccountNumberBusiness(String accountNumber) {
        Mono<ActiveBusiness> active = activeBusinessRepository
                .findAll()
                .filter(x -> x.getAccountNumber().equals(accountNumber))
                .next();
        return active;

    }

    @Override
    public Mono<ActiveBusiness> saveBusiness(ActiveBusiness dataActiveBusiness) {
        Mono<ActiveBusiness> activeMono= findByAccountNumberBusiness(dataActiveBusiness.getAccountNumber())
                .flatMap(__ -> Mono.<ActiveBusiness>error(new Error("La cuenta activa empresarial con numero " + dataActiveBusiness.getAccountNumber() + " YA EXISTE")))
                .switchIfEmpty(activeBusinessRepository.save(dataActiveBusiness));
        return activeMono;

    }


    @Override
    public Mono<ActiveBusiness> updateBusiness(ActiveBusiness dataActiveBusiness) {
        Mono<ActiveBusiness> activeMono = findByAccountNumberBusiness(dataActiveBusiness.getAccountNumber());
        //.delayElement(Duration.ofMillis(1000));
        try {
            dataActiveBusiness.setDni(activeMono.block().getDni());
            dataActiveBusiness.setCreationDate(activeMono.block().getCreationDate());
            return activeBusinessRepository.save(dataActiveBusiness);
        }catch (Exception e){
            return Mono.<ActiveBusiness>error(new Error("La cuenta activa empresarial con numero " + dataActiveBusiness.getAccountNumber() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Void> deleteBusiness(String accountNumber) {
        Mono<ActiveBusiness> activeMono = findByAccountNumberBusiness(accountNumber);
        try{
            ActiveBusiness activeStaff = activeMono.block();
            return activeBusinessRepository.delete(activeStaff);
        }catch (Exception e){
            return Mono.<Void>error(new Error("La cuenta activa empresarial con numero \" + dataActive.getAccountNumber() + \" NO EXISTE"));
        }

    }


}
