# KsvTech Driving School Management System

A production-style **Spring Boot 3** application for managing an Indian driving school:

- Student registration (with Aadhaar validation & hashing)
- Course management (LMV/Two-wheeler etc.)
- Certificate generation with **PDF export + QR code**
- Admin-only secure dashboard & APIs

Built with:

- Java 17
- Spring Boot 3
- Spring Data JPA (MySQL)
- Spring Security
- Thymeleaf (for login/dashboard UI)
- ZXing (QR code)
- OpenPDF (PDF generation)
- springdoc-openapi (Swagger UI)

---

## Features

### ✅ Auth & Security

- Form-based login for **admin dashboard** (`/login`)
- **Admin-only** access to all `/api/**` endpoints
- HTTP Basic auth support for calling APIs from tools like Postman
- Default admin user (via data initializer):

  - **Username:** `admin`  
  - **Password:** `admin123`

> You can (and should) change this in production.

---

### ✅ Student Management

- Register new students via API
- Stores **Aadhaar hash** (SHA-256 + salt) — never plain Aadhaar
- Stores last 4 digits for display/printing
- Basic student details: name, mobile, email, address, DOB

**Endpoints:**

- `POST /api/students/register`  
- `GET  /api/students/{id}`

Example request:

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
