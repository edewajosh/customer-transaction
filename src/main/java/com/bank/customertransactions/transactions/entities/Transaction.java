package com.bank.customertransactions.transactions.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    String accountId;
    double amount;
    String description;
    String transactionType;
    String debitAccount;
    String creditAccount;
    String transactionReference;
    String transactionCurrency;
    LocalDateTime transactionDate;
    String statusCode;
    String statusMessage;

}
