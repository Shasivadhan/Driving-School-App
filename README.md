# KsvTech Driving School Management System

A production-style **Spring Boot 3** application for managing an Indian driving school:

- Student registration (with Aadhaar validation & hashing)
- Course management (LMV/Two-wheeler/etc.)
- Certificate generation with **PDF export + QR code**
- Admin-only secure dashboard & UI pages
- REST APIs with Swagger UI for testing

Built with:

- Java 17
- Spring Boot 3 (Web, Security, Data JPA, Validation)
- MySQL 8
- Thymeleaf + Bootstrap
- OpenPDF (PDF)
- ZXing (QR code)
- springdoc-openapi (Swagger UI)

---

## Features

### 🔐 Auth & Security

- Form-based login for **admin** at `/login`
- **Admin-only** access to:
  - `/dashboard`
  - `/admin/**` (students, courses, certificates UI)
  - `/api/**` (REST APIs)
- HTTP Basic Auth enabled for calling APIs from Postman / Swagger UI

**Default admin (dev only):**

- Username: `admin`  
- Password: `admin123`  

> For production, change this user and password in the `DataInitializer` class and secure your instance properly.

---

### 👤 Student Management

- Register students with:
  - Full name, mobile, email, address, DOB
  - Aadhaar number (stored as `hash + last 4`, not plain text)
- Aadhaar is hashed using salted SHA-256 (`AadharUtil`) and never stored raw.
- Last 4 digits are stored for display (PDF/QR).

**REST APIs:**

- `POST /api/students/register`  
- `GET  /api/students/{id}`  

**Example request:**

```json
POST /api/students/register
Content-Type: application/json

{
  "fullName": "Ravi Kumar",
  "mobile": "7794631471",
  "aadharNumber": "779463147122",
  "email": "ravi@example.com",
  "address": "Some Street, Some City",
  "dob": "1994-11-20"
}
