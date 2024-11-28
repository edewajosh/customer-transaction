package com.bank.customertransactions.transactions.services;

import com.bank.customertransactions.customer.auth.TokenBody;
import com.bank.customertransactions.transactions.activemq.ActiveMq;
import com.bank.customertransactions.transactions.dto.AccountDto;
import com.bank.customertransactions.transactions.entities.Transaction;
import com.bank.customertransactions.transactions.repo.TransactionRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class TransactionService {
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);
    final TransactionRepo transactionRepo;
    final ObjectMapper objectMapper;
    final JmsTemplate jmsTemplate;
    final ActiveMq activeMq;

    public TransactionService(TransactionRepo transactionRepo, ObjectMapper objectMapper, JmsTemplate jmsTemplate, ActiveMq activeMq) {
        this.transactionRepo = transactionRepo;
        this.objectMapper = objectMapper;
        this.jmsTemplate = jmsTemplate;
        this.activeMq = activeMq;
    }

    public Transaction cashDeposit(Transaction transaction) throws JsonProcessingException {
        LOGGER.info("cash deposit called");
        //Check if account exists
        AccountDto accountDetails = fetchAccountDetails(transaction.getCreditAccount());
        LOGGER.info("Account name: {}-> A/C balance: {} {}", accountDetails.customerId(), accountDetails.currencyCode(),accountDetails.balance());
        if(!accountDetails.currencyCode().equals(transaction.getTransactionCurrency())) {
            LOGGER.info("Account currency code is different");
            transaction.setStatusCode("4");
            transaction.setStatusMessage("Transaction failed: CURRENCY MISMATCH");
            transaction.setTransactionDate(LocalDateTime.now());
        }else if(!Objects.equals(accountDetails.accountNumber(), transaction.getCreditAccount())){
            LOGGER.info("account number mismatch");
            transaction.setStatusCode("5");
            transaction.setStatusMessage("Transaction failed: ACCOUNT NOT FOUND");
            transaction.setTransactionDate(LocalDateTime.now());
        }else {
            LOGGER.info("Processing cash deposit");
            transaction.setTransactionReference("TT" + generateRandomString(4));
            transaction = transactionRepo.save(transaction);
            double balance = accountDetails.balance() + transaction.getAmount();

            AccountDto updatedAccount = new AccountDto(
                    null, accountDetails.accountNumber(),accountDetails.accountType(),
                    balance,accountDetails.accountStatus(),accountDetails.currencyCode()
            );
            //TODO: construct request for account details to be updated
            transaction.setStatusMessage("Transaction completed successfully");
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setStatusCode("0");

            LOGGER.info("Transaction completed successfully");
            updateAccountDetails(updatedAccount);
        }
        transaction = transactionRepo.save(transaction);
        LOGGER.info("End of cash deposit called");
        // send  notification to ACTIVEMQ
        String message = objectMapper.writeValueAsString(transaction);
        sendMessage(message, activeMq.cashDeposit());
        return transaction;
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        String s = localDateTime.toString().substring(0, 7).replace("-","");
        return s+result.toString().toUpperCase();
    }

    AccountDto fetchAccountDetails(String accountNumber) throws JsonProcessingException {
//        TokenBody body = getAuthToken();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", body.bearer_token());
        String response = accountRestClient()
                .get()
//                .headers((Consumer<HttpHeaders>) headers)
                .uri("/api/v1/customer/account/"+accountNumber)
                .retrieve().body(String.class);
        LOGGER.info("Account response: {}", response);
        AccountDto accountDto = objectMapper.readValue(response, AccountDto.class);
        LOGGER.info("Account map: {}", accountDto);
        return accountDto;
    }

    TokenBody getAuthToken() throws JsonProcessingException {
        String password = "johndoe@test.com";
        String username =  "test@123456";
        String loginCredentials = password + ":" + username;
        String encodedString = Base64.getEncoder().encodeToString(loginCredentials.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedString);
        RestClient client = RestClient.builder().baseUrl("http://localhost:8080/api/v1/auth/login").build();
        String token = client.get().headers((Consumer<HttpHeaders>)headers).retrieve().body(String.class);
        LOGGER.info("Token string: {}", token);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(token, TokenBody.class);
    }
    void updateAccountDetails(AccountDto accountDto) throws JsonProcessingException {
        String res = accountRestClient()
                .put()
                .uri("/api/v1/customer/account/"+accountDto.accountNumber())
                .body(accountDto)
                .retrieve().body(String.class);
        LOGGER.info("Balance update: {}", res);
    }

    RestClient accountRestClient(){
        return RestClient.builder().baseUrl("http://localhost:8080").build();
    }
    public void sendMessage(String message, String topic) throws JsonProcessingException {
        try {
            LOGGER.info("Sending notification to active MQ-> topic: {}",topic);
            jmsTemplate.convertAndSend(topic,message);
        }catch (Exception e){
            LOGGER.error("Sending notification to active MQ failed: {}", e.getMessage());
        }
    }

}
