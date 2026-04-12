package com.emergency.userservice.service;

import com.emergency.userservice.dto.LoginRequest;
import com.emergency.userservice.dto.LoginResponse;
import com.emergency.userservice.dto.RegisterRequest;
import com.emergency.userservice.dto.RegisterResponse;
import com.emergency.userservice.entity.Role;
import com.emergency.userservice.entity.User;
import com.emergency.userservice.exception.EmailAlreadyExistsException;
import com.emergency.userservice.exception.InvalidCredentialsException;
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
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String roleName = (request.role() != null && !request.role().isBlank())
                ? request.role().toUpperCase() : "CITIZEN";

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        user.setIsActive(true);
        user.setRoles(roles);

        User saved = userRepository.save(user);
        log.info("User registered: {} with role {}", saved.getEmail(), roleName);

        Set<String> roleNames = saved.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet());

        return new RegisterResponse(saved.getId(), saved.getFullName(),
                saved.getEmail(), saved.getPhoneNumber(), roleNames);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.email());

        User user = userRepository.findByEmailWithRoles(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getIsActive()) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Failed login for: {}", request.email());
            throw new InvalidCredentialsException();
        }

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), roleNames);

        log.info("Login successful for: {}", request.email());

        return new LoginResponse(accessToken, "Bearer",
                jwtService.getAccessTokenExpiryMs(),
                user.getId(), user.getEmail(), user.getFullName(), roleNames);
    }
}
