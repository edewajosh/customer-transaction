package com.bank.customertransactions.transactions.repo;

import com.bank.customertransactions.transactions.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
