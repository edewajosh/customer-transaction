package com.bank.customertransactions.customer.services;

import com.bank.customertransactions.customer.entities.Customer;
import com.bank.customertransactions.customer.repo.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UsersRepo useRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(UsersRepo useRepo, PasswordEncoder passwordEncoder) {
        this.useRepo = useRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public Customer createCustomer(Customer customer) {
        LOGGER.info("Registering customer: {} ",customer.toString());
        Customer newCustomer = new Customer();

        newCustomer.setFirstName(customer.getFirstName());
        newCustomer.setLastName(customer.getLastName());
        newCustomer.setEmail(customer.getEmail());
        newCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        newCustomer.setActive(true); // This could be enabled after email verification
        newCustomer.setBalance(customer.getBalance());
        newCustomer.setAccountNumber(customer.getAccountNumber());
        newCustomer.setAccountStatus(customer.getAccountStatus());
        newCustomer.setAccountType(customer.getAccountType());
        useRepo.save(newCustomer);
        newCustomer.setPassword(null);
        return newCustomer;
    }

    public Customer updateCustomerProfile(Customer customer) {
        LOGGER.info("Updating customer: {} ",customer.toString());
        Customer cust = (Customer) useRepo.findByEmail(customer.getEmail());

        cust.setFirstName(customer.getFirstName());
        cust.setLastName(customer.getLastName());
        cust.setEmail(customer.getEmail());
        cust.setPassword(passwordEncoder.encode(customer.getPassword()));
        useRepo.save(cust);
        return cust;
    }

    public Optional<Customer> findByCustomerId(Long customerId) {
        return useRepo.findById(customerId);
    }

    public Optional<Customer> findByCustomerByAccount(String accountNumber) {
        return useRepo.findByAccountNumber(accountNumber);
    }

    public ArrayList<Customer> getAllCustomers() {
        return (ArrayList<Customer>) useRepo.findAll();
    }
}
