package com.nttdata.bootcamp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nttdata.bootcamp.entity.Active;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/active")
public class ActiveController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveController.class);
	@Autowired
	private ActiveService activeService;

	//Actives search
	@GetMapping("/")
	public Flux<Active> findAllActives() {
		Flux<Active> actives = activeService.findAll();
		LOGGER.info("Registered Actives Products: " + actives);
		return actives;
	}

	//Actives search by customer
	@GetMapping("/findAllActivesByCustomer/{dni}")
	public Flux<Active> findAllActivesByCustomer(@PathVariable("dni") String dni) {
		Flux<Active> actives = activeService.findByCustomer(dni);
		LOGGER.info("Registered Actives Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}

	//Search for active by AccountNumber
	@GetMapping("/findByAccountNumber/{accountNumber}")
	public Mono<Active> findByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active product by accountNumber: " + accountNumber);
		return activeService.findByAccountNumber(accountNumber);
	}

	//Save active
	@PostMapping(value = "/save")
	public Mono<Active> saveActive(@RequestBody Active dataActive){
		Mono.just(dataActive).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActive).onErrorResume(e -> Mono.just(dataActive))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Active> activeMono = activeService.save(dataActive);
		return activeMono;
	}

	//Update active
	@PutMapping("/update/{accountNumber}")
	public Mono<Active> updateActive(@PathVariable("accountNumber") String accountNumber,
													   @Valid @RequestBody Active dataActive) {
		Mono.just(dataActive).doOnNext(t -> {

					t.setAccountNumber(accountNumber);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActive).onErrorResume(e -> Mono.just(dataActive))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Active> updateActive = activeService.update(dataActive);
		return updateActive;
	}

	//Delete customer
	@DeleteMapping("/delete/{accountNumber}")
	public Mono<Void> deleteCustomer(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeService.delete(accountNumber);
		return deleteActive;
	}



	@GetMapping("/balanceOfActive/{accountNumber}")
	//get balance of an Active Product
	public Mono<Double> balanceOfActive(@PathVariable("accountNumber") String accountNumber){
		Mono<Active> activeMono= findByAccountNumber(accountNumber);
		LOGGER.info("Balance of an passive product by AccountNumber: " + accountNumber);
		Double balance= activeMono.block().getBalance();
		Mono<Double> doubleMono = Mono.just(balance);
		return doubleMono;


	}


}
