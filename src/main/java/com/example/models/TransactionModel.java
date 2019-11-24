package com.example.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  Transaction entity.
 *
 */
public class TransactionModel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7784395616005775246L;
	private Integer id;
    private String globalId;
    private String transactionAmount;
    private BigDecimal currentBalance;
    private String description;
    private Date lastUpdated;
    private String lastUpdatedBy;

    public TransactionModel(){ }

    public TransactionModel( String globalId, String transactionAmount, BigDecimal currentBalance, String description) {
        this.globalId = globalId;
        this.transactionAmount = transactionAmount;
        this.currentBalance = currentBalance;
        this.description = description;
        this.lastUpdated = new Date();
    }

    public TransactionModel( String globalId, String transactionAmount, BigDecimal currentBalance, String description, String lastUpdatedBy) {
       this(globalId,transactionAmount,currentBalance,description);
       this.lastUpdatedBy = lastUpdatedBy;
    }
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
