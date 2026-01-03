# Crossfit Booking Application

CrossFit gym booking application where users can register/login, view weekly WOD schedules, book training slots, and cancel reservations. Administrators can manage WOD descriptions and generate weekly schedules.

---

## General Requirements
- Java 17
- Spring Boot 3.x
- Spring Security (JWT Authentication & Authorization)
- MongoDB (MongoDB Atlas supported)
- Swagger / OpenAPI
- Maven 3.8+ (or use the Maven Wrapper)

---

## Features
### User
- Register & Login
- View weekly training schedule (WODs)
- Book one training slot per day
- Cancel booking
- JWT-secured access

### Admin
- Generate weekly schedules automatically or manually
- Update WOD descriptions per date & time slot
- View and manage bookings
- Role-based authorization

---

## Configuration (Environment Variables)


### `.env` example (local development)
```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
APP_TIMEZONE=Europe/Athens

MONGO_URI=mongodb+srv://<USER>:<PASSWORD>@<CLUSTER_HOST>/<DB>?retryWrites=true&w=majority
MONGO_DB=crossfitdb

JWT_SECRET=<RANDOM_SECRET>
JWT_EXPIRATION_MS=86400000

```


## Notes

JWT_SECRET must be strong (at least 256-bit/32 bytes).

If your MongoDB password contains special characters, you may need URL encoding.

## Git safety

.env is ignored via .gitignore:

.env

.env.*

## Run (Local)
### IntelliJ

Install the IntelliJ plugin EnvFile

Run → Edit Configurations → (your Spring Boot run config)

Enable EnvFile → select your .env file

Run the application

## Build
### Build JAR
./mvnw clean package


## JAR output:

target/*.jar

## Swagger / API Docs

Swagger UI: /swagger-ui/index.html

OpenAPI JSON: /api-docs

## Scheduling: Weekly Slot Generation

Timezone: Europe/Athens (configured via spring.task.scheduling.time-zone)

Automatic: every Sunday at 23:00 creates the next week schedules (Mon–Sat; Sunday closed).

Admin can also manually trigger generation.


## Deploy
As a Java application and inject environment variables on the server/cloud provider.

MONGO_URI

JWT_SECRET

SERVER_PORT

APP_TIMEZONE

JWT_EXPIRATION_MS

## Roles

ROLE_USER: view schedule, book/cancel reservations, profile.

ROLE_ADMIN: CRUD WOD schedule data, view reservations, manage users.