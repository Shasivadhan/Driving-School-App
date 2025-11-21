package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.dto.CertificateRequest;
import com.ksvtech.drivingschool.dto.CertificateResponse;
import com.ksvtech.drivingschool.entity.Certificate;
import com.ksvtech.drivingschool.entity.Course;
import com.ksvtech.drivingschool.entity.Student;
import com.ksvtech.drivingschool.exception.BusinessException;
import com.ksvtech.drivingschool.exception.NotFoundException;
import com.ksvtech.drivingschool.repository.CertificateRepository;
import com.ksvtech.drivingschool.repository.CourseRepository;
import com.ksvtech.drivingschool.repository.StudentRepository;
import com.ksvtech.drivingschool.util.AadharUtil;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AadharUtil aadharUtil;
    private final QRCodeService qrCodeService;

    @Transactional
    public CertificateResponse createCertificate(CertificateRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        String hash = aadharUtil.hashAadhar(request.getAadharNumber());
        if (!hash.equals(student.getAadharHash())) {
            throw new BusinessException("Aadhaar verification failed for this student");
        }

        String certNumber = generateCertificateNumber();
        String verificationUrl =
                "https://your-domain.com/api/certificates/verify/" + certNumber;

        Certificate certificate = Certificate.builder()
                .certificateNumber(certNumber)
                .student(student)
                .course(course)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(2))
                .qrData(verificationUrl)
                .aadharHash(hash)
                .build();

        certificate = certificateRepository.save(certificate);

        return CertificateResponse.builder()
                .id(certificate.getId())
                .certificateNumber(certNumber)
                .studentId(student.getId())
                .studentName(student.getFullName())
                .courseName(course.getName())
                .issueDate(certificate.getIssueDate())
                .expiryDate(certificate.getExpiryDate())
                .qrData(verificationUrl)
                .build();
    }

    private String generateCertificateNumber() {
        return "DS-" + LocalDate.now().getYear() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Certificate getCertificate(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Certificate not found"));
    }

    public byte[] generateCertificatePdf(Long certificateId) {
        Certificate cert = getCertificate(certificateId);
        Student student = cert.getStudent();
        Course course = cert.getCourse();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("Driving School Completion Certificate", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Certificate No: " + cert.getCertificateNumber()));
            document.add(new Paragraph("Issue Date : " + cert.getIssueDate()));
            document.add(new Paragraph("Expiry Date: " + cert.getExpiryDate()));
            document.add(new Paragraph(" "));

            Font bodyFont = new Font(Font.HELVETICA, 12);
            Paragraph body = new Paragraph(
                    "This is to certify that " + student.getFullName() +
                            " has successfully completed the course " + course.getName() +
                            " at our driving school.", bodyFont);
            body.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(body);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Student ID: " + student.getId()));
            document.add(new Paragraph("Mobile    : " + student.getMobile()));
            document.add(new Paragraph("Aadhaar (last 4 digits): **** **** " + student.getAadharLast4()));
            document.add(new Paragraph("Address   : " + (student.getAddress() != null ? student.getAddress() : "")));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Scan the QR code below to verify this certificate."));

            byte[] qrBytes = qrCodeService.generateQRCodeImage(cert.getQrData(), 200, 200);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.setAlignment(Image.ALIGN_CENTER);
            document.add(qrImage);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Authorized Signatory", bodyFont));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
