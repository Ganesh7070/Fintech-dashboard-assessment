package com.example.demo.controller;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.entity.Role;
import com.example.demo.service.DashboardService;
import com.example.demo.security.UserDetailsImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<DashboardSummary> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Role role = userDetails.getAuthorities().stream()
                .map(auth -> Role.valueOf(auth.getAuthority().replace("ROLE_", "")))
                .findFirst().orElse(Role.VIEWER);

        return ResponseEntity.ok(dashboardService.getSummary(userDetails.getUsername(), role, startDate, endDate));
    }
}
