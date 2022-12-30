package com.ebanque.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import com.ebanque.dtos.AccountHistoryDto;
import com.ebanque.dtos.AccountOperationDto;
import com.ebanque.dtos.BankAccountDto;
import com.ebanque.dtos.CurrentBankAccountDto;
import com.ebanque.dtos.CustomerDTO;
import com.ebanque.dtos.SavingBankAccountDto;
import com.ebanque.entities.AccountOperation;
import com.ebanque.entities.BankAccount;
import com.ebanque.entities.CurrentAccount;
import com.ebanque.entities.Customer;
import com.ebanque.entities.SavingAccount;
import com.ebanque.enums.OperationType;
import com.ebanque.exception.BalanceNotSufficentExcetpion;
import com.ebanque.exception.BankAccountNotFoundExeption;
import com.ebanque.exception.CustomerNotFoundException;
import com.ebanque.mappers.BankAccountMapperImpl;
import com.ebanque.repository.AccountOperationRepository;
import com.ebanque.repository.BankAccountRepository;
import com.ebanque.repository.CustomerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
private BankAccountRepository bankAccountRepository;
private CustomerRepository customerRepository;
private AccountOperationRepository accountOperationRpository;
private BankAccountMapperImpl dtoMapper;

@Override
public CustomerDTO saveCustomer(CustomerDTO customerDto) {
	log.info("saving customer");
	Customer customer = dtoMapper.fromCustomerDto(customerDto);
	Customer saveCustomer = customerRepository.save(customer);
	CustomerDTO customerDTO2 = dtoMapper.fromCustomer(saveCustomer);
	return customerDTO2;
}

@Override
public List<CustomerDTO> listCustomer() {

	List<Customer> customers = customerRepository.findAll();
	List<CustomerDTO> customerDtos = customers.stream().map(customer-> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
	/*
	List<CustomerDTO> customersDtos = new ArrayList<>();
	for(Customer cutomer :customers) {
		CustomerDTO customerDto = dtoMapper.fromCustomer(cutomer);
		customersDtos.add(customerDto);
	}
	return customersDtos; */
	return customerDtos;
}
@Override
public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFoundExeption {
	BankAccount bankAccount = bankAccountRepository.findById(accountId)
			.orElseThrow(()->new BankAccountNotFoundExeption("bank no found "));
	
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingBankAccount(savingAccount);
		}else {
			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentBankAccount(currentAccount);
		}
}
@Override
public void debit(String accountId, double amount, String description) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion {
	BankAccount bankAccount = bankAccountRepository.findById(accountId)
			.orElseThrow(()->new BankAccountNotFoundExeption("bank no found "));
		if(bankAccount.getBalance()<amount) {
		throw new BalanceNotSufficentExcetpion("Banlance insuffisant");
	}else {
		AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.DEBIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRpository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()-amount);
		bankAccountRepository.save(bankAccount);
	}
}
@Override
public void credit(String accountId, double amount, String description) throws BankAccountNotFoundExeption{
	BankAccount bankAccount = bankAccountRepository.findById(accountId)
			.orElseThrow(()->new BankAccountNotFoundExeption("bank no found "));
			AccountOperation accountOperation = new AccountOperation();
		accountOperation.setType(OperationType.CREDIT);
		accountOperation.setAmount(amount);
		accountOperation.setDescription(description);
		accountOperation.setOperationDate(new Date());
		accountOperation.setBankAccount(bankAccount);
		accountOperationRpository.save(accountOperation);
		bankAccount.setBalance(bankAccount.getBalance()+amount);
		bankAccountRepository.save(bankAccount);	
	
}

