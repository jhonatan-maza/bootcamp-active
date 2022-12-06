package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.ActiveBusiness;
import com.nttdata.bootcamp.entity.ActiveCreditCard;
import com.nttdata.bootcamp.entity.ActiveStaff;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;
import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/active")
public class ActiveController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActiveController.class);
	@Autowired
	private ActiveService activeService;

	//Actives staff search
	@GetMapping("/findAllStaff")
	public Flux<ActiveStaff> findAllStaff() {
		Flux<ActiveStaff> actives = activeService.findAllStaff();
		LOGGER.info("Registered Actives Staff Products: " + actives);
		return actives;
	}

	//Actives staff search by customer
	@GetMapping("/findByCustomerStaff/{dni}")
	public Flux<ActiveStaff> findByCustomerStaff(@PathVariable("dni") String dni) {
		Flux<ActiveStaff> actives = activeService.findByCustomerStaff(dni);
		LOGGER.info("Registered Actives Staff Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	//Search for active staff by AccountNumber
	@GetMapping("/findByAccountNumberStaff/{accountNumber}")
	public Mono<ActiveStaff> findByAccountNumberStaff(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active Staff product by accountNumber: " + accountNumber);
		return activeService.findByAccountNumberStaff(accountNumber);
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

		Mono<ActiveStaff> activeMono = activeService.saveStaff(dataActiveStaff);
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

		Mono<ActiveStaff> updateActive = activeService.updateStaff(dataActiveStaff);
		return updateActive;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	//Delete active staff
	@DeleteMapping("/deleteStaff/{accountNumber}")
	public Mono<Void> deleteStaff(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeService.deleteStaff(accountNumber);
		return deleteActive;
	}
	//search all active business account
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetStaff")
	@GetMapping("/findAllBusiness")
	public Flux<ActiveBusiness> findAllBusiness() {
		Flux<ActiveBusiness> actives = activeService.findAllBusiness();
		LOGGER.info("Registered Actives business Products: " + actives);
		return actives;
	}

	//Actives business search by customer

	@GetMapping("/findByCustomerBusiness/{dni}")
	public Flux<ActiveBusiness> findByCustomerBusiness(@PathVariable("dni") String dni) {
		Flux<ActiveBusiness> actives = activeService.findByCustomerBusiness(dni);
		LOGGER.info("Registered Actives business Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}

	//Search business for active by AccountNumber
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@GetMapping("/findByAccountNumberBusiness/{accountNumber}")
	public Mono<ActiveBusiness> findByAccountNumberBusiness(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active Business product by accountNumber: " + accountNumber);
		return activeService.findByAccountNumberBusiness(accountNumber);
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

		Mono<ActiveBusiness> activeMono = activeService.saveBusiness(dataActiveBusiness);
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

		Mono<ActiveBusiness> updateActive = activeService.updateBusiness(dataActiveBusiness);
		return updateActive;
	}

	//Delete active business
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetBusiness")
	@DeleteMapping("/deleteBusiness/{accountNumber}")
	public Mono<Void> deleteBusiness(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active business by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeService.deleteBusiness(accountNumber);
		return deleteActive;
	}

	//search all credit card
	@GetMapping("/findAllCreditCard")
	public Flux<ActiveCreditCard> findAllCreditCard() {
		Flux<ActiveCreditCard> actives = activeService.findAllCreditCard();
		LOGGER.info("Registered Actives credit card Products: " + actives);
		return actives;
	}

	//Actives credit card search by customer

	@GetMapping("/findByCustomerCreditCard/{dni}")
	public Flux<ActiveCreditCard> findByCustomerCreditCard(@PathVariable("dni") String dni) {
		Flux<ActiveCreditCard> actives = activeService.findByCustomerCreditCard(dni);
		LOGGER.info("Registered Actives credit card Products by customer of dni: "+dni +"-" + actives);
		return actives;
	}
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	//Search for active credit card by AccountNumber
	@GetMapping("/findByAccountNumberCreditCard/{accountNumber}")
	public Mono<ActiveCreditCard> findByAccountNumberCreditCard(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Searching active credit card product by accountNumber: " + accountNumber);
		return activeService.findByAccountNumberCreditCard(accountNumber);
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

		Mono<ActiveCreditCard> activeMono = activeService.saveCreditCard(dataActiveBusiness);
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

		Mono<ActiveCreditCard> updateActive = activeService.updateCreditCard(dataActiveCreditCard);
		return updateActive;
	}

	//Delete active credit card
	@CircuitBreaker(name = "active", fallbackMethod = "fallBackGetCreditCard")
	@DeleteMapping("/deleteCreditCard/{accountNumber}")
	public Mono<Void> deleteCreditCard(@PathVariable("accountNumber") String accountNumber) {
		LOGGER.info("Deleting active by accountNumber: " + accountNumber);
		Mono<Void> deleteActive = activeService.deleteCreditCard(accountNumber);
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
	//get balance of a business product
	@GetMapping("/balanceBusiness/{accountNumber}")
	//get balance of an Active Product
	public Mono<Double> balanceBusiness(@PathVariable("accountNumber") String accountNumber){
		Mono<ActiveBusiness> activeMono= findByAccountNumberBusiness(accountNumber);
		LOGGER.info("Balance of an active business product by AccountNumber: " + accountNumber);
		Double balance= activeMono.block().getBalance();
		Mono<Double> doubleMono = Mono.just(balance);
		return doubleMono;

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

	@GetMapping("/countOfActiveStaffByCustomer/{dni}")
	//get balance of an Active Product
	public Mono<Long> countOfActiveStaffByCustomer(@PathVariable("dni") String dni){
		Flux<ActiveStaff> actives= findByCustomerStaff(dni);
		Mono<Long> count= actives.count();
		LOGGER.info("The customer "+dni+ " have "+count+" active staff " );
		return count;

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
	private Mono<ActiveStaff> fallBackGetStaff(Exception e){
		ActiveStaff activeStaff= new ActiveStaff();
		Mono<ActiveStaff> staffMono= Mono.just(activeStaff);
		return staffMono;
	}
	private Mono<ActiveBusiness> fallBackGetBusiness(Exception e){
		ActiveBusiness activeStaff= new ActiveBusiness();
		Mono<ActiveBusiness> staffMono= Mono.just(activeStaff);
		return staffMono;
	}
	private Mono<ActiveCreditCard> fallBackGetCreditCard(Exception e){
		ActiveCreditCard activeStaff= new ActiveCreditCard();
		Mono<ActiveCreditCard> staffMono= Mono.just(activeStaff);
		return staffMono;
	}

}
