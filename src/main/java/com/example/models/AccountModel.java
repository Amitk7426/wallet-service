package com.example.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Account entity.
 *
 */
public class AccountModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -551727855895657382L;
	private Integer id;
	private String emailId;
	private String password;
	private String name;
	private String mobile;
	private String address;
	private BigDecimal balance;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private List<TransactionModel> transactionModel;
	public AccountModel() {
	}

	public AccountModel(String emailId, String password, String name, String mobile, String address,
			BigDecimal balance) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.name = name;
		this.mobile = mobile;
		this.address = address;
		this.balance = balance;
	}

	public AccountModel(String emailId, String password, String name, String mobile, String address,
			BigDecimal balance, Date lastUpdated, String lastUpdatedBy) {
		this(emailId, password, name, mobile, address, balance);
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public List<TransactionModel> getTransactionModel() {
		return transactionModel;
	}

	public void setTransactionModel(List<TransactionModel> transactionModel) {
		this.transactionModel = transactionModel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
