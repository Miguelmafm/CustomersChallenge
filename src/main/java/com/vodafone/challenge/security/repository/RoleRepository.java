package com.vodafone.challenge.security.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vodafone.challenge.security.model.Role;
import com.vodafone.challenge.security.model.RoleEnum;


public interface RoleRepository extends MongoRepository<Role, String>
{

  Optional<Role> findByName(RoleEnum name);

}
