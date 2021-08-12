package com.vodafone.challenge.customers.controller;


import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vodafone.challenge.customers.model.Customers;
import com.vodafone.challenge.customers.service.CustomersService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
public class CustomersController
{

  private final CustomersService customersService;

  @GetMapping
  @PreAuthorize("permitAll")
  public ResponseEntity<List<Customers>> listCustomers()
  {
    return ResponseEntity.ok(customersService.listCustomers());
  }

  @GetMapping("/{id}")
  @PreAuthorize("permitAll")
  public ResponseEntity<Customers> detailCustomer(@PathVariable String id)
  {
    return ResponseEntity.ok(customersService.getCustomer(id));
  }

  @PostMapping
  @PreAuthorize("permitAll")
  public ResponseEntity createCustomers(@Valid @RequestBody Customers customers)
  {

    Customers createdCustomers = customersService.addCustomers(customers);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
    .path("/{id}")
    .buildAndExpand(createdCustomers)
    .toUri();

    return ResponseEntity.created(uri).body(createdCustomers);
  }

  @PutMapping
  @PreAuthorize("permitAll")
  public ResponseEntity updateCustomer(@Valid @RequestBody Customers aCustomer)
  {
    return ResponseEntity.ok().body(customersService.updateCustomer(aCustomer));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity deleteCustomers(@PathVariable String id)
  {
    customersService.deleteCustomers(id);

    return ResponseEntity.ok().build();
  }

}
