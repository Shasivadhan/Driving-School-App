package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.BatchRequest;
import com.ksvtech.drivingschool.dto.BatchResponse;
import com.ksvtech.drivingschool.service.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public BatchResponse create(@Valid @RequestBody BatchRequest request) {
        return batchService.create(request);
    }

    @PutMapping("/{id}")
    public BatchResponse update(@PathVariable Long id,
                                @Valid @RequestBody BatchRequest request) {
        return batchService.update(id, request);
    }

    @GetMapping("/{id}")
    public BatchResponse get(@PathVariable Long id) {
        return batchService.get(id);
    }

    @GetMapping
    public List<BatchResponse> getAll() {
        return batchService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        batchService.delete(id);
    }

    @PostMapping("/{id}/students")
    public BatchResponse addStudents(@PathVariable Long id,
                                     @RequestBody List<Long> studentIds) {
        return batchService.addStudents(id, studentIds);
    }

    @DeleteMapping("/{batchId}/students/{studentId}")
    public BatchResponse removeStudent(@PathVariable Long batchId,
                                       @PathVariable Long studentId) {
        return batchService.removeStudent(batchId, studentId);
    }
}
