package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.InstructorRequest;
import com.ksvtech.drivingschool.dto.InstructorResponse;
import com.ksvtech.drivingschool.service.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    public InstructorResponse create(@Valid @RequestBody InstructorRequest request) {
        return instructorService.create(request);
    }

    @PutMapping("/{id}")
    public InstructorResponse update(@PathVariable Long id,
                                     @Valid @RequestBody InstructorRequest request) {
        return instructorService.update(id, request);
    }

    @GetMapping("/{id}")
    public InstructorResponse get(@PathVariable Long id) {
        return instructorService.get(id);
    }

    @GetMapping
    public List<InstructorResponse> getAll() {
        return instructorService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instructorService.delete(id);
    }
}
