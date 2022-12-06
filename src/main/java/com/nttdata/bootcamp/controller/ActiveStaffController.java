package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.service.ActiveStaffService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.ActiveBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/staff")
public class ActiveStaffController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveStaffController.class);
	@Autowired
	private ActiveStaffService activeStaffService;

	//Actives staff search
	@GetMapping("/findAllStaff")
	public Flux<ActiveStaff> findAllStaff() {
		Flux<ActiveStaff> actives = activeStaffService.findAllStaff();
		LOGGER.info("Registered Actives Staff Products: " + actives);
		return actives;
	}

	//Actives staff search by customer
	@GetMapping("/findByCustomerStaff/{dni}")
	public Flux<ActiveStaff> findByCustomerStaff(@PathVariable("dni") String dni) {
		Flux<ActiveStaff> actives = activeStaffService.findByCustomerStaff(dni);
		LOGGER.info("Registered Actives Staff Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	//Search for active staff by AccountNumber
	@GetMapping("/findByAccountNumberStaff/{accountNumber}")
	public Mono<ActiveStaff> findByAccountNumberStaff(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active Staff product by accountNumber: " + accountNumber);
		return activeStaffService.findByAccountNumberStaff(accountNumber);
	}

	//Save active staff
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	@PostMapping(value = "/saveStaff")
	public Mono<ActiveStaff> saveStaff(@RequestBody ActiveStaff dataActiveStaff){
		Mono.just(dataActiveStaff).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveStaff).onErrorResume(e -> Mono.just(dataActiveStaff))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveStaff> activeMono = activeStaffService.saveStaff(dataActiveStaff);
		return activeMono;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	//Update active staff
	@PutMapping("/updateStaff/{accountNumber}")
	public Mono<ActiveStaff> updateStaff(@PathVariable("accountNumber") String accountNumber,
                                          @Valid @RequestBody ActiveStaff dataActiveStaff) {
		Mono.just(dataActiveStaff).doOnNext(t -> {

					t.setAccountNumber(accountNumber);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveStaff).onErrorResume(e -> Mono.just(dataActiveStaff))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveStaff> updateActive = activeStaffService.updateStaff(dataActiveStaff);
		return updateActive;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	//Delete active staff
	@DeleteMapping("/deleteStaff/{accountNumber}")
	public Mono<Void> deleteStaff(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeStaffService.deleteStaff(accountNumber);
		return deleteActive;
	}

	//get balance of a business product
	@GetMapping("/balanceStaff/{accountNumber}")
	//get balance of an Active Product
	public Mono<Double> balanceStaff(@PathVariable("accountNumber") String accountNumber){
		Mono<ActiveStaff> activeMono= findByAccountNumberStaff(accountNumber);
		LOGGER.info("Balance of an active business product by AccountNumber: " + accountNumber);
		Double balance= activeMono.block().getBalance();
		Mono<Double> doubleMono = Mono.just(balance);
		return doubleMono;

	}


	@GetMapping("/countOfActiveStaffByCustomer/{dni}")
	//get balance of an Active Product
	public Mono<Long> countOfActiveStaffByCustomer(@PathVariable("dni") String dni){
		Flux<ActiveStaff> actives= findByCustomerStaff(dni);
		Mono<Long> count= actives.count();
		LOGGER.info("The customer "+dni+ " have "+count+" active staff " );
		return count;

	}

	//circuit breaker
	private Mono<ActiveStaff> fallBackGetStaff(Exception e){
		ActiveStaff activeStaff= new ActiveStaff();
		Mono<ActiveStaff> staffMono= Mono.just(activeStaff);
		return staffMono;
	}


}
