package com.app.bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@Table(name = "account_transactions")
public class AccountTransactions {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "account_number", nullable = false)
    private long accountNumber;

    @Column(name = "customer_id", nullable = false)
    private long customerId;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_summary")
    private String transactionSummary;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_amount")
    private int transactionAmount;

    @Column(name = "closing_balance")
    private int closingBalance;

    @Column(name = "created_at")
    private Date createdAt;
}
