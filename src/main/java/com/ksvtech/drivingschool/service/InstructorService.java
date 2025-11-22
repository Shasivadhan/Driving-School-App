package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.InstructorRequest;
import com.ksvtech.drivingschool.dto.InstructorResponse;
import com.ksvtech.drivingschool.entity.Instructor;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorResponse create(InstructorRequest request) {
        Instructor instructor = Instructor.builder()
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .email(request.getEmail())
                .licenseNumber(request.getLicenseNumber())
                .licenseType(request.getLicenseType())
                .experienceYears(request.getExperienceYears())
                .joinedDate(request.getJoinedDate())
                .active(request.getActive() == null ? true : request.getActive())
                .notes(request.getNotes())
                .build();

        instructor = instructorRepository.save(instructor);
        return toResponse(instructor);
    }

    public InstructorResponse update(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Instructor not found"));

        instructor.setFullName(request.getFullName());
        instructor.setMobile(request.getMobile());
        instructor.setEmail(request.getEmail());
        instructor.setLicenseNumber(request.getLicenseNumber());
        instructor.setLicenseType(request.getLicenseType());
        instructor.setExperienceYears(request.getExperienceYears());
        instructor.setJoinedDate(request.getJoinedDate());
        instructor.setActive(request.getActive() == null ? instructor.isActive() : request.getActive());
        instructor.setNotes(request.getNotes());

        instructor = instructorRepository.save(instructor);
        return toResponse(instructor);
    }

    public InstructorResponse get(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Instructor not found"));
        return toResponse(instructor);
    }

    public List<InstructorResponse> getAll() {
        return instructorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!instructorRepository.existsById(id)) {
            throw new NotFoundException("Instructor not found");
        }
        instructorRepository.deleteById(id);
    }

    private InstructorResponse toResponse(Instructor instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .fullName(instructor.getFullName())
                .mobile(instructor.getMobile())
                .email(instructor.getEmail())
                .licenseNumber(instructor.getLicenseNumber())
                .licenseType(instructor.getLicenseType())
                .experienceYears(instructor.getExperienceYears())
                .joinedDate(instructor.getJoinedDate())
                .active(instructor.isActive())
                .build();
    }
}
