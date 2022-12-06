package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.repository.ActiveCreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Service implementation
@Service
public class ActiveCreditCardServiceImpl implements ActiveCreditCardService {

    @Autowired
    private ActiveCreditCardRepository activeCreditCardRepository;


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
        Mono<ActiveCreditCard> activeMono= findByAccountNumberCreditCard(dataActiveCreditCard.getAccountNumber())
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