@Override
	public void transfer(String accoutIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion {
		debit(accoutIdSource, amount, "Transfer to "+accountIdDestination);
		credit(accountIdDestination, amount, "Transfer to "+accoutIdSource);
		
	}

@Override
public SavingBankAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
		throws CustomerNotFoundException {
	Customer customer = customerRepository.findById(customerId).orElse(null);
	if(customer==null) {
		throw new CustomerNotFoundException("Customer not found");
	}
	SavingAccount savingAccount = new SavingAccount();
	savingAccount.setId(UUID.randomUUID().toString());
	savingAccount.setCreateAt(new Date());
	savingAccount.setBalance(initialBalance);
	savingAccount.setCustomer(customer);
	savingAccount.setInterestRate(interestRate);
	SavingAccount savedSavingAccout = bankAccountRepository.save(savingAccount);
	return dtoMapper.fromSavingBankAccount(savedSavingAccout);	
}

@Override
public CurrentBankAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
		throws CustomerNotFoundException {
	Customer customer = customerRepository.findById(customerId).orElse(null);
	if(customer==null) {
		throw new CustomerNotFoundException("Customer not found");
	}
	CurrentAccount currenAccount = new CurrentAccount();
	currenAccount.setId(UUID.randomUUID().toString());
	currenAccount.setCreateAt(new Date());
	currenAccount.setBalance(initialBalance);
	currenAccount.setCustomer(customer);
	currenAccount.setOverDraft(overDraft);
	CurrentAccount savedBankAccout = bankAccountRepository.save(currenAccount);
	return dtoMapper.fromCurrentBankAccount(savedBankAccout);	
}

@Override
public List<BankAccountDto> bankAccountList(){
	List<BankAccount> bankAccounts = bankAccountRepository.findAll();
	List<BankAccountDto> bankAccountDtos = bankAccounts.stream().map(bankAccount->{
		if(bankAccount instanceof SavingAccount) {
			SavingAccount savingAccount = (SavingAccount) bankAccount;
			return dtoMapper.fromSavingBankAccount(savingAccount);
		}else {
			CurrentAccount currentAccount = (CurrentAccount) bankAccount;
			return dtoMapper.fromCurrentBankAccount(currentAccount);
		}
	}).collect(Collectors.toList());
	return bankAccountDtos;
}

@Override	
public CustomerDTO getCustomer(Long curstomerId) throws CustomerNotFoundException {
	Customer customer = customerRepository.findById(curstomerId).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
	return dtoMapper.fromCustomer(customer);
	
}

@Override
public CustomerDTO updateCustomer(CustomerDTO customerDto) {
	log.info("update customer");
	Customer customer = dtoMapper.fromCustomerDto(customerDto);
	Customer saveCustomer = customerRepository.save(customer);
	return dtoMapper.fromCustomer(saveCustomer);
}

@Override
public void deleteCustomer(Long customerId) {
	customerRepository.deleteById(customerId);
}

@Override
public List<AccountOperationDto> accountHitory(String accountId){
	List<AccountOperation> accountOperations = accountOperationRpository.findByBankAccountId(accountId);
	return accountOperations.stream().map(ope->dtoMapper.fromAccountOperation(ope)).collect(Collectors.toList());
}

@Override
	public AccountHistoryDto getAccountHitory(String accountId, int page, int size) throws BankAccountNotFoundExeption {
		BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(null);
		if(bankAccount == null) throw new BankAccountNotFoundExeption("Bank not found eee");
		Page<AccountOperation> accountOperations =  accountOperationRpository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
		AccountHistoryDto accountHistoryDto = new AccountHistoryDto();
		List<AccountOperationDto> accountOperationDtos = accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
		accountHistoryDto.setAccountOperationDtos(accountOperationDtos);
		accountHistoryDto.setAccountId(bankAccount.getId());
		accountHistoryDto.setBalance(bankAccount.getBalance());
		accountHistoryDto.setCurrentPage(page);
		accountHistoryDto.setPageSize(size);
		accountHistoryDto.setTotalPages(accountOperations.getTotalPages());
		return accountHistoryDto;
	}

@Override
	public List<CustomerDTO> searchCutomers(String keyword) {
		List<Customer> customers = customerRepository.searchCustomer(keyword);
		List<CustomerDTO> customerDTOs =   customers.stream().map(customer->dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
		return customerDTOs;
	}


}
