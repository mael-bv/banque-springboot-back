package com.ebanque.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ebanque.dtos.AccountOperationDto;
import com.ebanque.dtos.CurrentBankAccountDto;
import com.ebanque.dtos.CustomerDTO;
import com.ebanque.dtos.SavingBankAccountDto;
import com.ebanque.entities.AccountOperation;
import com.ebanque.entities.CurrentAccount;
import com.ebanque.entities.Customer;
import com.ebanque.entities.SavingAccount;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class BankAccountMapperImpl  {
	public CustomerDTO fromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDTO);
		//customerDTO.setId(customer.getId());
		//customerDTO.setMail(customer.getName());
		//customerDTO.setName(customer.getName());
		return customerDTO;
	}
	public Customer fromCustomerDto(CustomerDTO customerDto) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDto, customer);
		return customer;
	}
	
	public SavingBankAccountDto fromSavingBankAccount(SavingAccount savingAcount) {
		SavingBankAccountDto savingBankAccountDto = new SavingBankAccountDto();
		BeanUtils.copyProperties(savingAcount, savingBankAccountDto);
		savingBankAccountDto.setCustomerDto(fromCustomer(savingAcount.getCustomer()));
		return savingBankAccountDto;
	}
	
	public SavingAccount fromSavingBankAccountDto(SavingBankAccountDto savingAcountDto) {
		SavingAccount savingAccount = new SavingAccount();
		BeanUtils.copyProperties(savingAcountDto, savingAccount);
		savingAccount.setCustomer(fromCustomerDto(savingAcountDto.getCustomerDto()));
		savingAcountDto.setType(savingAccount.getClass().getSimpleName());
		return savingAccount;
		
	}
	
	public AccountOperationDto fromAccountOperation(AccountOperation accountOperation) {
		AccountOperationDto accountOperationDto = new AccountOperationDto();
		BeanUtils.copyProperties(accountOperation, accountOperationDto);
		return accountOperationDto;
	}
	public AccountOperation fromAccountOperationDto(AccountOperationDto accountOperationDto) {
		AccountOperation accountOperation = new AccountOperation();
		BeanUtils.copyProperties(accountOperationDto, accountOperation);
		return accountOperation;
		
	}
	public CurrentBankAccountDto fromCurrentBankAccount(CurrentAccount currentAcount) {
		CurrentBankAccountDto currentBankAccountDto = new CurrentBankAccountDto();
		BeanUtils.copyProperties(currentAcount, currentBankAccountDto);
		currentBankAccountDto.setCustomerDto(fromCustomer(currentAcount.getCustomer()));
		currentBankAccountDto.setType(currentAcount.getClass().getSimpleName());
		return currentBankAccountDto;
	}
	
	public CurrentAccount fromCurrentBankAccountDto(CurrentBankAccountDto currentAcountDto) {
		CurrentAccount currentAccount = new CurrentAccount();
		BeanUtils.copyProperties(currentAcountDto, currentAccount);
		currentAccount.setCustomer(fromCustomerDto(currentAcountDto.getCustomerDto()));
		return currentAccount;
	}
	
}
