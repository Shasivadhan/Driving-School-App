package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.BatchRequest;
import com.ksvtech.drivingschool.dto.BatchResponse;
import com.ksvtech.drivingschool.entity.*;
import com.ksvtech.drivingschool.exception.BusinessException;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public BatchResponse create(BatchRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new NotFoundException("Instructor not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Batch batch = new Batch();
        batch.setName(request.getName());
        batch.setCourse(course);
        batch.setInstructor(instructor);
        batch.setVehicle(vehicle);
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setStartTime(request.getStartTime());
        batch.setEndTime(request.getEndTime());
        batch.setCapacity(request.getCapacity());
        batch.setStatus(
                request.getStatus() == null ? BatchStatus.PLANNED : request.getStatus()
        );

        if (request.getStudentIds() != null && !request.getStudentIds().isEmpty()) {
            Set<Student> students = new HashSet<>(
                    studentRepository.findAllById(request.getStudentIds())
            );
            if (students.size() != request.getStudentIds().size()) {
                throw new BusinessException("One or more students not found");
            }
            if (batch.getCapacity() != null && students.size() > batch.getCapacity()) {
                throw new BusinessException("Student count exceeds batch capacity");
            }
            batch.setStudents(students);
        }

        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    @Transactional
    public BatchResponse update(Long id, BatchRequest request) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Batch not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));
        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new NotFoundException("Instructor not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        batch.setName(request.getName());
        batch.setCourse(course);
        batch.setInstructor(instructor);
        batch.setVehicle(vehicle);
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setStartTime(request.getStartTime());
        batch.setEndTime(request.getEndTime());
        batch.setCapacity(request.getCapacity());
        if (request.getStatus() != null) {
            batch.setStatus(request.getStatus());
        }

        if (request.getStudentIds() != null) {
            Set<Student> students = new HashSet<>(
                    studentRepository.findAllById(request.getStudentIds())
            );
            if (students.size() != request.getStudentIds().size()) {
                throw new BusinessException("One or more students not found");
            }
            if (batch.getCapacity() != null && students.size() > batch.getCapacity()) {
                throw new BusinessException("Student count exceeds batch capacity");
            }
            batch.setStudents(students);
        }

        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    public BatchResponse get(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Batch not found"));
        return toResponse(batch);
    }

    public List<BatchResponse> getAll() {
        return batchRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!batchRepository.existsById(id)) {
            throw new NotFoundException("Batch not found");
        }
        batchRepository.deleteById(id);
    }

    @Transactional
    public BatchResponse addStudents(Long batchId, List<Long> studentIds) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("Batch not found"));

        Set<Student> existing = batch.getStudents();
        List<Student> toAdd = studentRepository.findAllById(studentIds);

        if (toAdd.size() != studentIds.size()) {
            throw new BusinessException("One or more students not found");
        }

        if (batch.getCapacity() != null &&
                existing.size() + toAdd.size() > batch.getCapacity()) {
            throw new BusinessException("Adding these students exceeds batch capacity");
        }

        existing.addAll(toAdd);
        batch.setStudents(existing);

        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    @Transactional
    public BatchResponse removeStudent(Long batchId, Long studentId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new NotFoundException("Batch not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        batch.getStudents().remove(student);
        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    private BatchResponse toResponse(Batch batch) {
        List<Long> studentIds = batch.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        return BatchResponse.builder()
                .id(batch.getId())
                .name(batch.getName())
                .courseId(batch.getCourse().getId())
                .courseName(batch.getCourse().getName())
                .instructorId(batch.getInstructor().getId())
                .instructorName(batch.getInstructor().getFullName())
                .vehicleId(batch.getVehicle().getId())
                .vehicleRegistration(batch.getVehicle().getRegistrationNumber())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .startTime(batch.getStartTime())
                .endTime(batch.getEndTime())
                .capacity(batch.getCapacity())
                .enrolledCount(batch.getStudents().size())
                .status(batch.getStatus())
                .studentIds(studentIds)
                .build();
    }
}
