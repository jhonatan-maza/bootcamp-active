package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//Mongodb Repository
public interface ActiveBusinessRepository extends ReactiveCrudRepository<ActiveBusiness, String> {
}
