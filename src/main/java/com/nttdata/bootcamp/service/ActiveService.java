package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.Active;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface ActiveService {

    public Flux<Active> findAll();
    public Mono<Active> findByDni(String dni);
    public Mono<Active> save(Active active);
    public Mono<Active> update(Active dataActive);
    public Mono<Void> delete(String dni);

}
