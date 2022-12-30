package com.ebanque.dtos;

import lombok.Data;

@Data
public class TransferDto {
	private String accountSource;
	private String accountDestination;
	private double amount;
	private String description;

}
