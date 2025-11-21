package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.StudentRegistrationRequest;
import com.ksvtech.drivingschool.dto.StudentResponse;
import com.ksvtech.drivingschool.entity.Student;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.util.AadharUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final AadharUtil aadharUtil;   // ✅ only AadharUtil now

    @Transactional
    public StudentResponse registerStudent(StudentRegistrationRequest request) {

        // ❌ REMOVE userService & login creation
        // User user = userService.createStudentUser(request.getUsername(), request.getPassword());

        String hash = aadharUtil.hashAadhar(request.getAadharNumber());
        String last4 = aadharUtil.last4(request.getAadharNumber());

        Student student = Student.builder()
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .aadharHash(hash)
                .aadharLast4(last4)
                .email(request.getEmail())
                .address(request.getAddress())
                .dob(request.getDob())
                // ❌ no .user(user)
                .build();

        Student saved = studentRepository.save(student);

        return StudentResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .mobile(saved.getMobile())
                .email(saved.getEmail())
                .address(saved.getAddress())
                .dob(saved.getDob())
                .build();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }
}
