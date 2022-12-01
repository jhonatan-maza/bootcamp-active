package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.ActiveStaff;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//Mongodb Repository
public interface ActiveStaffRepository extends ReactiveCrudRepository<ActiveStaff, String> {
}
