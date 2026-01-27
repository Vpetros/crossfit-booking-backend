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

### Roles

ROLE_USER: view schedule, book/cancel reservations, profile.

ROLE_ADMIN: CRUD WOD schedule data, generate weekly schedules, view reservations, manage users.

### Notes
- JWT is required for protected endpoints.
- Role-based access control is enforced via Spring Security.
Admin users can be created:
- Manually in MongoDB (direct database access), or
- Via the Swagger `POST /api/auth/register` endpoint **for development/testing purposes**,
  by registering a user with role `ROLE_ADMIN`.

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

JWT_SECRET must be at least 256 bits(32 bytes).

If your MongoDB password contains special characters, you may need URL encoding.

## Git safety

.env is ignored via .gitignore:

.env

.env.*

## Run (Local)
### IntelliJ

-Clone the repository

-Create a .env file in the project root

-Install IntelliJ plugin: EnvFile

-Run → Edit Configurations → Spring Boot

-Enable EnvFile → select your .env file

-Run the application

Application runs on:

http://localhost:8080


Swagger UI:

http://localhost:8080/swagger-ui/index.html

## Run (CLI – Optional)

Alternatively, the application can be started via terminal:

- export SPRING_PROFILES_ACTIVE=dev
- export MONGO_URI="mongodb+srv://<USER>:<PASSWORD>@<CLUSTER_HOST>/<DB>?retryWrites=true&w=majority"
- export MONGO_DB=crossfitdb
- export JWT_SECRET="<SECRET>"

mvn spring-boot:run


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

Automatic: every Saturday at 15:00 creates the next week schedule (Mon–Sat; Sunday closed).

Admin can also manually trigger generation.


## Deploy
The application can be deployed as a standard Java application and inject environment variables on the server/cloud provider.

Required environment variables:

MONGO_URI

MONGO_DB

JWT_SECRET

JWT_EXPIRATION_MS

SERVER_PORT

APP_TIMEZONE

