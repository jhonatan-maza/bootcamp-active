package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.service.ActiveBusinessService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/business")
public class ActiveBusinessController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveBusinessController.class);
	@Autowired
	private ActiveBusinessService activeBusinessService;


	//search all active business account
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	@GetMapping("/findAllBusiness")
	public Flux<ActiveBusiness> findAllBusiness() {
		Flux<ActiveBusiness> actives = activeBusinessService.findAllBusiness();
		LOGGER.info("Registered Actives business Products: " + actives);
		return actives;
	}

	//Actives business search by customer

	@GetMapping("/findByCustomerBusiness/{dni}")
	public Flux<ActiveBusiness> findByCustomerBusiness(@PathVariable("dni") String dni) {
		Flux<ActiveBusiness> actives = activeBusinessService.findByCustomerBusiness(dni);
		LOGGER.info("Registered Actives business Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}

	//Search business for active by AccountNumber
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@GetMapping("/findByAccountNumberBusiness/{accountNumber}")
	public Mono<ActiveBusiness> findByAccountNumberBusiness(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active Business product by accountNumber: " + accountNumber);
		return activeBusinessService.findByAccountNumberBusiness(accountNumber);
	}

	//Save active business
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@PostMapping(value = "/saveBusiness")
	public Mono<ActiveBusiness> saveBusiness(@RequestBody ActiveBusiness dataActiveBusiness){
		Mono.just(dataActiveBusiness).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveBusiness).onErrorResume(e -> Mono.just(dataActiveBusiness))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveBusiness> activeMono = activeBusinessService.saveBusiness(dataActiveBusiness);
		return activeMono;
	}

	//Update active business
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@PutMapping("/updateBusiness/{accountNumber}")
	public Mono<ActiveBusiness> updateBusiness(@PathVariable("accountNumber") String accountNumber,
											   @Valid @RequestBody ActiveBusiness dataActiveBusiness) {
		Mono.just(dataActiveBusiness).doOnNext(t -> {

					t.setAccountNumber(accountNumber);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveBusiness).onErrorResume(e -> Mono.just(dataActiveBusiness))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveBusiness> updateActive = activeBusinessService.updateBusiness(dataActiveBusiness);
		return updateActive;
	}

	//Delete active business
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@DeleteMapping("/deleteBusiness/{accountNumber}")
	public Mono<Void> deleteBusiness(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active business by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeBusinessService.deleteBusiness(accountNumber);
		return deleteActive;
	}

	//get balance of a credit card product
	@GetMapping("/balanceBusiness/{accountNumber}")
	//get balance of an Active Product
	public Mono<Double> balanceBusiness(@PathVariable("accountNumber") String accountNumber){
		Mono<ActiveBusiness> activeMono= findByAccountNumberBusiness(accountNumber);
		LOGGER.info("Balance of an active business product by AccountNumber: " + accountNumber);
		Double balance= activeMono.block().getBalance();
		Mono<Double> doubleMono = Mono.just(balance);
		return doubleMono;

	}

	@GetMapping("/countOfActiveBusinessByCustomer/{dni}")
	//get balance of an Active Product
	public Mono<Long> countOfActiveBusinessByCustomer(@PathVariable("dni") String dni){
		Flux<ActiveBusiness> actives= findByCustomerBusiness(dni);
		Mono<Long> count= actives.count();
		LOGGER.info("The customer "+dni+ " have "+count+" active business " );
		return count;

	}

	//circuit breaker

	private Mono<ActiveBusiness> fallBackGetBusiness(Exception e){
		ActiveBusiness activeStaff= new ActiveBusiness();
		Mono<ActiveBusiness> staffMono= Mono.just(activeStaff);
		return staffMono;
	}


}
