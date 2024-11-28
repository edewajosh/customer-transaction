package com.bank.customertransactions.transactions.activemq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "active-mq")
public record ActiveMq(String fundsTransfer,String cashDeposit,String withdrawals) {
}
