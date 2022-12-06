package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import com.nttdata.bootcamp.service.ActiveBusinessService;
import com.nttdata.bootcamp.service.ActiveCreditCardService;
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
@RequestMapping(value = "/creditCard")
public class ActiveCreditCardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveCreditCardController.class);
	@Autowired
	private ActiveCreditCardService activeCreditCardService;



	//search all credit card
	@GetMapping("/findAllCreditCard")
	public Flux<ActiveCreditCard> findAllCreditCard() {
		Flux<ActiveCreditCard> actives = activeCreditCardService.findAllCreditCard();
		LOGGER.info("Registered Actives credit card Products: " + actives);
		return actives;
	}

	//Actives credit card search by customer

	@GetMapping("/findByCustomerCreditCard/{dni}")
	public Flux<ActiveCreditCard> findByCustomerCreditCard(@PathVariable("dni") String dni) {
		Flux<ActiveCreditCard> actives = activeCreditCardService.findByCustomerCreditCard(dni);
		LOGGER.info("Registered Actives credit card Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	//Search for active credit card by AccountNumber
	@GetMapping("/findByAccountNumberCreditCard/{accountNumber}")
	public Mono<ActiveCreditCard> findByAccountNumberCreditCard(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active credit card product by accountNumber: " + accountNumber);
		return activeCreditCardService.findByAccountNumberCreditCard(accountNumber);
	}

	//Save active credit card
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	@PostMapping(value = "/saveCreditCard")
	public Mono<ActiveCreditCard> saveCreditCard(@RequestBody ActiveCreditCard dataActiveBusiness){
		Mono.just(dataActiveBusiness).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveBusiness).onErrorResume(e -> Mono.just(dataActiveBusiness))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveCreditCard> activeMono = activeCreditCardService.saveCreditCard(dataActiveBusiness);
		return activeMono;
	}

	//Update active credit card
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	@PutMapping("/updateCreditCard/{accountNumber}")
	public Mono<ActiveCreditCard> updateBusiness(@PathVariable("accountNumber") String accountNumber,
												 @Valid @RequestBody ActiveCreditCard dataActiveCreditCard) {
		Mono.just(dataActiveCreditCard).doOnNext(t -> {

					t.setAccountNumber(accountNumber);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActiveCreditCard).onErrorResume(e -> Mono.just(dataActiveCreditCard))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<ActiveCreditCard> updateActive = activeCreditCardService.updateCreditCard(dataActiveCreditCard);
		return updateActive;
	}

	//Delete active credit card
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	@DeleteMapping("/deleteCreditCard/{accountNumber}")
	public Mono<Void> deleteCreditCard(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeCreditCardService.deleteCreditCard(accountNumber);
		return deleteActive;
	}

	//get balance of a credit card product
	@GetMapping("/balanceCreditCard/{accountNumber}")
	//get balance of an Active Product
	public Mono<Double> balanceCreditCard(@PathVariable("accountNumber") String accountNumber){
		Mono<ActiveCreditCard> activeMono= findByAccountNumberCreditCard(accountNumber);
		LOGGER.info("Balance of an active business product by AccountNumber: " + accountNumber);
		Double balance= activeMono.block().getBalance();
		Mono<Double> doubleMono = Mono.just(balance);
		return doubleMono;

	}


	private Mono<ActiveCreditCard> fallBackGetCreditCard(Exception e){
		ActiveCreditCard activeStaff= new ActiveCreditCard();
		Mono<ActiveCreditCard> staffMono= Mono.just(activeStaff);
		return staffMono;
	}

}
