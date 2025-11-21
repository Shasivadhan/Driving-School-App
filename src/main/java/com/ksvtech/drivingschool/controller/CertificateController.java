package com.ksvtech.drivingschool.controller;

import com.ksvtech.drivingschool.dto.CertificateRequest;
import com.ksvtech.drivingschool.dto.CertificateResponse;
import com.ksvtech.drivingschool.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    public ResponseEntity<CertificateResponse> createCertificate(
            @Valid @RequestBody CertificateRequest request) {
        CertificateResponse response = certificateService.createCertificate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadCertificatePdf(@PathVariable Long id) {
        byte[] pdfBytes = certificateService.generateCertificatePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("certificate-" + id + ".pdf").build());
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<String> verifyCertificate(@PathVariable String certificateNumber) {
        return ResponseEntity.ok("Certificate " + certificateNumber + " is VALID (demo).");
    }
}
