package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.InstructorRequest;
import com.ksvtech.drivingschool.dto.InstructorResponse;
import com.ksvtech.drivingschool.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/instructors")
@RequiredArgsConstructor
public class AdminInstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public String list(Model model) {
        List<InstructorResponse> instructors = instructorService.getAll();
        model.addAttribute("instructors", instructors);
        return "admin/instructors/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("instructor", new InstructorRequest());
        model.addAttribute("instructorId", null);
        return "admin/instructors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("instructor") InstructorRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("instructorId", null);
            return "admin/instructors/form";
        }
        instructorService.create(form);
        return "redirect:/admin/instructors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        InstructorResponse resp = instructorService.get(id);
        InstructorRequest form = new InstructorRequest();
        form.setFullName(resp.getFullName());
        form.setMobile(resp.getMobile());
        form.setEmail(resp.getEmail());
        form.setLicenseNumber(resp.getLicenseNumber());
        form.setLicenseType(resp.getLicenseType());
        form.setExperienceYears(resp.getExperienceYears());
        form.setJoinedDate(resp.getJoinedDate());
        form.setActive(resp.isActive());

        model.addAttribute("instructor", form);
        model.addAttribute("instructorId", id);
        return "admin/instructors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("instructor") InstructorRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("instructorId", id);
            return "admin/instructors/form";
        }
        instructorService.update(id, form);
        return "redirect:/admin/instructors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        instructorService.delete(id);
        return "redirect:/admin/instructors";
    }
}
