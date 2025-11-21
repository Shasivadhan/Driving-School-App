package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @PostMapping("/api/auth/me")
    @ResponseBody
    public AuthResponse me(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse("ROLE_STUDENT");
        return new AuthResponse(username, role);
    }
}
