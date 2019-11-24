package com.example.account.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.account.validator.Validator;
import com.example.dao.AccountDAO;
import com.example.entities.AccountEntity;
import com.example.entities.TransactionEntity;
import com.example.exceptions.ErrorCode;
import com.example.exceptions.ErrorMessage;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;
import com.example.models.TransactionModel;

/**
 * Service for managing wallets
 */
@Validated
@PropertySource("classpath:application.properties")
@Service
class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private Validator validator;

	@Value("${db.updated_by}")
	private String updatedBy;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Transactional(rollbackFor = WalletException.class)
	@Override
	public List<AccountEntity> findAll() throws WalletException {
		return accountDAO.getAllAccounts();
	}

	@Transactional(rollbackFor = WalletException.class)
	@Override
	public List<AccountModel> findByEmailId(@NotBlank String emailId) throws WalletException {
		List<AccountEntity> accountEntityList = accountDAO.findByEmailId(emailId);
		List<AccountModel> accountModels = new ArrayList();
		for (Iterator iterator = accountEntityList.iterator(); iterator.hasNext();) {
			AccountEntity accountEntity = (AccountEntity) iterator.next();
			AccountModel accountModel = new AccountModel();
			accountModel.setId(accountEntity.getId());
			accountModel.setEmailId(accountEntity.getEmailId());
			accountModel.setBalance(accountEntity.getBalance());
			accountModel.setAddress(accountEntity.getAddress());
			accountModel.setMobile(accountEntity.getMobile());
			accountModel.setName(accountEntity.getName());
			accountModel.setPassword(accountEntity.getPassword());
			accountModel.setLastUpdated(accountEntity.getLastUpdated());
			accountModel.setLastUpdatedBy(accountEntity.getLastUpdatedBy());
			accountModel.setTransactionModel(getTransactionModel(accountEntity.getTransactions()));
			accountModels.add(accountModel);
		}
		return accountModels;
	}
	
	private List<TransactionModel> getTransactionModel(List<TransactionEntity> transactionEntityList) {
		List<TransactionModel> transactionList = new ArrayList();
		if (null != transactionEntityList) {
			for (Iterator<TransactionEntity> iterator = transactionEntityList.iterator(); iterator.hasNext();) {
				TransactionEntity transactionEntity = (TransactionEntity) iterator.next();
				TransactionModel transaction = new TransactionModel();
				transaction.setTransactionAmount(transactionEntity.getTransactionAmount());
				transaction.setCurrentBalance(transactionEntity.getCurrentBalance());
				transaction.setDescription(transactionEntity.getDescription());
				transaction.setGlobalId(transactionEntity.getGlobalId());
				transaction.setId(transactionEntity.getId());
				transaction.setLastUpdated(transactionEntity.getLastUpdated());
				transaction.setLastUpdatedBy(transactionEntity.getLastUpdatedBy());
				//transaction.setAccountModel(getAccountModel(transactionEntity.getAccountEntity()));
				transactionList.add(transaction);
			}
		}
		return transactionList;
	}

	private AccountModel getAccountModel(AccountEntity accountEntity) {
		AccountModel accountModel = new AccountModel();
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

	@Transactional(rollbackFor = WalletException.class)
	@Override
	public boolean createWallet(@NotNull AccountModel accountModel) throws WalletException {
		boolean successful = false; 
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setEmailId(accountModel.getEmailId());
		accountEntity.setPassword(accountModel.getPassword());
		accountEntity.setName(accountModel.getName());
		accountEntity.setMobile(accountModel.getMobile());
		accountEntity.setAddress(accountModel.getAddress());
		accountEntity.setBalance(new BigDecimal(0));
		accountEntity.setLastUpdatedBy(updatedBy);
		if(accountDAO.save(accountEntity) != null);
			successful = true;
		return successful;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = WalletException.class)
	@Override
	public BigDecimal updateWalletAmount(@NotNull AccountEntity accountEntity, @NotBlank String amount,
			@NotNull Boolean isCredit) throws WalletException {
		try {
			BigDecimal transactionAmount = (isCredit) ? new BigDecimal(amount).abs()
					: new BigDecimal(amount).abs().negate();

			// check that there is enough funds on accountEntity balance for debit transaction
			Boolean condition = (isCredit || (accountEntity.getBalance().compareTo(transactionAmount.abs()) >= 0));
			validator.isTrue(condition, String.format(ErrorMessage.NOT_ENOUGH_FUNDS, accountEntity.getId(), amount),
					ErrorCode.BadRequest.getCode());

			// update accountEntity
			BigDecimal newTransactionAmount = accountEntity.getBalance().add(transactionAmount);
			accountEntity.setBalance(newTransactionAmount);
			accountEntity.setLastUpdatedBy(updatedBy);
			accountEntity.setLastUpdated(new Date());

		    accountDAO.update(accountEntity);
			return newTransactionAmount;
		} catch (NumberFormatException e) {
			String error = String.format(ErrorMessage.NUMBER_FORMAT_MISMATCH, amount);
			throw new WalletException(error, ErrorCode.BadRequest.getCode());
		}
	}

	@Transactional(rollbackFor = WalletException.class)
	@Override
	public void updateWallet(AccountModel accountModel) throws WalletException {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setEmailId(accountModel.getEmailId());
		accountEntity.setPassword(accountModel.getPassword());
		accountEntity.setName(accountModel.getName());
		accountEntity.setMobile(accountModel.getMobile());
		accountEntity.setAddress(accountModel.getAddress());
		accountEntity.setBalance(new BigDecimal(0));
		accountEntity.setLastUpdatedBy(updatedBy);
		accountDAO.update(accountEntity);
	}
	
	@Override
	public List<TransactionModel> getTransactions(String emailId) throws WalletException {
		List<AccountEntity> list = accountDAO.findAllTransactions(emailId);
		List<TransactionModel> transactions = getTransactionModel(list.get(0).getTransactions());
		return transactions;
	}

	@Override
	public List<TransactionModel> getTransactions(String emailId, String fromDate, String toDate) throws WalletException, ParseException {
		List<AccountEntity> list = accountDAO.findAllTransactions(emailId, fromDate, toDate);
		List<TransactionModel> transactions = getTransactionModel(list.get(0).getTransactions());
		return transactions;
	}
}
