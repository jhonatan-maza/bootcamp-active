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
@RequestMapping(value = "/customer")
public class ActiveController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveController.class);
	@Autowired
	private ActiveService activeService;

	//Customer search
	@GetMapping("/")
	public Flux<Active> findAllCustomers() {
		Flux<Active> customers = activeService.findAll();
		LOGGER.info("Registered Customers: " + customers);
		return customers;
	}

	//Search for clients by DNI
	@GetMapping("/findByClient/{dni}")
	public Mono<Active> findByClientDNI(@PathVariable("dni") String dni) {
		LOGGER.info("Searching client by DNI: " + dni);
		return activeService.findByDni(dni);
	}

	//Save customer
	@PostMapping(value = "/save")
	public Mono<Active> saveCustomer(@RequestBody Active dataActive){
		Mono.just(dataActive).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActive).onErrorResume(e -> Mono.just(dataActive))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Active> newCustomer = activeService.save(dataActive);
		return newCustomer;
	}

	//Update customer
	@PutMapping("/update/{dni}")
	public ResponseEntity<Mono<?>> updateCustomer(@PathVariable("dni") String dni,
													   @Valid @RequestBody Active dataActive) {
		Mono.just(dataActive).doOnNext(t -> {
					dataActive.setDni(dni);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataActive).onErrorResume(e -> Mono.just(dataActive))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Active> pAsset = activeService.update(dataActive);

		if (pAsset != null) {
			return new ResponseEntity<>(pAsset, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(Mono.just(new Active()), HttpStatus.I_AM_A_TEAPOT);
	}

	//Delete customer
	@DeleteMapping("/delete/{dni}")
	public ResponseEntity<Mono<Void>> deleteCustomer(@PathVariable("dni") String dni) {
		LOGGER.info("Deleting client by DNI: " + dni);
		Mono<Void> delete = activeService.delete(dni);
		return ResponseEntity.noContent().build();
	}

}
