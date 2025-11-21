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
import com.lowagie.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
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
        LocalDate issueDate = LocalDate.now();
        LocalDate expiryDate = issueDate.plusYears(2);

        // For privacy, only last 4 digits of Aadhaar in QR
        String aadhaarDisplay = "XXXX XXXX " + student.getAadharLast4();

        // Text that will be encoded into the QR code
        String qrText =
                "KSVTECH DRIVING SCHOOL\n" +
                        "Certificate No : " + certNumber + "\n" +
                        "Student ID     : " + student.getId() + "\n" +
                        "Student Name   : " + student.getFullName() + "\n" +
                        "Course         : " + course.getName() + "\n" +
                        "Issue Date     : " + issueDate + "\n" +
                        "Expiry Date    : " + expiryDate + "\n" +
                        "Aadhaar        : " + aadhaarDisplay;

        Certificate certificate = Certificate.builder()
                .certificateNumber(certNumber)
                .student(student)
                .course(course)
                .issueDate(issueDate)
                .expiryDate(expiryDate)
                .qrData(qrText)          // store details, not URL
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
                .qrData(qrText)
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
            // A4 landscape, with margins
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            PdfContentByte canvas = writer.getDirectContent();
            Rectangle pageSize = document.getPageSize();

            // ====== BORDERS / FRAME ======
            // Outer border
            Rectangle outer = new Rectangle(
                    document.leftMargin() / 2,
                    document.bottomMargin() / 2,
                    pageSize.getRight() - document.rightMargin() / 2,
                    pageSize.getTop() - document.topMargin() / 2
            );
            outer.setBorder(Rectangle.BOX);
            outer.setBorderWidth(4);
            outer.setBorderColor(new Color(0, 51, 102)); // dark blue
            canvas.rectangle(outer);

            // Inner border
            Rectangle inner = new Rectangle(
                    outer.getLeft() + 10,
                    outer.getBottom() + 10,
                    outer.getRight() - 10,
                    outer.getTop() - 10
            );
            inner.setBorder(Rectangle.BOX);
            inner.setBorderWidth(1.5f);
            inner.setBorderColor(new Color(153, 153, 153)); // grey
            canvas.rectangle(inner);

            // ====== FONTS ======
            Font titleFont = new Font(Font.HELVETICA, 26, Font.BOLD, new Color(0, 51, 102));
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font subHeaderFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
            Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font smallFont = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.DARK_GRAY);

            // ====== HEADER (School name etc.) ======
            Paragraph schoolName = new Paragraph("KSVTECH DRIVING SCHOOL", titleFont);
            schoolName.setAlignment(Element.ALIGN_CENTER);
            schoolName.setSpacingBefore(20f);
            document.add(schoolName);

            Paragraph schoolAddress = new Paragraph(
                    "Near RTO Office, Your City, Your State, India",
                    subHeaderFont
            );
            schoolAddress.setAlignment(Element.ALIGN_CENTER);
            schoolAddress.setSpacingAfter(10f);
            document.add(schoolAddress);

            Paragraph certTitle = new Paragraph("CERTIFICATE OF COMPLETION", headerFont);
            certTitle.setAlignment(Element.ALIGN_CENTER);
            certTitle.setSpacingBefore(10f);
            certTitle.setSpacingAfter(20f);
            document.add(certTitle);

            // ====== INTRO TEXT ======
            Paragraph intro = new Paragraph(
                    "This is to certify that the candidate mentioned below has successfully " +
                            "completed the prescribed driving training course at KsvTech Driving School.",
                    valueFont
            );
            intro.setAlignment(Element.ALIGN_CENTER);
            intro.setLeading(16f);
            intro.setSpacingAfter(25f);
            document.add(intro);

            // ====== DETAILS TABLE ======
            PdfPTable detailsTable = new PdfPTable(4); // label / value / label / value
            detailsTable.setWidthPercentage(90);
            detailsTable.setSpacingBefore(10f);
            detailsTable.setWidths(new float[]{1.4f, 2.6f, 1.4f, 2.6f});

            detailsTable.addCell(makeLabelCell("Student Name", labelFont));
            detailsTable.addCell(makeValueCell(student.getFullName(), valueFont));

            detailsTable.addCell(makeLabelCell("Student ID", labelFont));
            detailsTable.addCell(makeValueCell(String.valueOf(student.getId()), valueFont));

            detailsTable.addCell(makeLabelCell("Course", labelFont));
            detailsTable.addCell(makeValueCell(course.getName(), valueFont));

            detailsTable.addCell(makeLabelCell("Course Code", labelFont));
            detailsTable.addCell(makeValueCell(course.getCode(), valueFont));

            detailsTable.addCell(makeLabelCell("Issue Date", labelFont));
            detailsTable.addCell(makeValueCell(String.valueOf(cert.getIssueDate()), valueFont));

            detailsTable.addCell(makeLabelCell("Expiry Date", labelFont));
            detailsTable.addCell(makeValueCell(String.valueOf(cert.getExpiryDate()), valueFont));

            detailsTable.addCell(makeLabelCell("Mobile", labelFont));
            detailsTable.addCell(makeValueCell(student.getMobile(), valueFont));

            detailsTable.addCell(makeLabelCell("Aadhaar (last 4)", labelFont));
            detailsTable.addCell(makeValueCell("XXXX XXXX " + student.getAadharLast4(), valueFont));

            document.add(detailsTable);

            document.add(Chunk.NEWLINE);

            Paragraph note = new Paragraph(
                    "Note: This certificate is valid only when verified online using the QR code.",
                    smallFont
            );
            note.setAlignment(Element.ALIGN_CENTER);
            note.setSpacingAfter(20f);
            document.add(note);

            // ====== BOTTOM SECTION: signatures + QR ======
            PdfPTable bottomTable = new PdfPTable(3);
            bottomTable.setWidthPercentage(90);
            bottomTable.setWidths(new float[]{2.5f, 2.5f, 2.0f});
            bottomTable.setSpacingBefore(30f);

            // Left: Student signature
            PdfPCell studentCell = new PdfPCell();
            studentCell.setBorder(Rectangle.NO_BORDER);
            studentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            studentCell.addElement(new Paragraph("__________________________", valueFont));
            studentCell.addElement(new Paragraph("Student Signature", labelFont));
            bottomTable.addCell(studentCell);

            // Middle: Authorized Signatory
            PdfPCell authCell = new PdfPCell();
            authCell.setBorder(Rectangle.NO_BORDER);
            authCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            authCell.addElement(new Paragraph("__________________________", valueFont));
            authCell.addElement(new Paragraph("Authorized Signatory", labelFont));
            bottomTable.addCell(authCell);

            // Right: QR Code
            PdfPCell qrCell = new PdfPCell();
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            byte[] qrBytes = qrCodeService.generateQRCodeImage(cert.getQrData(), 120, 120);
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(100, 100);
            qrImage.setAlignment(Image.ALIGN_CENTER);

            qrCell.addElement(qrImage);
            qrCell.addElement(new Paragraph("Scan to verify", smallFont));
            bottomTable.addCell(qrCell);

            document.add(bottomTable);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    // ===== Helper methods for table cells =====

    private PdfPCell makeLabelCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6f);
        return cell;
    }

    private PdfPCell makeValueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(6f);
        return cell;
    }
}
