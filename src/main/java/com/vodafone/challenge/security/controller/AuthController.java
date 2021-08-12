package com.vodafone.challenge.security.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vodafone.challenge.security.jwt.JwtUtils;
import com.vodafone.challenge.security.model.Role;
import com.vodafone.challenge.security.model.RoleEnum;
import com.vodafone.challenge.security.model.User;
import com.vodafone.challenge.security.payload.request.LoginRequest;
import com.vodafone.challenge.security.payload.request.SignupRequest;
import com.vodafone.challenge.security.payload.response.JwtResponse;
import com.vodafone.challenge.security.repository.RoleRepository;
import com.vodafone.challenge.security.repository.UserRepository;
import com.vodafone.challenge.security.service.UserDetailsImpl;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController
{

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/login")
  @PreAuthorize("permitAll")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
  {

    Authentication authentication = authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
    .map(item -> item.getAuthority())
    .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
    userDetails.getId(),
    userDetails.getUsername(),
    userDetails.getEmail(),
    roles));
  }

  @PostMapping("/signup")
  @PreAuthorize("permitAll")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest)
  {
    if (userRepository.existsByUsername(signUpRequest.getUsername()))
    {
      return ResponseEntity
      .badRequest()
      .body("Error: Username is already taken!");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail()))
    {
      return ResponseEntity
      .badRequest()
      .body("Error: Email is already in use!");
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
    signUpRequest.getEmail(),
    encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRoles();
    Set<Role> roles = validateRoles(strRoles);

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully!");
  }

  private Set<Role> validateRoles(Set<String> aStrRoles)
  {
    Set<Role> roles = new HashSet<>();

    if (aStrRoles == null)
    {
      Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    }
    else
    {
      aStrRoles.forEach(role -> {
        switch (role) // in case we want to add more roles
        {
          case "admin":
          case "Admin":
          case "ADMIN":
            Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          default:
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    return roles;
  }
}
