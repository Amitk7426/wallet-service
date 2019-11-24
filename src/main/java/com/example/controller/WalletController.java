package com.example.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.account.service.AccountService;
import com.example.account.service.TransactionService;
import com.example.account.validator.Validator;
import com.example.entities.AccountEntity;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;
import com.example.models.TransactionModel;

/**
 * Restful controller for managing wallets
 */
@RestController
class WalletController {

	org.slf4j.Logger logger = LoggerFactory.getLogger(WalletController.class);

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private Validator validator;

	@GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
	public String test() throws WalletException, ClassNotFoundException {
		return "Hello from account microservice!";
	}

	@GetMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AccountModel> getAllAccounts() throws WalletException, ClassNotFoundException {
		logger.debug("Called WalletController.getAll");
		List <AccountEntity> accountEntityList = accountService.findAll();
		List <AccountModel> accountModelList = new ArrayList();
		for (Iterator iterator = accountEntityList.iterator(); iterator.hasNext();) {
			AccountEntity accountEntity = (AccountEntity) iterator.next();
			AccountModel accountModel = new AccountModel();
			accountModel.setId(accountEntity.getId());
			accountModel.setEmailId(accountEntity.getEmailId());
			accountModel.setName(accountEntity.getName());
			accountModel.setPassword(accountEntity.getPassword());
			accountModel.setMobile(accountEntity.getMobile());
			accountModel.setBalance(accountEntity.getBalance());
			accountModel.setLastUpdated(accountEntity.getLastUpdated());
			accountModel.setLastUpdatedBy(accountEntity.getLastUpdatedBy());
			accountModelList.add(accountModel);
		}
		return accountModelList;
	}


	@PutMapping(value = "/account/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountModel> putAccountByEmailId(@PathVariable("emailId") String emailId, @RequestBody HashMap<String, String> dataHashMap) throws WalletException, ClassNotFoundException {
		logger.debug("Called WalletController.getWalletById with id=" + emailId);
		
		validator.validate(dataHashMap, Arrays.asList("password","name","mobile","address"));
		AccountModel accountModel = accountService.findByEmailId(emailId).get(0);
		accountModel.setPassword(dataHashMap.get("password"));
		accountModel.setName(dataHashMap.get("name"));
		accountModel.setMobile(dataHashMap.get("mobile"));
		accountModel.setAddress(dataHashMap.get("address"));
		
		accountService.updateWallet(accountModel);
		
		return new ResponseEntity<AccountModel>(accountModel, HttpStatus.OK);
	}

	@GetMapping(value = "/account/{emailId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AccountModel> getAccountByEmailId(@PathVariable("emailId") String emailId)
			throws WalletException, ClassNotFoundException {
		logger.debug("Called WalletController.getWalletsByUserId with userId=" + emailId);
		return accountService.findByEmailId(emailId);
	}

	@PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountModel> createAccount(@RequestBody HashMap<String, String> dataHashMap) throws WalletException {
		logger.debug("Called WalletController.createAccount");
		validator.validate(dataHashMap, Arrays.asList("emailId","password","name","mobile","address"));
		AccountModel accountModel = new AccountModel();
		accountModel.setEmailId(dataHashMap.get("emailId"));
		accountModel.setPassword(dataHashMap.get("password"));
		accountModel.setName(dataHashMap.get("name"));
		accountModel.setMobile(dataHashMap.get("mobile"));
		accountModel.setAddress(dataHashMap.get("address"));
		
		accountService.createWallet(accountModel);
		return new ResponseEntity<AccountModel>(accountModel, HttpStatus.OK);
	}
	
	@GetMapping(value = "/account/{emailId}/statement", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TransactionModel>> getWalletsTransactions(@PathVariable("emailId") String emailId)
			throws WalletException, ClassNotFoundException {
		logger.debug("Called TransactionController.createWalletTransaction");

		List<TransactionModel> transactions =  accountService.getTransactions(emailId);
		
		logger.info("Transaction created with id=" + transactions.get(0).getId());

		return new ResponseEntity<List<TransactionModel>>(transactions, HttpStatus.OK);
	}

	@GetMapping(value = "/account/{emailId}/statement/{fromDate}/{toDate}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TransactionModel>> getWalletsTransactionsFromDateTo(@PathVariable("emailId") String emailId, 
						@PathVariable("fromDate") String fromDate,@PathVariable("toDate") String toDate)
			throws WalletException, ClassNotFoundException, ParseException {
		logger.debug("Called TransactionController.createWalletTransaction");

		List<TransactionModel> transactions =  accountService.getTransactions(emailId,fromDate, toDate);
		
		logger.info("Transaction created with id=" + transactions.get(0).getId());

		return new ResponseEntity<List<TransactionModel>>(transactions, HttpStatus.OK);
	}
	
	@PostMapping(value = "/account/{emailId}/funds/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountModel> addFundsToWallets(@PathVariable("emailId") String emailId, @PathVariable("amount") String amount)
			throws WalletException, ClassNotFoundException {
		logger.debug("Called TransactionController.createWalletTransaction");

	    // this will convert any number sequence into 8 character.
	    String globalId = String.format("%08d", new Random().nextInt(99999999));
	    
		String description = "Add funds";

		AccountModel accountModel = transactionService.createTransaction(globalId, emailId, amount, description, true);

		logger.info("Transaction created with id=" + accountModel.getId());

		return new ResponseEntity<AccountModel>(accountModel, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/account/{emailId}/funds/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountModel> deleteFundsToWallets(@PathVariable("emailId") String emailId, @PathVariable("amount") String amount)
			throws WalletException, ClassNotFoundException {
		logger.debug("Called TransactionController.createWalletTransaction");

	    // this will convert any number sequence into 6 character.
		String globalId = String.format("%08d", new Random().nextInt(99999999));
	    
		String description = "Remove funds";

		AccountModel accountModel = transactionService.createTransaction(globalId, emailId, amount, description, false);
		
		logger.info("Transaction created with id=" + accountModel.getId());

		return new ResponseEntity<AccountModel>(accountModel, HttpStatus.OK);
	}
}
