package com.ebanque.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data  @NoArgsConstructor @AllArgsConstructor
public class Customer {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String mail;
	@OneToMany(mappedBy = "customer" )
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<BankAccount> bankAccounts;

}
