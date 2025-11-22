package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // Optional: redirect root to dashboard (does NOT conflict with DashboardController)
    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @PostMapping("/api/auth/me")
    @ResponseBody
    public AuthResponse me(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_STUDENT");

        return new AuthResponse(username, role);
    }
}
