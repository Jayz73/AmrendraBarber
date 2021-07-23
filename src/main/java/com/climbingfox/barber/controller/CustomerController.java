package com.climbingfox.barber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.climbingfox.barber.entity.Customer;
import com.climbingfox.barber.service.WelcomeService;

@RestController
@RequestMapping("/shop")
public class CustomerController {
	@Autowired
	private WelcomeService service;

	@PostMapping("/welcome")
	public ResponseEntity<String> newPolicy(@RequestBody Customer customer) throws Exception {
		String ret = service.newCustomer(customer);
		return new ResponseEntity<>(ret, HttpStatus.CREATED);
	}

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello";
	}
}
