package com.ebanque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebanque.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}
