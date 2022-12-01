package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.ActiveCreditCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//Mongodb Repository
public interface ActiveCreditCardRepository extends ReactiveCrudRepository<ActiveCreditCard, String> {
}
