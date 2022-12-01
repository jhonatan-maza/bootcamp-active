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
public class ActiveServiceImpl implements ActiveService {

    @Autowired
    private ActiveStaffRepository activeStaffRepository;
    @Autowired
    private ActiveBusinessRepository activeBusinessRepository;
    @Autowired
    private ActiveCreditCardRepository activeCreditCardRepository;

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
        Mono<ActiveStaff> activeMono= findByAccountNumberStaff(dataActiveStaff.getAccountNumber())
                .flatMap(__ -> Mono.<ActiveStaff>error(new Error("La cuenta activa personal con numero " + dataActiveStaff.getAccountNumber() + " YA EXISTE")))
                .switchIfEmpty(activeStaffRepository.save(dataActiveStaff));
        return activeMono;

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
    @Override
    public Flux<ActiveCreditCard> findAllCreditCard() {
        Flux<ActiveCreditCard> actives = activeCreditCardRepository.findAll();
        return actives;

    }

    @Override
    public Flux<ActiveCreditCard> findByCustomerCreditCard(String dni) {
        Flux<ActiveCreditCard> actives = activeCreditCardRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni));
        return actives;

    }

    @Override
    public Mono<ActiveCreditCard> findByAccountNumberCreditCard(String accountNumber) {
        Mono<ActiveCreditCard> active = activeCreditCardRepository
                .findAll()
                .filter(x -> x.getAccountNumber().equals(accountNumber))
                .next();
        return active;

    }

    @Override
    public Mono<ActiveCreditCard> saveCreditCard(ActiveCreditCard dataActiveCreditCard){
        Mono<ActiveCreditCard> activeMono= findByAccountNumberBusiness(dataActiveCreditCard.getAccountNumber())
                .flatMap(__ -> Mono.<ActiveCreditCard>error(new Error("La cuenta activa credito con numero " + dataActiveCreditCard.getAccountNumber() + " YA EXISTE")))
                .switchIfEmpty(activeCreditCardRepository.save(dataActiveCreditCard));
        return activeMono;

    }

    @Override
    public Mono<ActiveCreditCard> updateCreditCard(ActiveCreditCard dataActiveCreditCard) {
        Mono<ActiveCreditCard> activeMono = findByAccountNumberCreditCard(dataActiveCreditCard.getAccountNumber());
        //.delayElement(Duration.ofMillis(1000));
        try {
            dataActiveCreditCard.setDni(activeMono.block().getDni());
            dataActiveCreditCard.setCreationDate(activeMono.block().getCreationDate());
            return activeCreditCardRepository.save(dataActiveCreditCard);
        }catch (Exception e){
            return Mono.<ActiveCreditCard>error(new Error("La cuenta activa personal con numero " + dataActiveCreditCard.getAccountNumber() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Void> deleteCreditCard(String accountNumber) {
        Mono<ActiveCreditCard> activeMono = findByAccountNumberCreditCard(accountNumber);
        try{
            ActiveCreditCard activeStaff = activeMono.block();
            return activeCreditCardRepository.delete(activeStaff);
        }catch (Exception e){
            return Mono.<Void>error(new Error("La cuenta activa credito con numero \" + dataActive.getAccountNumber() + \" NO EXISTE"));
        }

    }

}
