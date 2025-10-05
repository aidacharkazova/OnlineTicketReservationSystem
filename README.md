
# 🎟️ Online Ticket Reservation System (REST API)

A **Spring Boot RESTful API** for managing **events, venues, schedules, seats, and ticket reservations**.
The project provides secure user authentication, role-based access control, and transactional ticket booking — following clean backend architecture.

---

## 🚀 Overview

This backend system allows:

* Users to **register, log in, view events, and book tickets**
* Admins to **create and manage venues, events, and schedules**
* Real-time **seat and ticket management** with Redis caching and data consistency ensured through transactions

---

## 🧱 Tech Stack

| Layer          | Technology                        |
| -------------- | --------------------------------- |
| **Language**   | Java 17+                          |
| **Framework**  | Spring Boot 3.x                   |
| **Database**   | PostgreSQL                        |
| **ORM**        | Spring Data JPA                   |
| **Security**   | Spring Security + JWT             |
| **Caching**    | Redis                             |
| **Validation** | Jakarta Bean Validation (JSR 380) |
| **Mapping**    | MapStruct                         |
| **Build Tool** | Maven                             |
| **API Format** | RESTful JSON                      |

---

## 🏗️ Architecture

The project uses **multi-layered architecture** with clear separation of concerns:

```
controller  →  service  →  repository  →  entity
          ↘ exception ↙
```

### 🧩 Package Structure

```
com.example.onlineticketreservationsystem/
│
├── controller/       # REST endpoints
├── dto/
│   ├── request/      # Request models
│   └── response/     # Response models
├── entity/           # JPA entities
├── repository/       # Data access layer
├── service/          # Business logic
├── mapper/           # Entity-DTO converters (MapStruct)
├── exception/        # Custom exceptions & global handler
├── config/           # JWT, Security, Redis, CORS, etc.
└── OnlineTicketReservationSystemApplication.java
```

---

## 📦 Core Entities

| Entity       | Description                                                |
| ------------ | ---------------------------------------------------------- |
| **AppUser**  | Represents registered users with roles (`USER`, `ADMIN`)   |
| **Venue**    | Venue information (name, location, capacity)               |
| **Event**    | Event details like title, category, venue, and description |
| **Schedule** | Event date and time                                        |
| **Seat**     | Seat details and availability                              |
| **Ticket**   | Links user, event, seat, and schedule for booking          |

---

## ⚙️ Features

### 👤 User Management

* User registration and login via JWT
* Role-based access (`USER`, `ADMIN`)
* View user profile and ticket history

### 🎭 Event & Venue Management

* Admin can create, update, delete, and view events or venues
* Filter events by category, date, or location

### 🕓 Schedule & Seat Handling

* Admin defines event schedules
* Seats are managed dynamically and reserved per booking
* Transactional safety to prevent overbooking

### 🎫 Ticket Booking

* Users can book or cancel tickets
* Seat availability updated automatically
* View all booked tickets for logged-in user

### ⚡ Performance Enhancements

* Redis caching for commonly accessed data (e.g., events list)
* Global exception handling with custom responses
* Input validation on all API requests

---

## 🔐 Security Overview

* **JWT-based authentication** for stateless sessions
* **BCrypt password encryption** for all stored passwords
* **Role-based access control** for different endpoints
* **CORS configuration** for safe frontend integration

---

## 🧠 REST API Endpoints

| Method   | Endpoint             | Description                      | Access |
| -------- | -------------------- | -------------------------------- | ------ |
| `POST`   | `/api/auth/register` | Register a new user              | Public |
| `POST`   | `/api/auth/login`    | Authenticate user and return JWT | Public |
| `GET`    | `/api/events`        | Retrieve all events              | Public |
| `GET`    | `/api/events/{id}`   | Retrieve event details           | Public |
| `POST`   | `/api/events`        | Create a new event               | Admin  |
| `PUT`    | `/api/events/{id}`   | Update existing event            | Admin  |
| `DELETE` | `/api/events/{id}`   | Delete event                     | Admin  |
| `POST`   | `/api/tickets/book`  | Book a ticket                    | User   |
| `DELETE` | `/api/tickets/{id}`  | Cancel a ticket                  | User   |
| `GET`    | `/api/tickets/my`    | View all user’s tickets          | User   |

---

## 🧰 Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/online-ticket-reservation-system.git
cd online-ticket-reservation-system
```

### 2️⃣ Configure the Database

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ticketdb
    username: postgres
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: your-jwt-secret
  expiration: 86400000 # 1 day in ms

spring:
  data:
    redis:
      host: localhost
      port: 6379
```

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

### 4️⃣ Access the API

Once running:

```
http://localhost:8080
```

If Swagger is enabled:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🧩 Example JSON Payloads

### 🔸 User Registration

```json
{
  "name": "Aida",
  "email": "aida@example.com",
  "password": "SecurePass123"
}
```

### 🔸 Event Creation (Admin)

```json
{
  "title": "Spring Boot Conference",
  "category": "Technology",
  "venueId": 2,
  "scheduleId": 5,
  "price": 50.0
}
```

### 🔸 Ticket Booking

```json
{
  "userId": 1,
  "eventId": 3,
  "seatId": 12
}
```

---

## 💡 Future Improvements

* Payment integration (Stripe/PayPal)
* Email notifications after booking
* Admin dashboard for analytics
* Interactive seat selection (React/Angular frontend)
* Docker setup for deployment

---

## 👨‍💻 Author

**Aida Charkazova**
Java Backend Developer
📍 Azerbaijan
💻 GitHub: https://github.com/aidacharkazova/OnlineTicketReservationSystem.git
