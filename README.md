# AI-Powered Distributed Emergency Assistance System

> Think: Uber + 112 Emergency + AI Prioritization

A production-grade distributed system for real-time emergency response.
Built with microservices architecture, event-driven design, and AI-powered prioritization.

![CI](https://github.com/Kshitij5486/emergency-system/actions/workflows/ci.yml/badge.svg)

---

## Live Demo Flow

```
Citizen reports emergency
       |
       v
  API Gateway (8080) -- JWT validation
       |
       +---> User Service (8081)     -- Auth, JWT, RBAC
       |
       +---> Emergency Service (8082) -- Incidents, State Machine
       |
       +---> [Sprint 2] Dispatch Service (8083) -- AI routing
       |
       +---> [Sprint 3] Tracking Service (8084) -- WebSockets
```

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.3.2 |
| API Gateway | Spring Cloud Gateway |
| Database | PostgreSQL 16.3 (6 schemas) |
| Cache | Redis 7.4 |
| Migrations | Flyway 10.15 |
| Auth | JWT (jjwt 0.12.6), BCrypt |
| Messaging | Apache Kafka (Sprint 2) |
| AI | Python + ML models (Sprint 4) |
| Frontend | React + WebSockets (Sprint 3) |
| DevOps | Docker, GitHub Actions CI |

---

## Microservices

| Service | Port | Responsibility |
|---------|------|---------------|
| api-gateway | 8080 | JWT validation, routing |
| user-service | 8081 | Auth, registration, RBAC |
| emergency-service | 8082 | Incident reporting, state machine |
| dispatch-service | 8083 | Responder assignment (Sprint 2) |
| tracking-service | 8084 | Real-time GPS (Sprint 3) |
| notification-service | 8086 | SMS/push alerts (Sprint 2) |

---

## Key Features (Sprint 1 Complete)

- **JWT Authentication** — stateless, token stored in memory not localStorage
- **Role-Based Access Control** — CITIZEN, RESPONDER, ADMIN roles
- **Incident State Machine** — enforced transitions: REPORTED → QUEUED → DISPATCHED → IN_PROGRESS → RESOLVED
- **Status History** — append-only audit log of every status change
- **API Gateway** — single entry point, JWT validated once at edge
- **6 Postgres Schemas** — one per service, zero cross-schema queries
- **Flyway Migrations** — versioned, checksummed, auto-applied on startup

---

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker Desktop

### Run locally

```bash
# 1. Start infrastructure
docker compose up -d postgres redis

# 2. Start services (separate terminals)
cd services/user-service && mvn spring-boot:run
cd services/emergency-service && mvn spring-boot:run
cd services/api-gateway && mvn spring-boot:run
```

### Run integration tests

```powershell
.\docs\integration-test.ps1
# Expected: Passed: 7, Failed: 0
```

---

## API Reference

All requests go through the gateway at `http://localhost:8080`.
Import `docs/postman-collection.json` into Postman for the full collection.

### Auth
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/auth/register | None | Register new user |
| POST | /api/auth/login | None | Login, returns JWT |

### Users
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | /api/users/me | JWT | Get own profile |
| GET | /api/admin/dashboard | JWT + ADMIN | Admin only route |

### Incidents
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/incidents | JWT | Report emergency |
| GET | /api/incidents/:id | JWT | Get incident by ID |
| PATCH | /api/incidents/:id/status | JWT | Update status |
| GET | /api/incidents/my | JWT | My incidents |
| GET | /api/incidents/active?city= | JWT | Active by city |

---

## Incident State Machine

```
REPORTED --> QUEUED --> DISPATCHED --> IN_PROGRESS --> RESOLVED
    |            |            |               |
    +------------+------------+---------------+-> CANCELLED
    |
    +-> FAKE (AI detection - Sprint 4)
```

Invalid transitions return **409 Conflict** with the exact error message.
Every transition is recorded in `incident_status_history` (append-only audit log).

---

## System Design Concepts Demonstrated

- **Microservices** — 6 independently deployable services
- **API Gateway pattern** — single entry point, cross-cutting concerns (JWT) at edge
- **Event-driven architecture** — Kafka integration (Sprint 2)
- **State machine pattern** — enforced incident lifecycle
- **Append-only audit log** — status history never updated, only inserted
- **Schema-per-service** — database isolation without separate DB instances
- **Stateless auth** — JWT, no server-side sessions
- **Partial indexes** — optimized queries on filtered subsets

---

## Sprint Progress

- [x] **Sprint 1** — Foundation, Auth, Emergency Service, API Gateway
- [ ] **Sprint 2** — Kafka, Dispatch Service, Notifications
- [ ] **Sprint 3** — React Frontend, WebSockets, Live Tracking
- [ ] **Sprint 4** — AI Priority Scoring, Fake Detection
- [ ] **Sprint 5** — Kubernetes, Load Testing, Production Deploy

---

## Author

Built by **Kshitij** as a portfolio project demonstrating distributed systems,
microservices architecture, and AI integration.
