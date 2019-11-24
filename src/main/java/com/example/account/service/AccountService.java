package com.example.account.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.example.entities.AccountEntity;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;
import com.example.models.TransactionModel;

/**
 * Service for managing wallets
 */
public interface AccountService {
    public List<AccountEntity> findAll() throws WalletException;
    public List<AccountModel> findByEmailId(@NotBlank String emailId) throws WalletException;
    public boolean createWallet(@NotNull  AccountModel accountModel) throws WalletException;
    public BigDecimal updateWalletAmount(@NotNull AccountEntity wallet,@NotBlank String amount,@NotNull Boolean isCredit) throws WalletException;
	public void updateWallet(AccountModel accountModel) throws WalletException;
	public List<TransactionModel> getTransactions(String emailId) throws WalletException;
	public List<TransactionModel> getTransactions(String emailId, String fromDate, String toDate) throws WalletException, ParseException;

}