package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.Active;
import com.nttdata.bootcamp.repository.ActiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Service implementation
@Service
public class ActiveServiceImpl implements ActiveService {
    @Autowired
    private ActiveRepository activeRepository;

    @Override
    public Flux<Active> findAll() {
        Flux<Active> actives = activeRepository.findAll();
        return actives;

    }

    @Override
    public Flux<Active> findByCustomer(String dni) {
        Flux<Active> actives = activeRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni));
        return actives;

    }

    @Override
    public Mono<Active> findByAccountNumber(String accountNumber) {
        Mono<Active> active = activeRepository
                .findAll()
                .filter(x -> x.getAccountNumber().equals(accountNumber))
                .next();
        return active;

    }

    @Override
    public Mono<Active> save(Active dataActive) {
        Mono<Active> activeMono= findByAccountNumber(dataActive.getAccountNumber())
                .flatMap(__ -> Mono.<Active>error(new Error("La cuenta bancaria con numero " + dataActive.getAccountNumber() + " YA EXISTE")))
                .switchIfEmpty(activeRepository.save(dataActive));
        return activeMono;

    }



    @Override
    public Mono<Active> update(Active dataActive) {
        Mono<Active> activeMono = findByAccountNumber(dataActive.getAccountNumber());
        //.delayElement(Duration.ofMillis(1000));
        try {
            dataActive.setDni(activeMono.block().getDni());
            dataActive.setStaff(activeMono.block().getStaff());
            dataActive.setBusiness(activeMono.block().getBusiness());
            dataActive.setBusinessCreditCard(activeMono.block().getBusinessCreditCard());
            dataActive.setPersonalCreditCard(activeMono.block().getPersonalCreditCard());
            dataActive.setCreationDate(activeMono.block().getCreationDate());
            return activeRepository.save(dataActive);
        }catch (Exception e){
            return Mono.<Active>error(new Error("La cuenta activa con numero " + dataActive.getAccountNumber() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Void> delete(String accountNumber) {
        Mono<Active> activeMono = findByAccountNumber(accountNumber);
        try{
            Active active = activeMono.block();
            return activeRepository.delete(active);
        }catch (Exception e){
            return Mono.<Void>error(new Error("La cuenta activa con numero \" + dataActive.getAccountNumber() + \" NO EXISTE"));
        }

    }



}
