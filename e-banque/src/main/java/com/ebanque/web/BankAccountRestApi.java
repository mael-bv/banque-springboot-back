package com.ebanque.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanque.dtos.AccountHistoryDto;
import com.ebanque.dtos.AccountOperationDto;
import com.ebanque.dtos.BankAccountDto;
import com.ebanque.dtos.CreditDto;
import com.ebanque.dtos.DebitDto;
import com.ebanque.dtos.TransferDto;
import com.ebanque.exception.BalanceNotSufficentExcetpion;
import com.ebanque.exception.BankAccountNotFoundExeption;
import com.ebanque.service.BankAccountService;

@RestController
@CrossOrigin("*")
public class BankAccountRestApi {
	
	private BankAccountService bankAccountService;
	
	public BankAccountRestApi(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@GetMapping("/accounts/{accoundId}")
	public BankAccountDto getBankAccountDto(@PathVariable String accoundId) throws BankAccountNotFoundExeption {
		return bankAccountService.getBankAccount(accoundId);
	}
	
	@GetMapping("/accounts")
	public List<BankAccountDto> listAccount() {
		return bankAccountService.bankAccountList();
	}
	
	@GetMapping("/accounts/{accountId}/operations")
	public List<AccountOperationDto> getHistory(@PathVariable String accountId){
	 return bankAccountService.accountHitory(accountId);	
	}
	
	@GetMapping("/accounts/{accountId}/pageOperations")
	public AccountHistoryDto getAccountHistory(@PathVariable String accountId,
																	@RequestParam(name="page", defaultValue = "0") int page, 
																	@RequestParam(name="size", defaultValue = "5")int size) throws BankAccountNotFoundExeption{
	 return bankAccountService.getAccountHitory(accountId,page,size);	
	}
	
	@PostMapping("/accounts/debit")
	public DebitDto debit(@RequestBody DebitDto debitDto) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion {
		bankAccountService.debit(debitDto.getAccountId(), debitDto.getAmount(), debitDto.getDescription());
		return debitDto;
	}
	
	@PostMapping("/accounts/credit")
	public CreditDto credit(@RequestBody CreditDto creditDto) throws BankAccountNotFoundExeption {
		bankAccountService.credit(creditDto.getAccountId(), creditDto.getAmount(), creditDto.getDescription());
		return creditDto;
	}
	
	@PostMapping("/accounts/transfer")
	public void transfer(@RequestBody TransferDto transferDto) throws BankAccountNotFoundExeption, BalanceNotSufficentExcetpion{
		bankAccountService.transfer(transferDto.getAccountSource(), transferDto.getAccountDestination(), transferDto.getAmount());
		
	}
	
}
