package com.emergency.userservice.service;

import com.emergency.userservice.dto.RegisterRequest;
import com.emergency.userservice.dto.RegisterResponse;
import com.emergency.userservice.entity.Role;
import com.emergency.userservice.entity.User;
import com.emergency.userservice.exception.EmailAlreadyExistsException;
import com.emergency.userservice.repository.RoleRepository;
import com.emergency.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            log.info("Starting registration for: {}", request.email());

            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException(request.email());
            }

            log.info("Email is unique, finding CITIZEN role...");
            Role citizenRole = roleRepository.findByName("CITIZEN")
                    .orElseThrow(() -> new RuntimeException("CITIZEN role not found in DB"));

            log.info("Found role: {}", citizenRole.getName());

            Set<Role> roles = new HashSet<>();
            roles.add(citizenRole);

            User user = new User();
            user.setEmail(request.email());
            user.setPasswordHash(passwordEncoder.encode(request.password()));
            user.setFullName(request.fullName());
            user.setPhoneNumber(request.phoneNumber());
            user.setIsActive(true);
            user.setRoles(roles);

            log.info("Saving user...");
            User saved = userRepository.save(user);
            log.info("User saved successfully: {}", saved.getId());

            Set<String> roleNames = saved.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            return new RegisterResponse(
                    saved.getId(),
                    saved.getFullName(),
                    saved.getEmail(),
                    saved.getPhoneNumber(),
                    roleNames
            );
        } catch (EmailAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error("Registration failed for {}: {} - {}", request.email(), e.getClass().getName(), e.getMessage(), e);
            throw e;
        }
    }
}