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
        Flux<Active> customers = activeRepository.findAll();
        return customers;
    }

    @Override
    public Mono<Active> findByDni(String dni) {
        Mono<Active> customer = activeRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni))
                .next();
        return customer;
    }

    @Override
    public Mono<Active> save(Active dataActive) {
        return activeRepository.save(dataActive);
    }

    @Override
    public Mono<Active> update(Active dataActive) {
        Mono<Active> customerMono = findByDni(dataActive.getDni());
        Active active = customerMono.block();
        active.setStatus(dataActive.getStatus());
        return activeRepository.save(active);
    }

    @Override
    public Mono<Void> delete(String dni) {
        Mono<Active> customerMono = findByDni(dni);
        Active active = customerMono.block();
        return activeRepository.delete(active);
    }

}
