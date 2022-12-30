package com.ebanque.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanque.dtos.CustomerDTO;
import com.ebanque.entities.Customer;
import com.ebanque.exception.CustomerNotFoundException;
import com.ebanque.service.BankAccountService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
	private BankAccountService bankAccountService;
	
	@GetMapping("/customers")
	public List<CustomerDTO> customers(){
		return bankAccountService.listCustomer();
	}
	
	@GetMapping("/customers/search")
	public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword){
		return bankAccountService.searchCutomers("%"+keyword+"%");
	}
	
	
	
	
	@GetMapping("/customers/{id}")
	public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
		return bankAccountService.getCustomer(customerId);
	}
	
	@PostMapping("/customers")
	public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDto) {
		return bankAccountService.saveCustomer(customerDto);
	}
	@PutMapping("/customers/{customerId}")
	public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDto) {
		customerDto.setId(customerId);
		return bankAccountService.updateCustomer(customerDto);
	}
	
	@DeleteMapping("customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		bankAccountService.deleteCustomer(id);
	}
}
