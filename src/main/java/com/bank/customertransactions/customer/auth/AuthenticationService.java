package com.bank.customertransactions.customer.auth;


import com.bank.customertransactions.customer.entities.Customer;
import com.bank.customertransactions.customer.repo.UsersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final UsersRepo usersRepo;

    public AuthenticationService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Customer createUser(Customer user) {
        try {
            String encryptedPassword = new BCryptPasswordEncoder(10).encode(user.getPassword());
            user.setPassword(encryptedPassword);
//            user.setActive(true);
            LOGGER.info("User created: {}",user);
            return usersRepo.save(user);
        }catch (Exception e){
            LOGGER.error("An error occurred: {}", (Object) e.getStackTrace());
            return null;
        }

    }


}
