package com.vodafone.challenge;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.vodafone.challenge.security.model.Role;
import com.vodafone.challenge.security.model.RoleEnum;


@Component
public class CommandLineAppStartupRunner implements CommandLineRunner
{

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public void run(String... args) throws Exception
  {

    if (!mongoTemplate.collectionExists("roles"))
    {
      Role roleAdmin = new Role(RoleEnum.ROLE_ADMIN);
      Role roleUser = new Role(RoleEnum.ROLE_USER);

      mongoTemplate.insert(roleAdmin);
      mongoTemplate.insert(roleUser);
    }
  }
}
