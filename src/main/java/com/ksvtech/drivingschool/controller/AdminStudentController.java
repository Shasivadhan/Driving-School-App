package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.StudentRegistrationRequest;
import com.ksvtech.drivingschool.dto.StudentResponse;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "admin/students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("studentForm", new StudentRegistrationRequest());
        return "admin/students/form";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute("studentForm") StudentRegistrationRequest form,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/students/form";
        }

        StudentResponse saved = studentService.registerStudent(form);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Student created successfully with ID " + saved.getId()
        );
        return "redirect:/admin/students";
    }
}
