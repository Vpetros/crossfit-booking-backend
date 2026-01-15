package com.crossfit.booking.controller;

import com.crossfit.booking.dto.admin.AdminMembersTabResponse;
import com.crossfit.booking.model.User;
import com.crossfit.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMembersTabController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<AdminMembersTabResponse>> getMembers() {
        List<User> users;
        try {
            users = userRepository.findAllByOrderByCreatedAtDesc();
        } catch (Exception ex) {
            users = userRepository.findAll();
        }

        List<AdminMembersTabResponse> response = users.stream()
                // only ROLE_USER (exclude admins)
                .filter(u -> u.getRoles() != null && u.getRoles().stream()
                        .anyMatch(r -> "ROLE_USER".equals(r.getName())))
                // only fields needed in Members tab
                .map(u -> AdminMembersTabResponse.builder()
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .createdAt(u.getCreatedAt())
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }
}
