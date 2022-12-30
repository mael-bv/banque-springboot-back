package com.ebanque;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ebanque.dtos.BankAccountDto;
import com.ebanque.dtos.CurrentBankAccountDto;
import com.ebanque.dtos.CustomerDTO;
import com.ebanque.dtos.SavingBankAccountDto;
import com.ebanque.entities.AccountOperation;
import com.ebanque.entities.BankAccount;
import com.ebanque.entities.CurrentAccount;
import com.ebanque.entities.Customer;
import com.ebanque.entities.SavingAccount;
import com.ebanque.enums.AccountStatus;
import com.ebanque.enums.OperationType;
import com.ebanque.exception.BalanceNotSufficentExcetpion;
import com.ebanque.exception.BankAccountNotFoundExeption;
import com.ebanque.exception.CustomerNotFoundException;
import com.ebanque.repository.AccountOperationRepository;
import com.ebanque.repository.BankAccountRepository;
import com.ebanque.repository.CustomerRepository;
import com.ebanque.service.BankAccountService;
import com.ebanque.service.BankService;

@SpringBootApplication
public class EBanqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBanqueApplication.class, args);
	}
	
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		
		return args->{
			Stream.of("Laura","Melanie","Aurelien","Eric").forEach(name->{
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setMail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomer().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*9000, 9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());
					
				
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			
			});
			
			List<BankAccountDto> bankAccounts = bankAccountService.bankAccountList();
			for(BankAccountDto bankAccount : bankAccounts) {
				for(int i=0;i<10;i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAccountDto) {
						accountId = ((SavingBankAccountDto) bankAccount).getId();
					}else {
						accountId = ((CurrentBankAccountDto) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
					bankAccountService.debit(accountId, 1000+Math.random()*9000, "Debit");
				}
				
			}
		
			
		};
	}
	
	
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountBankRepository) {
		return args -> {
			Stream.of("Mike","Aurelien","Arthur").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setMail(name + "@gmail.com");
				customerRepository.save(customer);
			});
				customerRepository.findAll().forEach(cust->{
					CurrentAccount currentAccount = new CurrentAccount();
					currentAccount.setId(UUID.randomUUID().toString());
					currentAccount.setBalance(Math.random()*9000);
					currentAccount.setCreateAt(new Date());
					currentAccount.setStatus(AccountStatus.CREATED);
					currentAccount.setCustomer(cust);
					currentAccount.setOverDraft(9000);
					bankAccountRepository.save(currentAccount);
					
					
					SavingAccount savingAccount = new SavingAccount();
					savingAccount.setId(UUID.randomUUID().toString());
					savingAccount.setBalance(Math.random()*9000);
					savingAccount.setCreateAt(new Date());
					savingAccount.setStatus(AccountStatus.CREATED);
					savingAccount.setCustomer(cust);	
					savingAccount.setInterestRate(5.5);
					bankAccountRepository.save(savingAccount); 
					
				});
				
				bankAccountRepository.findAll().forEach(acc->{
					for(int i=0;i<10;i++) {
						AccountOperation operation = new AccountOperation();
						operation.setOperationDate(new Date());
						operation.setAmount(Math.random()*12000);
						operation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
						operation.setBankAccount(acc);
						accountBankRepository.save(operation); 
					}
				});
				
				
				
		};
	}

}
