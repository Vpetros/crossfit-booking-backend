package com.crossfit.booking.controller;

import com.crossfit.booking.dto.user.UserCreateRequest;
import com.crossfit.booking.dto.user.UserResponse;
import com.crossfit.booking.dto.user.UserUpdateRequest;
import com.crossfit.booking.mapper.UserMapper;
import com.crossfit.booking.model.Role;
import com.crossfit.booking.model.User;
import com.crossfit.booking.repository.RoleRepository;
import com.crossfit.booking.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            UserMapper userMapper,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(userMapper::mapToResponse)
                .orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable String id,
            @Valid
            @RequestBody UserUpdateRequest request)
    {
        return userRepository.findById(id)
                .map(existingUser ->
                {
                    existingUser.setUsername(request.getUsername());
                    existingUser.setEmail(request.getEmail());

                    return userMapper.mapToResponse(
                            userRepository.save(existingUser));
                })
                .orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {

        Set<Role> roles = request.getRoles()
                .stream()
                .map(roleName ->
                        roleRepository.findByName(roleName)
                                .orElseThrow(() ->
                                        new RuntimeException("Role not found: " + roleName)
                                )
                )
                .collect(Collectors.toSet());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        return userMapper.mapToResponse(
                userRepository.save(user)
        );

    }
}
