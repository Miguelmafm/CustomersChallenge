package com.vodafone.challenge.customers.model;


import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
@Document()
public class Customers
{

  @Id
  private String id;
  @NotBlank(message = "Name is mandatory")
  private String name;
  private String phoneNumber;
  @NotNull(message = "BirthDate is mandatory")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @PastOrPresent(message = "BirthDate must be a date in past or in the present")
  private LocalDate birthDate;
  @Transient
  private Integer age;
  private BigDecimal creditLimit;

}
