# AI-Powered Distributed Emergency Assistance System

A production-grade emergency response platform built using a microservices architecture. This project demonstrates an event-driven, scalable, and fault-tolerant backend system for real-time emergency dispatch, AI-based prioritization, and live responder tracking â€” all containerized with Docker.

> Think: Uber + 112 Emergency + AI Prioritization

---

## Architecture Overview

The system is composed of independent microservices that communicate through Apache Kafka. All external requests are routed through a Spring Cloud API Gateway.

```
+---------------------------------------+
|         API Gateway  :8080            |
|     Spring Cloud Gateway 4.1.4        |
+----------+------------+---------------+
           |            |               |
    +------+------+ +---+--------+ +---+--------+
    | User Service| | Emergency  | | Tracking   |
    |    :8081    | |  Service   | | Service    |
    |  JWT + Auth | |   :8082    | | :8084 WS   |
    +-------------+ +-----+------+ +------------+
                          |
                          | Kafka
          +---------------+--------------------+
          |          Apache Kafka 3.7.1         |
          |  emergency-events, dispatch-updates  |
          +-----------+----------------+--------+
                      |                |
          +-----------+----+  +--------+-----------------+
          | Dispatch       |  | Notification             |
          | Service :8083  |  | Service  :8086           |
          |                |  | SMS via Twilio           |
          +-------+--------+  +--------------------------+
                  |
                  | HTTP
          +-------+----------------------------------+
          |    AI Service  :8085  (Python)           |
          |  Priority model - Fake detection         |
          |  Smart routing via OSRM                  |
          +------------------------------------------+

  Data:     PostgreSQL 16.3  +  Redis 7.4
  Frontend: React 18 + Leaflet + STOMP WebSockets
  DevOps:   Docker -> Kubernetes (Sprint 5)
```

The main services are:

- **API Gateway**: Single entry point for all client requests. Handles routing and cross-cutting concerns.
- **User Service**: Manages authentication, JWT issuance, and user profiles.
- **Emergency Service**: Handles incident creation, status tracking, and audit history.
- **Dispatch Service**: Assigns the nearest available responder using AI scores and geolocation.
- **Tracking Service**: Streams live GPS updates to the frontend via WebSockets.
- **Notification Service**: Sends SMS alerts to citizens and responders via Twilio.
- **AI Service**: Scores incident priority, detects fake alerts, and computes optimal routing.

---

## Features

- One-tap SOS button for instant emergency reporting.
- AI-based priority scoring (1-5) on every incoming incident.
- Fake alert detection using a trained ML model.
- Nearest-responder dispatch via OSRM self-hosted routing.
- Live GPS tracking via WebSockets for both citizen and responder.
- Full incident status timeline (reported -> dispatched -> resolved).
- SMS notifications via Twilio on dispatch and resolution.
- Role-based access: Citizen, Responder, and Admin dashboards.
- Centralized API documentation via SpringDoc OpenAPI through the gateway.

---

## Tech Stack

| Category      | Technology                       | Version         |
|---------------|----------------------------------|-----------------|
| Language      | Java                             | 21 LTS          |
| Framework     | Spring Boot                      | 3.3.2           |
| Auth          | Spring Security + jjwt           | 0.12.6          |
| Gateway       | Spring Cloud Gateway             | 4.1.4           |
| Messaging     | Apache Kafka                     | 3.7.1           |
| Database      | PostgreSQL                       | 16.3            |
| Migrations    | Flyway                           | 10.15.0         |
| Cache         | Redis                            | 7.4             |
| Frontend      | React + Vite                     | 18.3.1 / 5.3.4  |
| Maps          | Leaflet + React-Leaflet          | 1.9.4 / 4.2.1   |
| AI/ML         | Python + FastAPI + scikit-learn  | 3.12.4          |
| Routing       | OSRM (self-hosted)               | 5.27.1          |
| CI/CD         | GitHub Actions                   | latest          |
| Containers    | Docker + Compose                 | 27.x            |
| Orchestration | Kubernetes                       | 1.31 (Sprint 5) |

---

## Getting Started

### Prerequisites

- **Git**
- **Docker** and **Docker Compose** (v2+)
- **JDK 21** or later
- **Maven**

