package com.bank.customertransactions.transactions.queue;

import com.bank.customertransactions.transactions.entities.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class CashDepositListener implements MessageListener {
    static final Logger LOGGER = LoggerFactory.getLogger(CashDepositListener.class);
    @Override
    @JmsListener(destination ="${active-mq.cash-deposit}" )
    public void onMessage(Message message) {
        try{
            LOGGER.info("Received deposit notification");
            TextMessage textMessage = (TextMessage) message;
            String msg = textMessage.getText();
            LOGGER.info("Received withdrawal notification: {}", msg);
            ObjectMapper mapper = new ObjectMapper();
            Transaction transaction = mapper.readValue(msg, Transaction.class);
            LOGGER.info("Received Message: {}", transaction.toString());
        } catch(Exception e) {
            LOGGER.error("Received Exception : {}", e.getMessage());
        }
    }
}
