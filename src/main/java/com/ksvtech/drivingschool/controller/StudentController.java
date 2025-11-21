package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.StudentRegistrationRequest;
import com.ksvtech.drivingschool.dto.StudentResponse;
import com.ksvtech.drivingschool.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody StudentRegistrationRequest request) {
        StudentResponse response = studentService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        var student = studentService.getById(id);
        StudentResponse response = StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .mobile(student.getMobile())
                .email(student.getEmail())
                .address(student.getAddress())
                .dob(student.getDob())
                .build();
        return ResponseEntity.ok(response);
    }
}
