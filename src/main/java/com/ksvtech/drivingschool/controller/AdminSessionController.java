package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.SessionRequest;
import com.ksvtech.drivingschool.entity.AttendanceStatus;
import com.ksvtech.drivingschool.entity.DrivingSession;
import com.ksvtech.drivingschool.entity.SessionAttendance;
import com.ksvtech.drivingschool.repository.BatchRepository;
import com.ksvtech.drivingschool.repository.InstructorRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.repository.VehicleRepository;
import com.ksvtech.drivingschool.service.DrivingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/sessions")
@RequiredArgsConstructor
public class AdminSessionController {

    private final DrivingSessionService sessionService;
    private final BatchRepository batchRepository;
    private final InstructorRepository instructorRepository;
    private final VehicleRepository vehicleRepository;
    private final StudentRepository studentRepository;

    // ------ Sessions list ------

    @GetMapping
    public String listSessions(Model model) {
        List<DrivingSession> sessions = sessionService.findAllSessions();
        model.addAttribute("sessions", sessions);
        return "admin/sessions/list";
    }

    // ------ Create ------

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sessionForm", new SessionRequest());
        model.addAttribute("sessionId", null);
        addCommonLookups(model);
        return "admin/sessions/form";
    }

    @PostMapping
    public String createSession(@ModelAttribute("sessionForm") SessionRequest form,
                                RedirectAttributes redirectAttributes) {
        sessionService.createSession(form);
        redirectAttributes.addFlashAttribute("successMessage", "Session created successfully.");
        return "redirect:/admin/sessions";
    }

    // ------ Edit ------

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        DrivingSession session = sessionService.getSessionOrThrow(id);

        SessionRequest form = new SessionRequest();
        form.setBatchId(session.getBatch().getId());
        form.setInstructorId(session.getInstructor().getId());
        form.setVehicleId(session.getVehicle().getId());
        form.setSessionDate(session.getSessionDate());
        form.setStartTime(session.getStartTime());
        form.setEndTime(session.getEndTime());
        form.setLocation(session.getLocation());
        form.setNotes(session.getNotes());

        model.addAttribute("sessionForm", form);
        model.addAttribute("sessionId", id);
        addCommonLookups(model);
        return "admin/sessions/form";
    }

    @PostMapping("/{id}")
    public String updateSession(@PathVariable Long id,
                                @ModelAttribute("sessionForm") SessionRequest form,
                                RedirectAttributes redirectAttributes) {
        sessionService.updateSession(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Session updated successfully.");
        return "redirect:/admin/sessions";
    }

    // ------ Delete ------

    @PostMapping("/{id}/delete")
    public String deleteSession(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        sessionService.deleteSession(id);
        redirectAttributes.addFlashAttribute("successMessage", "Session deleted.");
        return "redirect:/admin/sessions";
    }

    // ------ Attendance UI ------

    @GetMapping("/{id}/attendance")
    public String manageAttendance(@PathVariable Long id, Model model) {
        DrivingSession session = sessionService.getSessionOrThrow(id);
        List<SessionAttendance> attendanceList = sessionService.getAttendanceForSession(id);

        model.addAttribute("session", session);
        model.addAttribute("sessionId", id);          // <--- IMPORTANT
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("statuses", AttendanceStatus.values());

        return "admin/sessions/attendance";
    }


    @PostMapping("/{id}/attendance")
    public String addAttendance(@PathVariable Long id,
                                @RequestParam(value = "studentId", required = false) Long studentId,
                                @RequestParam(value = "status", required = false) String status,
                                @RequestParam(value = "remarks", required = false) String remarks,
                                RedirectAttributes redirectAttributes) {

        // Validate inputs to avoid 400 Bad Request
        if (studentId == null || status == null || status.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Please select both a student and a status.");
            return "redirect:/admin/sessions/" + id + "/attendance";
        }

        AttendanceStatus enumStatus;
        try {
            enumStatus = AttendanceStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid attendance status selected.");
            return "redirect:/admin/sessions/" + id + "/attendance";
        }

        try {
            sessionService.addAttendance(id, studentId, enumStatus, remarks);
            redirectAttributes.addFlashAttribute("successMessage", "Attendance saved.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    ex.getMessage() != null ? ex.getMessage() : "Could not save attendance.");
        }

        return "redirect:/admin/sessions/" + id + "/attendance";
    }

    @PostMapping("/attendance/{attendanceId}/delete")
    public String deleteAttendance(@PathVariable Long attendanceId,
                                   @RequestParam("sessionId") Long sessionId,
                                   RedirectAttributes redirectAttributes) {
        sessionService.deleteAttendance(attendanceId);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance record deleted.");
        return "redirect:/admin/sessions/" + sessionId + "/attendance";
    }

    // ------ helpers ------

    private void addCommonLookups(Model model) {
        model.addAttribute("batches", batchRepository.findAll());
        model.addAttribute("instructors", instructorRepository.findAll());
        model.addAttribute("vehicles", vehicleRepository.findAll());
    }
}
