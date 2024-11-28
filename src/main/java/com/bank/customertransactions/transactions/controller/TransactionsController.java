package com.bank.customertransactions.transactions.controller;

import com.bank.customertransactions.transactions.entities.Transaction;
import com.bank.customertransactions.transactions.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionsController {
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);
    final TransactionService transactionService;
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> cashDeposit(@RequestBody Transaction transaction) throws JsonProcessingException {
        LOGGER.info("Cash deposit transactions: {}", transaction.toString());
        Transaction newTransaction = transactionService.cashDeposit(transaction);
        if (newTransaction != null) {
            return new ResponseEntity<>(newTransaction, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
