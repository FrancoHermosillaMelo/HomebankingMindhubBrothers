package com.mindhub.homebanking.DTOS;

import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {
        private long id;
        private TransactionType type;
        private double amount;
        private String description;
        private LocalDateTime date;
        private Boolean activeTransaction;
        private double balanceTotal;

        public TransactionDTO(Transaction transaction) {
            this.id = transaction.getId();
            this.type = transaction.getType();
            this.amount = transaction.getAmount();
            this.description = transaction.getDescription();
            this.date = transaction.getDate();
            this.activeTransaction = transaction.getActiveTransaction();
            this.balanceTotal = transaction.getBalanceTotal();
        }

        public long getId() {
            return id;
        }

        public TransactionType getType() {
            return type;
        }

        public double getAmount() {
            return amount;
        }

        public String getDescription() {
            return description;
        }

        public LocalDateTime getDate() {
            return date;
        }

         public Boolean getActiveTransaction() {
        return activeTransaction;
    }

        public double getBalanceTotal() {
        return balanceTotal;
          }
};
