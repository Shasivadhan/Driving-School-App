package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.CourseRequest;
import com.ksvtech.drivingschool.dto.CourseResponse;
import com.ksvtech.drivingschool.entity.Course;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        Course course = Course.builder()
                .code(request.getCode())
                .name(request.getName())
                .durationDays(request.getDurationDays())
                .fees(request.getFees())
                .build();

        Course saved = courseRepository.save(course);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        return toResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setDurationDays(request.getDurationDays());
        course.setFees(request.getFees());

        Course updated = courseRepository.save(course);
        return toResponse(updated);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .durationDays(course.getDurationDays())
                .fees(course.getFees())
                .build();
    }
}
