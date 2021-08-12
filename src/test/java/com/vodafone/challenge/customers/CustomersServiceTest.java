package com.vodafone.challenge.customers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.vodafone.challenge.customers.model.Customers;
import com.vodafone.challenge.customers.repository.CustomersRepository;
import com.vodafone.challenge.customers.service.CustomersService;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomersServiceTest
{

  @Mock
  private CustomersRepository customersRepository;
  private CustomersService underTest;
  private Customers customerTest;

  @BeforeEach
  void setUp()
  {
    underTest = new CustomersService(customersRepository);

    customerTest = new Customers();
    customerTest.setId("tstId");
    customerTest.setName("MiguelTST");
    customerTest.setPhoneNumber("966745100");
    customerTest.setBirthDate(LocalDate.of(1996, Month.OCTOBER, 13));
    customerTest.setCreditLimit(new BigDecimal(300));
  }


  @Test
  void canListAllCustomers()
  {
    // given
    List<Customers> customers = new ArrayList<Customers>();
    customers.add(customerTest);

    // when
    Mockito.when(customersRepository.findAll()).thenReturn(customers);

    List<Customers> actual = underTest.listCustomers();

    // then
    assertEquals(customers, actual);

    verify(customersRepository).findAll();

  }

  @Test
  void canDetailCustomer()
  {

    // when
    Mockito.when(customersRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));

    Customers actual = underTest.getCustomer(customerTest.getId());

    // then
    assertEquals(customerTest, actual);

    verify(customersRepository).findById(customerTest.getId());

  }

  @Test()
  void shouldThrowExceptionWhenDetailDoesntExist() throws Exception
  {

    Exception exception = assertThrows(IllegalStateException.class, () -> {

      underTest.getCustomer(customerTest.getId());
    });

    String expectedMessage = "Cannot find Customer by 'Id' : " + customerTest.getId();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  void canCreateCustomers()
  {

    // when
    Mockito.when(customersRepository.save(ArgumentMatchers.any(Customers.class))).thenReturn(customerTest);

    Customers createdCustomer = underTest.addCustomers(customerTest);

    // then

    assertEquals(customerTest.getName(), createdCustomer.getName());

    verify(customersRepository).save(customerTest);

  }

  @Test
  void canUpdateCustomers()
  {
    // given

    // new customer credit limit 500
    Customers newCustomer = new Customers();
    newCustomer.setId("tstId");
    newCustomer.setName("NEWMiguelTST");
    newCustomer.setPhoneNumber("966745100");
    newCustomer.setBirthDate(LocalDate.of(1996, Month.OCTOBER, 13));
    newCustomer.setCreditLimit(new BigDecimal(500));

    // when
    Mockito.when(customersRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));

    Mockito.when(customersRepository.save(newCustomer)).thenReturn(newCustomer);

    Customers updatedCustomer = underTest.updateCustomer(newCustomer);

    // then

    verify(customersRepository).findById(customerTest.getId());

    assertEquals(newCustomer.getName(), updatedCustomer.getName());

  }

  @Test()
  void shouldThrowExceptionWhenUpdateDoesntExist() throws Exception
  {

    Exception exception = assertThrows(IllegalStateException.class, () -> {

      underTest.updateCustomer(customerTest);
    });

    String expectedMessage = "Cannot find Customer by 'Id' : " + customerTest.getId();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }


  @Test
  void canDeleteCustomers()
  {

    // when
    Mockito.when(customersRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));

    underTest.deleteCustomers(customerTest.getId());

    // then
    verify(customersRepository).deleteById(customerTest.getId());

  }

  @Test()
  void shouldThrowExceptionWhenDeleteIdDoesntExist() throws Exception
  {

    Exception exception = assertThrows(IllegalStateException.class, () -> {

      underTest.deleteCustomers(customerTest.getId());
    });

    String expectedMessage = "Cannot find Customer by 'Id' : " + customerTest.getId();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  void canCalculateCustomerAge()
  {
    // when
    Mockito.when(customersRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));

    Customers testCustomer = underTest.getCustomer(customerTest.getId());

    // then

    assertEquals(24, testCustomer.getAge());
  }

  @Test
  void canCalculateCustomerDefaultAge()
  {
    // given
    Customers customerWithoutBirthdate = customerTest;
    customerWithoutBirthdate.setBirthDate(null);

    // when
    Mockito.when(customersRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerWithoutBirthdate));

    Customers testCustomer = underTest.getCustomer(customerTest.getId());

    // then

    assertEquals(0, testCustomer.getAge());
  }


}
