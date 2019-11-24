package com.example.account.service;

import static com.example.exceptions.ErrorMessage.NUMBER_FORMAT_MISMATCH;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.account.validator.Validator;
import com.example.dao.TransactionDAO;
import com.example.entities.AccountEntity;
import com.example.entities.TransactionEntity;
import com.example.exceptions.ErrorCode;
import com.example.exceptions.ErrorMessage;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;

@Validated
@PropertySource("classpath:application.properties")
@Service
public class TransactionServiceImpl implements TransactionService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private AccountService walletService;

	@Autowired
	private Validator validator;

	@Value("${db.updated_by}")
	private String updatedBy;

	@Transactional(rollbackFor = WalletException.class)
	@Override
	public List<TransactionEntity> getTransactionsByEmailId(@NotNull String emailId) throws WalletException {
		List<AccountModel> accountModelList = walletService.findByEmailId(emailId);
		List<TransactionEntity> transactionEntities = new ArrayList();
		if (accountModelList != null) {
			for (Iterator iterator = accountModelList.iterator(); iterator.hasNext();) {
				AccountModel accountModel = (AccountModel) iterator.next();
				AccountEntity accountEntity = new AccountEntity();
				accountEntity.setEmailId(accountModel.getEmailId());
				accountEntity.setPassword(accountModel.getPassword());
				accountEntity.setName(accountModel.getName());
				accountEntity.setMobile(accountModel.getMobile());
				accountEntity.setAddress(accountModel.getAddress());
				accountEntity.setBalance(new BigDecimal(0));
				accountEntity.setLastUpdatedBy(updatedBy);
				transactionEntities.addAll(transactionDAO.findByAccountEntity(accountEntity));
			}
			return transactionEntities;
		} else {
			throw new WalletException(String.format(ErrorMessage.NO_WALLET_FOUND, emailId),
					ErrorCode.BadRequest.getCode());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
	@Override
	public AccountModel createTransaction(@NotBlank String globalId, @NotBlank String emailId, @NotBlank String amount,
			String description, @NotNull Boolean isCredit) throws WalletException {
		try {
			// Check for unique transaction globalId happens due to entity constrains on
			// Transaction.globalId (unique=true)
			AccountEntity accountEntity = new AccountEntity();

			AccountModel accountModel = walletService.findByEmailId(emailId).get(0);
			String error = String.format(ErrorMessage.NO_WALLET_FOUND, emailId);
			validator.isTrue((accountModel != null), error, ErrorCode.BadRequest.getCode());

			if (accountModel != null) {
				accountEntity.setId(accountModel.getId());
				accountEntity.setEmailId(accountModel.getEmailId());
				accountEntity.setPassword(accountModel.getPassword());
				accountEntity.setName(accountModel.getName());
				accountEntity.setMobile(accountModel.getMobile());
				accountEntity.setAddress(accountModel.getAddress());
				accountEntity.setBalance(accountModel.getBalance());
				accountEntity.setLastUpdatedBy(updatedBy);
			}
			// Update account, checks if there is enough funds for debit transaction. If
			// not,
			// throws WalletException
			BigDecimal currentBalance = walletService.updateWalletAmount(accountEntity, amount, isCredit);

			BigDecimal transactionAmount = (isCredit) ? new BigDecimal(amount).abs()
					: new BigDecimal(amount).abs().negate();
			// Create transaction
			TransactionEntity transaction = new TransactionEntity(globalId, transactionAmount.toString(),
					currentBalance, accountEntity, description, updatedBy);
			transactionDAO.save(transaction);
			accountModel = getAccountModel(transaction);
			return accountModel;
		} catch (NumberFormatException e) {
			throw new WalletException(String.format(NUMBER_FORMAT_MISMATCH, amount), ErrorCode.BadRequest.getCode());
		}
	}

	private AccountModel getAccountModel(TransactionEntity transaction) {
		AccountEntity accountEntity;
		AccountModel accountModel;
		accountEntity = transaction.getAccountEntity();
		accountModel = new AccountModel();
		accountModel.setId(accountEntity.getId());
		accountModel.setEmailId(accountEntity.getEmailId());
		accountModel.setBalance(accountEntity.getBalance());
		accountModel.setAddress(accountEntity.getAddress());
		accountModel.setMobile(accountEntity.getMobile());
		accountModel.setName(accountEntity.getName());
		accountModel.setPassword(accountEntity.getPassword());
		accountModel.setLastUpdated(accountEntity.getLastUpdated());
		accountModel.setLastUpdatedBy(accountEntity.getLastUpdatedBy());
		return accountModel;
	}
}
