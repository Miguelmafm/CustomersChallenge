package com.vodafone.challenge.customers.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.vodafone.challenge.customers.model.Customers;


public interface CustomersRepository extends MongoRepository<Customers, String>
{
  //
}
