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
        return activeRepository.save(dataActive);
    }

    @Override
    public Mono<Active> update(Active dataActive) {
        Mono<Active> activeMono = findByAccountNumber(dataActive.getAccountNumber());
        Active active = activeMono.block();
        active.setStatus(dataActive.getStatus());
        return activeRepository.save(active);
    }

    @Override
    public Mono<Void> delete(String accountNumber) {
        Mono<Active> activeMono = findByAccountNumber(accountNumber);
        Active active = activeMono.block();
        return activeRepository.delete(active);
    }

}
