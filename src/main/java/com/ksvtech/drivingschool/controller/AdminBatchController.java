package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.BatchRequest;
import com.ksvtech.drivingschool.dto.BatchResponse;
import com.ksvtech.drivingschool.entity.BatchStatus;
import com.ksvtech.drivingschool.entity.Course;
import com.ksvtech.drivingschool.entity.Instructor;
import com.ksvtech.drivingschool.entity.Student;
import com.ksvtech.drivingschool.entity.Vehicle;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.InstructorRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.repository.VehicleRepository;
import com.ksvtech.drivingschool.service.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/batches")
@RequiredArgsConstructor
public class AdminBatchController {

    private final BatchService batchService;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;

    @GetMapping
    public String list(Model model) {
        List<BatchResponse> batches = batchService.getAll();
        model.addAttribute("batches", batches);
        return "admin/batches/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        BatchRequest form = new BatchRequest();
        form.setStatus(BatchStatus.PLANNED);

        populateReferenceData(model);
        model.addAttribute("batch", form);
        model.addAttribute("batchId", null);
        return "admin/batches/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("batch") BatchRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateReferenceData(model);
            model.addAttribute("batchId", null);
            return "admin/batches/form";
        }
        batchService.create(form);
        return "redirect:/admin/batches";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BatchResponse resp = batchService.get(id);

        BatchRequest form = new BatchRequest();
        form.setName(resp.getName());
        form.setCourseId(resp.getCourseId());
        form.setInstructorId(resp.getInstructorId());
        form.setVehicleId(resp.getVehicleId());
        form.setStartDate(resp.getStartDate());
        form.setEndDate(resp.getEndDate());
        form.setStartTime(resp.getStartTime());
        form.setEndTime(resp.getEndTime());
        form.setCapacity(resp.getCapacity());
        form.setStatus(resp.getStatus());
        form.setStudentIds(resp.getStudentIds());

        populateReferenceData(model);
        model.addAttribute("batch", form);
        model.addAttribute("batchId", id);
        return "admin/batches/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("batch") BatchRequest form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            populateReferenceData(model);
            model.addAttribute("batchId", id);
            return "admin/batches/form";
        }
        batchService.update(id, form);
        return "redirect:/admin/batches";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        batchService.delete(id);
        return "redirect:/admin/batches";
    }

    private void populateReferenceData(Model model) {
        List<Course> courses = courseRepository.findAll();
        List<Instructor> instructors = instructorRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Student> students = studentRepository.findAll();

        model.addAttribute("courses", courses);
        model.addAttribute("instructors", instructors);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("students", students);
        model.addAttribute("batchStatuses", BatchStatus.values());
    }
}
