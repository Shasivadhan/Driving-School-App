# Changelog

All notable changes to this project will be documented in this file.

## v3.2.0 – Sessions & Attendance (Latest)

### Added
- Driving **Session** module (entity, service, and admin UI).
- Per-session **attendance management** for students.
- Session linkage with **batches, instructors, and vehicles**.
- Updated admin dashboard and navigation to:
  - Include **Sessions** section and quick links.
  - Fix `/admin/dashboard` routing so login redirects correctly to the admin dashboard.

---

## v3.1.0 – Fees & Payments & Revenue Dashboard

### Added
- **Fees & Payments** module:
  - `Payment` entity, repository, service, and CRUD admin screens.
- Linked payments to **students**, **courses**, and **batches**.

### Dashboard & Finance
- New finance summary on dashboard:
  - **Total revenue**
  - **Current month revenue**
  - **Outstanding dues** (pending fee amount from students)

### UI
- Minor UI tweaks in admin navigation to include **Payments** and improve layout.

---

## v3.0 – Instructors, Vehicles & Batches Module

### Added
- Complete admin modules for:
  - **Instructors**
  - **Vehicles**
  - **Batches**
- Redesigned professional dashboard with:
  - Live stats (students, courses, certificates)
  - Basic analytics charts

### Features
- Manage trainers, training vehicles, and student batches, including:
  - Batch capacity
  - Student assignments to batches

> Built on top of existing student registration and QR-enabled certificate generation.

---

## v2.1 – UI Refresh

### Changed
- Minor UI improvements and visual polish across:
  - Login screen
  - Dashboard

### Notes
- No breaking changes to APIs or database schema.
- Safe upgrade from previous versions.

---

## v2.0-beta – Admin Dashboard & API Enhancements

### Added
- New **admin dashboard UI**.
- Improved **login flow**.
- Integrated **Swagger-based API documentation** (`/swagger-ui/index.html`).

### Certificates & APIs
- Richer certificate generation:
  - Custom PDF layout
  - QR code included
- Better DTO-based request/response models.
- Centralized exception handling.

### Notes
- Prepares the app for more stable production-ready UI in subsequent releases.

---

## v1.0-beta – Initial Driving School Backend

### Added
- Initial Spring Boot backend with MySQL integration.
- **Student registration** APIs.
- Basic **course management**.
- **Admin login**.
- PDF/QR-based **certificate generation**.

### Notes
- First internal beta for testing and feedback before full production rollout.
