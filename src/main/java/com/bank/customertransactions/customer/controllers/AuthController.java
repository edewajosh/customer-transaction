package com.bank.customertransactions.customer.controllers;

import com.bank.customertransactions.customer.auth.TokenBody;
import com.bank.customertransactions.customer.auth.TokenService;
import com.bank.customertransactions.customer.entities.Customer;
import com.bank.customertransactions.customer.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    final TokenService tokenService;
    final CustomerService customerService;

    public AuthController(TokenService tokenService, CustomerService customerService) {
        this.tokenService = tokenService;
        this.customerService = customerService;
    }

    @GetMapping("login")
    public ResponseEntity<TokenBody> login(Authentication authentication){
        Customer user = (Customer) authentication.getPrincipal();
        user.setActive(true);
        if(user.isActive()){
            TokenBody token = tokenService.generateToken(authentication);
            LOGGER.info("Token value: {} ", token.bearer_token());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("register")
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        LOGGER.info("Request from: {}",customer.toString());
        Customer res = customerService.createCustomer(customer);
        LOGGER.info("Response: {}",res.getId());
        if(res.getId() != null) {
            return new ResponseEntity<>(res,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
