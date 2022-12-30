package com.ebanque.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebanque.entities.BankAccount;
import com.ebanque.entities.CurrentAccount;
import com.ebanque.entities.SavingAccount;
import com.ebanque.repository.BankAccountRepository;

@Service
@Transactional
public class BankService {
	@Autowired
	private BankAccountRepository bankAccountRepository;

	public void consulter() {
		BankAccount bank = bankAccountRepository.findById("fb4f0a62-2da4-4fa5-a758-9fda4e9ee1e7").orElse(null);
		if(bank != null) {
		System.out.println("***************************************************");
		System.out.println(bank.getId());
		System.out.println(bank.getBalance());
		System.out.println(bank.getStatus());
		System.out.println(bank.getCreateAt());
		System.out.println(bank.getCustomer().getName());
		if(bank instanceof CurrentAccount) {
			System.out.println("oVER DRAFT => " + ((CurrentAccount)bank).getOverDraft());
		}else if (bank instanceof SavingAccount) {
			System.out.println("Rate => " + ((SavingAccount)bank).getInterestRate());
		}
		bank.getAccoutOperations().forEach(op->{
			System.out.println(op.getType()+"\t" + op.getOperationDate());
	
			});
		}
	}
}
