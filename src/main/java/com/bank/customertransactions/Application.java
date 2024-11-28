package com.bank.customertransactions;

import com.bank.customertransactions.transactions.activemq.ActiveMq;
import com.bank.customertransactions.customer.configs.RsaKeyProperties;
import com.bank.customertransactions.customer.entities.Customer;
import com.bank.customertransactions.customer.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class, ActiveMq.class})
public class Application {
    @Autowired
    UsersRepo usersRepo;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(ApplicationContext context) {
//
//        return args -> {
//            Customer customer = new Customer();
//            customer.setActive(true);
//            customer.setEmail(UUID.randomUUID() +"@test.com");
//            customer.setPassword(new BCryptPasswordEncoder(10).encode("test@123456"));
//            customer.setFirstName("John");
//            customer.setLastName("Doe");
//            customer.setCurrencyCode("KES");
//            customer.setAccountNumber("123445566");
//            customer.setAccountType("SALARY");
//            customer.setBalance(10000);
//            System.out.println("Customer: " + customer);
//
//            Customer customer1 = usersRepo.save(customer);
//            System.out.println("Customer created: " + customer1);
//        };
//    }
}
