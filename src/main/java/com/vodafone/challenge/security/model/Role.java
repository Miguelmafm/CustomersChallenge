package com.vodafone.challenge.security.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document(collection = "roles")
public class Role
{

  @Id
  private String id;
  private RoleEnum name;


  public Role(RoleEnum name)
  {
    this.name = name;
  }


}