### Installation and Startup

1. Clone the repository:

```bash
git clone https://github.com/Kshitij5486/emergency-system.git
cd emergency-system
```

2. Set up environment variables:

```bash
cp .env.example .env
```

3. Start the infrastructure containers:

```bash
make up
```

4. Verify both containers are healthy:

```bash
make ps
```

5. Inspect the database schemas:

```bash
make db-shell
```

Inside psql:

```sql
\dn
\q
```

6. Build and run a service (Sprint 1+):

```bash
cd services/user-service
mvn spring-boot:run
```

Flyway migrations run automatically on startup.

---

## Usage

Once all services are running, the API is accessible through the API Gateway on port `8080`.

### API Documentation

Generated using SpringDoc OpenAPI, accessible via the gateway:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Key Endpoints

| Method | Path                        | Description                     |
|--------|-----------------------------|---------------------------------|
| POST   | `/api/auth/register`        | Register a new citizen account  |
| POST   | `/api/auth/login`           | Obtain a JWT access token       |
| POST   | `/api/incidents`            | Report a new emergency          |
| GET    | `/api/incidents/{id}`       | Get incident status and history |
| GET    | `/api/responders`           | List available responders       |
| WS     | `/ws/tracking/{incidentId}` | Live GPS tracking stream        |

---

## Project Structure

```
emergency-system/
+-- docker-compose.yml
+-- pom.xml                          (Maven parent - all versions locked here)
+-- Makefile                         (make up / down / logs / db-shell)
+-- .env.example
|
+-- services/
|   +-- user-service/                (Auth, JWT        - Sprint 1)
|   +-- emergency-service/           (Incidents CRUD   - Sprint 1)
|   +-- dispatch-service/            (Kafka + routing  - Sprint 2)
|   +-- tracking-service/            (WebSockets + GPS - Sprint 3)
|   +-- notification-service/        (SMS via Twilio   - Sprint 2)
|   +-- api-gateway/                 (Spring Cloud GW  - Sprint 1)
|
+-- ai-service/                      (FastAPI + ML     - Sprint 4)
+-- frontend/                        (React 18         - Sprint 3)
|
+-- infrastructure/
    +-- postgres/init.sql            (Schema creation)
    +-- kafka/                       (Topic config     - Sprint 2)
    +-- k8s/                         (K8s manifests    - Sprint 5)
```

---

## Sprint Progress

- [x] **Sprint 1** - Foundation: monorepo scaffold, Docker, Postgres + Redis, Flyway migrations, User Service, Emergency Service, API Gateway (Weeks 1-3)
- [ ] **Sprint 2** - Kafka messaging, Dispatch Service, Notification Service (Weeks 4-6)
- [ ] **Sprint 3** - WebSocket tracking, React frontend, live map with Leaflet (Weeks 7-9)
- [ ] **Sprint 4** - AI/ML integration: priority scoring, fake detection, OSRM routing (Weeks 10-12)
- [ ] **Sprint 5** - Kubernetes, observability, Gatling load tests, production hardening (Weeks 13-15)

---

## Key Design Decisions

**Kafka over direct REST between services** - Emergency Service fires an event and does not care who processes it. Services scale independently and consumers can replay from offset if they crash.

**One Postgres, separate schemas** - Simulates microservice database isolation on a dev machine without the overhead of multiple database instances. Production would use separate RDS instances per service.

**JWT stored in memory, not localStorage** - XSS cannot steal in-memory tokens. A single line of caution that most candidates miss.

**OSRM self-hosted routing** - Zero API cost, no rate limits, no dependency on paid third-party APIs. Shows operational thinking beyond calling Google Maps.

**Append-only status history table** - Every incident state change writes a new row. Nothing is ever updated. This is the event-sourcing pattern and directly powers the live timeline UI.

---

## Load Test Results

> Gatling results to be added in Sprint 5.
> Target: 500 concurrent users, p95 latency < 500ms.

---

## Demo Video

> Recorded in Sprint 3.

---

## License

This project is distributed under the MIT License. See `LICENSE` for more information.

---

## Contact

**Kshitij**

- GitHub: [@Kshitij5486](https://github.com/Kshitij5486)

---

*Built one day at a time.*
