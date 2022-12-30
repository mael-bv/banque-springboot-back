package com.ebanque.dtos;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor
public  class CurrentBankAccountDto extends BankAccountDto{
	private String id;
	private double balance;
	private Date createAt;
	private CustomerDTO customerDto;
	private double overDraft;
	

}
