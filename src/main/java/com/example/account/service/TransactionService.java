package com.example.account.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.example.entities.TransactionEntity;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;

/**
 * Service for managing transactions
 */
public interface TransactionService {
    public List<TransactionEntity> getTransactionsByEmailId(@NotNull String emailId) throws WalletException;
    public AccountModel createTransaction(@NotBlank String globalId, @NotBlank String emailId, @NotBlank String amount, String description, @NotNull Boolean isCredit) throws WalletException;

}
