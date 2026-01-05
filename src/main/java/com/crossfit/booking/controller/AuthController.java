package com.crossfit.booking.controller;

import com.crossfit.booking.dto.common.MessageResponse;
import com.crossfit.booking.auth.JwtService;
import com.crossfit.booking.dto.auth.AuthResponse;
import com.crossfit.booking.dto.auth.LoginRequest;
import com.crossfit.booking.dto.user.UserRegisterRequest;
import com.crossfit.booking.model.Role;
import com.crossfit.booking.model.User;
import com.crossfit.booking.repository.RoleRepository;
import com.crossfit.booking.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest userDto) {


        // Checks duplicates
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already exists"));
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already exists"));
        }

        Set<Role> roles = new HashSet<>();

        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            // default ROLE_USER
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER missing"));
            roles.add(userRole);
        } else {
            // Search roles added
            for (UserRegisterRequest.RoleDto roleDto : userDto.getRoles()) {
                Role r = roleRepository.findByName(roleDto.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleDto.getName()));
                roles.add(r);
            }
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(roles);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User created"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_USER");

        String token = jwtService.generateToken(
                user.getUsername(),
                role
        );

        AuthResponse response = new AuthResponse(token, user.getUsername(), role, "Bearer");

        return ResponseEntity.ok(response);
    }
}

