package com.vodafone.challenge.customers.service;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vodafone.challenge.customers.model.Customers;
import com.vodafone.challenge.customers.repository.CustomersRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class CustomersService
{

  private final CustomersRepository customersRepository;
  private static final int DEFAULT_AGE = 0;


  public List<Customers> listCustomers()
  {

    List<Customers> listCustomers = customersRepository.findAll();

    LocalDate currentDate = LocalDate.now();

    listCustomers.forEach(customer -> customer.setAge(calculateAge(customer.getBirthDate(), currentDate)));

    return listCustomers;
  }

  public Customers getCustomer(String aId)
  {
    Customers customer =
    customersRepository.findById(aId).orElseThrow(() -> new IllegalStateException("Cannot find Customer by 'Id' : " + aId));

    customer.setAge(calculateAge(customer.getBirthDate(), LocalDate.now()));

    return customer;
  }

  public Customers addCustomers(Customers aCustomers)
  {
    Customers customer = customersRepository.save(aCustomers);

    customer.setAge(calculateAge(customer.getBirthDate(), LocalDate.now()));

    return customer;
  }

  public void deleteCustomers(String aId)
  {
    customersRepository.findById(aId)
    .orElseThrow(() -> new IllegalStateException("Cannot find Customer by 'Id' : " + aId));

    customersRepository.deleteById(aId);
  }

  public Customers updateCustomer(Customers aCustomer)
  {
    Customers savedCustomer =
    customersRepository.findById(aCustomer.getId())
    .orElseThrow(() -> new IllegalStateException("Cannot find Customer by 'Id' : " + aCustomer.getId()));

    savedCustomer.setName(aCustomer.getName());
    savedCustomer.setBirthDate(aCustomer.getBirthDate());
    savedCustomer.setPhoneNumber(aCustomer.getPhoneNumber());
    savedCustomer.setCreditLimit(aCustomer.getCreditLimit());

    Customers customer = customersRepository.save(savedCustomer);
    customer.setAge(calculateAge(customer.getBirthDate(), LocalDate.now()));

    return customer;
  }

  private int calculateAge(LocalDate birthDate, LocalDate currentDate)
  {
    return ((birthDate != null) && (currentDate != null)) ? Period.between(birthDate, currentDate).getYears() : DEFAULT_AGE;
  }
}
