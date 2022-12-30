package com.ebanque.dtos;

import java.util.List;
import lombok.Data;

@Data
public class AccountHistoryDto {
	private String accountId;
	private double balance;
	private int currentPage;
	private int totalPages;
	private int pageSize;
	List<AccountOperationDto> accountOperationDtos;
}
