package com.example.controller;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.account.service.TransactionService;
import com.example.exceptions.WalletException;
import com.example.models.AccountModel;

/**
 * Restful controller for managing wallet transactions
 *
 */
//@RestController
// @RequestMapping("/api")
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TransactionService transactionService;

	@PostMapping(value = "/account/{emailId}/funds/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountModel> addFundsToWallets(@PathVariable("emailId") String emailId, @PathVariable("amount") String amount)
			throws WalletException, ClassNotFoundException {
		logger.debug("Called TransactionController.createWalletTransaction");

	    // this will convert any number sequence into 6 character.
	    String globalId = String.format("%06d", new Random().nextInt(999999));
	    
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
	    String globalId = String.format("%06d", new Random().nextInt(999999));
	    
		String description = "Remove funds";

		AccountModel accountModel = transactionService.createTransaction(globalId, emailId, amount, description, false);
		
		logger.info("Transaction created with id=" + accountModel.getId());

		return new ResponseEntity<AccountModel>(accountModel, HttpStatus.OK);
	}
	
}
