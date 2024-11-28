package com.bank.customertransactions.customer.controllers;

import com.bank.customertransactions.customer.entities.Customer;
import com.bank.customertransactions.customer.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customer/")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("update")
    public ResponseEntity updateCustomerProfile(@RequestBody Customer customer) {
        logger.info("Request from: {}",customer.toString());
        Customer res = customerService.updateCustomerProfile(customer);
        logger.info("Response: {}",res.getId());
        if(res.getId() != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    @GetMapping("{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
        logger.info("Find customer request from: {}",id);
        Optional<Customer> res = customerService.findByCustomerId(id);
        return res.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @GetMapping("account/{accountNumber}")
    public ResponseEntity<Customer> getCustomerByAccount(@PathVariable("accountNumber") String accountNumber) {
        logger.info("Find customer request from: {}",accountNumber);
        Optional<Customer> res = customerService.findByCustomerByAccount(accountNumber);
        return res.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }
    @GetMapping
    public ResponseEntity<ArrayList<Customer>> getAllCustomers() {
        logger.info("Find all customers request from: {}",customerService.toString());
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }
}
