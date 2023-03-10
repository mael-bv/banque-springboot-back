package com.ebanque.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebanque.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
	 List<AccountOperation> findByBankAccountId(String accountId);
	 Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);
	 
}
