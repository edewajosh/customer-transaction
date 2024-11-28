package com.bank.customertransactions.customer.repo;

import com.bank.customertransactions.customer.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Customer, Long> {
    UserDetails findByEmail(String s);

    Optional<Customer> findByAccountNumber(String accountNumber);
}
