package com.ksvtech.drivingschool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // This looks inside the 'admin' folder for 'dashboard.html'
        return "dashboard";
    }
}
