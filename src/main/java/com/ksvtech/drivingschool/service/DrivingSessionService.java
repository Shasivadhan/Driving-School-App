package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.SessionRequest;
import com.ksvtech.drivingschool.entity.*;
import com.ksvtech.drivingschool.exception.BusinessException;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DrivingSessionService {

    private final DrivingSessionRepository sessionRepository;
    private final SessionAttendanceRepository attendanceRepository;
    private final BatchRepository batchRepository;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;

    // -------- Sessions --------

    public List<DrivingSession> findAllSessions() {
        return sessionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "sessionDate", "startTime")
        );
    }

    public DrivingSession getSessionOrThrow(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found: " + id));
    }

    public DrivingSession createSession(SessionRequest request) {
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new NotFoundException("Batch not found: " + request.getBatchId()));

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new NotFoundException("Instructor not found: " + request.getInstructorId()));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + request.getVehicleId()));

        DrivingSession session = DrivingSession.builder()
                .batch(batch)
                .instructor(instructor)
                .vehicle(vehicle)
                .sessionDate(request.getSessionDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();

        return sessionRepository.save(session);
    }

    public DrivingSession updateSession(Long id, SessionRequest request) {
        DrivingSession session = getSessionOrThrow(id);

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new NotFoundException("Batch not found: " + request.getBatchId()));

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new NotFoundException("Instructor not found: " + request.getInstructorId()));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + request.getVehicleId()));

        session.setBatch(batch);
        session.setInstructor(instructor);
        session.setVehicle(vehicle);
        session.setSessionDate(request.getSessionDate());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setLocation(request.getLocation());
        session.setNotes(request.getNotes());

        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    // -------- Attendance --------

    public List<SessionAttendance> getAttendanceForSession(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId);
    }

    public SessionAttendance addAttendance(Long sessionId,
                                           Long studentId,
                                           AttendanceStatus status,
                                           String remarks) {

        DrivingSession session = getSessionOrThrow(sessionId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));

        if (attendanceRepository.existsBySessionIdAndStudentId(sessionId, studentId)) {
            throw new BusinessException("Attendance already recorded for this student in this session.");
        }

        SessionAttendance attendance = SessionAttendance.builder()
                .session(session)
                .student(student)
                .status(status)
                .remarks(remarks)
                .build();

        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
}
