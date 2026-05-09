# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a RuoYi-Vue 3.9.2 based psychological counseling management system with a Spring Boot backend and Vue 2 frontend. The system supports three user roles: students, counselors, and administrators.

## Build Commands

### Backend (Java 17, Spring Boot 4.0.3)
```bash
mvn clean package                    # Full build
mvn clean install                    # Build and install to local repo
mvn test                             # Run tests
mvn -pl ruoyi-admin -am package      # Build only admin module and dependencies
```

### Frontend (Vue 2, Node >= 8.9)
```bash
cd ruoyi-ui
npm install                           # Install dependencies
npm run dev                          # Start dev server (port 80)
npm run build:prod                    # Production build
npm run build:stage                   # Pre-release build
```

### Service Management
```bash
./ry.sh start|stop|restart|status   # Linux
ry.bat                               # Windows (starts ruoyi-admin.jar)
```

## Architecture

### Backend (Maven Multi-Module)
```
ruoyi/                    # Root pom - manages versions and modules
├── ruoyi-admin/         # Entry point - Spring Boot startup, Web controllers
├── ruoyi-framework/     # Security config, JWT filter, interceptor setup
├── ruoyi-system/        # Business logic, domain entities, mapper interfaces
│   └── domain/          # psy_* entities: PsyStudentProfile, PsyCounselorProfile,
│                        # PsyAppointment, PsyEmotionRecord, PsyScale, etc.
│   └── service/         # IPsyCenterService + PsyCenterServiceImpl (main service)
├── ruoyi-common/        # Utilities, annotations, constants, response wrappers
├── ruoyi-quartz/        # Scheduled tasks
└── ruoyi-generator/     # Code generation templates
```

**Key backend patterns:**
- Controllers return `AjaxResult` (defined in ruoyi-common)
- Service layer uses `IPsyCenterService` interface in ruoyi-system
- Mapper XML files live in `ruoyi-system/src/main/resources/mapper/system/`
- Redis session auth via Spring Security

### Frontend (Vue 2 + Element UI)
```
ruoyi-ui/src/
├── api/psych/           # center.js - all psychological module API calls
├── views/psych/         # Role-based pages:
│   ├── student/         # confession, assessment, appointment, profile
│   ├── counselor/      # index, warning
│   └── admin/          # index, scale, warning
└── layout/components/  # Sidebar, TopBar customization
```

**Key frontend patterns:**
- API proxy: `/dev-api` → `http://localhost:8080` (configured in vue.config.js)
- Auth token stored in cookies (`cookies.get('token')`)
- Element UI components for all admin-style forms

## Configuration Files

| Purpose | Path |
|---------|------|
| Database connection | `ruoyi-admin/src/main/resources/application-druid.yml` |
| Redis & app config | `ruoyi-admin/src/main/resources/application.yml` |
| Frontend env vars | `ruoyi-ui/.env.development`, `.env.production`, `.env.staging` |
| Frontend proxy | `ruoyi-ui/vue.config.js` |

## Database

- MySQL default: `ry-vue` on `localhost:3306`
- Init scripts in `sql/`:
  - `ry_20260417.sql` - system tables
  - `psychological_counseling_v1.sql` - psychological module tables (psy_*)
  - `quartz.sql` - scheduled tasks tables

## API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`
- Druid monitor: `http://localhost:8080/druid/` (user: ruoyi, pass: 123456)

## Psychological Module Entities (psy_*)

Main domain classes in `ruoyi-system/src/main/java/com/ruoyi/system/domain/`:
- `PsyStudentProfile` - student profiles
- `PsyCounselorProfile` - counselor profiles
- `PsyAppointment` - appointment bookings
- `PsyEmotionRecord` - emotion tracking records
- `PsyScale` / `PsyScaleQuestion` / `PsyScaleOption` - psychological assessment scales
- `PsyAssessmentRecord` / `PsyAssessmentAnswer` - assessment results
- `PsyWarning` - risk/warning alerts
- `PsyMessage` - messages between users

## Development Notes

- Java packages follow `com.ruoyi.*` convention
- Psychological module API endpoints start with `/psych/` mapped in `PsyAdminController`, `PsyCounselorController`, `PsyStudentController`
- Frontend uploads directory: `D:/ruoyi/uploadPath`
- Admin default credentials: admin/admin123