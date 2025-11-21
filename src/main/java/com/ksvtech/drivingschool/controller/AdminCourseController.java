package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.CourseRequest;
import com.ksvtech.drivingschool.dto.CourseResponse;
import com.ksvtech.drivingschool.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("courseForm", new CourseRequest());
        return "admin/courses/form";
    }

    @PostMapping
    public String createCourse(@Valid @ModelAttribute("courseForm") CourseRequest form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/courses/form";
        }

        CourseResponse saved = courseService.createCourse(form);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Course created successfully with ID " + saved.getId()
        );
        return "redirect:/admin/courses";
    }
}
