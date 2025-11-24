# KsvTech Driving School Management

A Spring Boot 3 / Thymeleaf web application to manage a driving school’s day-to-day operations:

- Student registration & course assignment  
- Instructors, vehicles & batches  
- Certificates with PDF + QR code  
- Fees & payments tracking  
- Admin dashboard with basic analytics  
- REST APIs documented via Swagger UI

---

## Tech Stack

- **Backend**
  - Java **17**
  - Spring Boot **3.3.4**
  - Spring MVC (Web)
  - Spring Data JPA (Hibernate)
  - Spring Security 6
  - Spring Validation
- **Frontend**
  - Thymeleaf templates
  - Bootstrap 5 + Bootstrap Icons
  - Chart.js (dashboard charts)
- **Database**
  - MySQL (via `mysql-connector-j`)
- **Other Libraries**
  - [springdoc-openapi-starter-webmvc-ui](https://springdoc.org/) – Swagger UI
  - OpenPDF – PDF generation
  - ZXing – QR code generation
  - Lombok – boilerplate reduction

---

## Main Features

### 1. Authentication & Security

- Form login using Spring Security.
- Protected admin area under `/admin/**`.
- API endpoints protected; can be tested via Swagger UI with authentication.
- Default login page and post-login redirection to dashboard.

> **Login URLs**
> - `GET /login` – login page  
> - `POST /login` – form submit  
> - `GET /logout` – logout

> **Default demo users**  
> (Check `SecurityConfig` class for the latest values)
> - `admin` / `admin123` – ROLE_ADMIN  
> - `staff` / `staff123` – ROLE_STAFF  

You should change these for production.

---

### 2. Admin Dashboard

**URL:** `GET /dashboard`  
**Controller:** `DashboardController`

Dashboard shows:

- **Key metrics**
  - Total **Students**
  - Total **Courses**
  - Total **Certificates issued**

- **Finance summary (from Payment data)**
  - Total revenue *all time*
  - Total revenue *this month*
  - Total *outstanding due*

- **Charts (Chart.js)**
  - Certificates per **course**
  - Certificates per **month**
  - Revenue per **month** (using aggregated Payment data)

Dashboard also has quick action buttons:

- Manage Students → `/admin/students`
- Manage Courses → `/admin/courses`
- Manage Certificates → `/admin/certificates`
- Manage Instructors → `/admin/instructors`
- Manage Vehicles → `/admin/vehicles`
- Manage Batches → `/admin/batches`
- Manage Payments → `/admin/payments`
- Open Swagger UI → `/swagger-ui/index.html`

---

### 3. Student Management

**UI:**  
- List / search / view / edit / delete students  
- Register a new student (with basic validation like Aadhaar, DOB, contact details)

**Typical admin URLs:**

- `GET /admin/students` – list
- `GET /admin/students/new` – new student form
- `POST /admin/students` – create
- `GET /admin/students/{id}` – view
- `GET /admin/students/{id}/edit` – edit form
- `POST /admin/students/{id}` – update
- `POST /admin/students/{id}/delete` – delete

**API (example):**

- `POST /api/students/register` – register student
- `GET /api/students/{id}` – get a student
- (More endpoints visible in Swagger UI)

---

### 4. Course Management

**UI URLs (admin):**

- `GET /admin/courses` – list
- `GET /admin/courses/new` – create form
- `POST /admin/courses` – save
- `GET /admin/courses/{id}/edit` – edit form
- `POST /admin/courses/{id}` – update
- `POST /admin/courses/{id}/delete` – delete

Typical fields:

- Name (e.g. LMV, MCWG, HMV)
- Duration
- Fee amount
- Description

**API endpoints (examples):**

- `POST /api/courses` – create course  
- `GET /api/courses` – list all courses  

---

### 5. Certificate Management

**What it does**

- Issue completion certificates when a student finishes a course.
- Generate **PDF certificates** using **OpenPDF**.
- Embed a **QR code** in the certificate using **ZXing**, linking back to a verification endpoint (e.g., via certificate ID).

**UI URLs (admin):**

- `GET /admin/certificates` – list certificates
- `GET /admin/certificates/new` – generate certificate form
- `POST /admin/certificates` – issue certificate
- `GET /admin/certificates/{id}` – view certificate details

**API endpoints (examples):**

- `POST /api/certificates` – generate a new certificate
- `GET /api/certificates/{id}/pdf` – download certificate as PDF

Certificates are also aggregated for dashboard analytics:

- Certificates per course
- Certificates per month (YYYY-MM)

---

### 6. Instructors, Vehicles & Batches

These are the core operational modules that connect students with instructors & vehicles via batches.

#### Instructors

- Store instructor profile:
  - Name, phone, license/ID, experience, etc.
- Assign instructors to **batches**.

**UI:** `/admin/instructors`

#### Vehicles

- Manage vehicles used for training:
  - Registration number
  - Type (LMV / MCWG / etc.)
  - Status (ACTIVE / IN_MAINTENANCE / etc.)

**UI:** `/admin/vehicles`

#### Batches

A **Batch** connects:

- A **Course**
- A **Vehicle**
- An **Instructor**
- A set of **Students**
- Schedule info (start date, etc.)

Features:

- Create / edit batches.
- Add multiple students to a batch.
- View which students belong to which batch.

**UI:** `/admin/batches`

Implementation-wise, the **Batch** entity uses a relationship to:

- `Course`
- `Instructor`
- `Vehicle`
- `Set<Student>` (students in that batch)

---

### 7. Fees & Payments Module

This is the latest major addition.

#### Payment Entity & Status

`Payment` is linked to a `Student` and optionally to a `Course` or `Batch`.

Key fields:

- `student` – linked student
- `course` – (optional) linked course
- `batch` – (optional) linked batch
- `amountDue` – total fee expected
- `amountPaid` – amount received
- `status` – enum `PaymentStatus`:
  - `PENDING`
  - `PARTIALLY_PAID`
  - `PAID`
  - `OVERDUE`
- `paymentDate` – last payment timestamp
- `createdAt` – record creation time
- `note` – optional remarks

`PaymentStatus` is a simple enum used for UI badges & filtering.

#### Payment Repository & Service

- `PaymentRepository`
  - Uses Spring Data JPA.
  - Includes aggregate queries to compute:
    - Total revenue all time
    - Total revenue for current month
    - Outstanding balances
    - Revenue per month (for charts)

- `PaymentService`
  - Provides `DashboardFinanceSummary` DTO:
    - `totalRevenueAllTime`
    - `totalRevenueThisMonth`
    - `totalOutstandingDue`
  - Provides monthly revenue data for dashboard charts.

#### Payment Admin UI

**Controller:** e.g. `PaymentAdminController`  

**Typical URLs:**

- `GET /admin/payments` – list all payments
- `GET /admin/payments/new` – new payment form
- `POST /admin/payments` – create payment
- `GET /admin/payments/{id}/edit` – edit payment
- `POST /admin/payments/{id}` – update payment
- `POST /admin/payments/{id}/delete` – delete payment

The list view shows:

- Student name
- Course (if any)
- Amount due / paid / balance
- Status badge (color-coded)
- Payment date (formatted)
- Actions (edit/delete)

The Payment data feeds into dashboard **finance cards** and **revenue charts**.

---

### 8. API Documentation (Swagger UI)

The project uses `springdoc-openapi-starter-webmvc-ui`.

**URL:**  
`/swagger-ui/index.html`

From here you can:

- Browse all REST endpoints (students, courses, certificates, payments, etc.)
- Send test requests (with authentication)
- See request/response models

Swagger is mainly for **testing / integration**. Real users use the **Thymeleaf admin UI** (`/login`, `/dashboard`, `/admin/...`).

---

## Project Structure (High Level)

```text
src/main/java/com/ksvtech/drivingschool
├─ config
│  └─ SecurityConfig.java
├─ controller
│  ├─ AuthController.java
│  ├─ DashboardController.java
│  ├─ StudentController.java
│  ├─ CourseController.java
│  ├─ CertificateController.java
│  ├─ InstructorController.java
│  ├─ VehicleController.java
│  ├─ BatchController.java
│  └─ PaymentAdminController.java
├─ dto
│  ├─ AuthResponse.java
│  └─ DashboardFinanceSummary.java
├─ entity
│  ├─ Student.java
│  ├─ Course.java
│  ├─ Certificate.java
│  ├─ Instructor.java
│  ├─ Vehicle.java
│  ├─ Batch.java
│  ├─ Payment.java
│  └─ PaymentStatus.java
├─ repository
│  ├─ StudentRepository.java
│  ├─ CourseRepository.java
│  ├─ CertificateRepository.java
│  ├─ InstructorRepository.java
│  ├─ VehicleRepository.java
│  ├─ BatchRepository.java
│  └─ PaymentRepository.java
└─ service
   ├─ CertificateService.java
   └─ PaymentService.java
