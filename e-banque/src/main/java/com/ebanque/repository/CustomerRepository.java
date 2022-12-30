package com.ebanque.repository;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ebanque.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	//List<Customer> findByNameContains(String keyword);
	
	@Query("select c from Customer c where c.name like :kw")
	List<Customer> searchCustomer(@Param("kw") String keyword);
}

