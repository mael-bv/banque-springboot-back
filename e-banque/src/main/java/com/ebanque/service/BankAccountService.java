package com.ebanque.service;

import java.util.List;

import com.ebanque.dtos.AccountHistoryDto;
import com.ebanque.dtos.AccountOperationDto;
import com.ebanque.dtos.BankAccountDto;
import com.ebanque.dtos.CurrentBankAccountDto;
import com.ebanque.dtos.CustomerDTO;
import com.ebanque.dtos.SavingBankAccountDto;
import com.ebanque.entities.BankAccount;
import com.ebanque.entities.CurrentAccount;
import com.ebanque.entities.Customer;
import com.ebanque.entities.SavingAccount;
import com.ebanque.exception.BalanceNotSufficentExcetpion;
import com.ebanque.exception.BankAccountNotFoundExeption;
import com.ebanque.exception.CustomerNotFoundException;

public interface BankAccountService {
	 CustomerDTO saveCustomer(CustomerDTO customerDto);
	 SavingBankAccountDto saveSavingBankAccount(double initialBalance,double interestRate,Long customerId) throws CustomerNotFoundException;
	 CurrentBankAccountDto saveCurrentBankAccount(double initialBalance,double overDraft,Long customerId) throws CustomerNotFoundException;
	 List<CustomerDTO> listCustomer();
	 BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundExeption;
	 void debit(String accountId,double amount,String description) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion; 
	void credit(String accountId,double amount,String description) throws BankAccountNotFoundExeption; 
	void transfer(String accoutIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion;
	List<BankAccountDto> bankAccountList();
	CustomerDTO getCustomer(Long curstomerId) throws CustomerNotFoundException;
	CustomerDTO updateCustomer(CustomerDTO customerDto);
	void deleteCustomer(Long customerId);
	List<AccountOperationDto> accountHitory(String accountId);
	AccountHistoryDto getAccountHitory(String accountId, int page, int size) throws BankAccountNotFoundExeption;
	List<CustomerDTO> searchCutomers(String keyword);
}
