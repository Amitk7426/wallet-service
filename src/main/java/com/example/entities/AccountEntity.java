package com.example.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * account entity.
 *
 */
@Entity
@Table(name = "account")
public class AccountEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@NotNull(message = "emailId must be provided")
	@Column(name = "emailId")
	private String emailId;

	@NotNull(message = "password must be provided")
	@Column(name = "password")
	private String password;

	@NotNull(message = "name must be provided")
	@Column(name = "name")
	private String name;

	@NotNull(message = "mobile must be provided")
	@Column(name = "mobile")
	private String mobile;

	@Column(name = "address")
	private String address;

	@Min(0)
	@Column(name = "balance", nullable = false)
	@NotNull(message = "Wallet balance must be provided")
	private BigDecimal balance;

	@Column(name = "last_updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;

	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

	@OneToMany(mappedBy = "accountEntity", fetch = FetchType.LAZY)
	private List<TransactionEntity> transactions;

	public AccountEntity() {
	}

	public AccountEntity(String emailId, String password, String name, String mobile, String address,
			BigDecimal balance) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.name = name;
		this.mobile = mobile;
		this.address = address;
		this.balance = balance;
	}

	public AccountEntity(String emailId, String password, String name, String mobile, String address,
			BigDecimal balance, Date lastUpdated, String lastUpdatedBy) {
		this(emailId, password, name, mobile, address, balance);
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
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

	public List<TransactionEntity> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionEntity> transactions) {
		this.transactions = transactions;
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
