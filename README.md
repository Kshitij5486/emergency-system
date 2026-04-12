# Emergency Response System
**AI-Powered Distributed Emergency Assistance Platform**

> Think: Uber + 112 Emergency + AI Prioritization â€” built with production-grade distributed systems.

---

## What it does

- Citizens report emergencies (accident, fire, medical) via web app or one-tap SOS
- AI scores priority and detects fake alerts instantly
- Nearest available responder assigned in milliseconds via smart routing
- Live GPS tracking for both citizen and responder via WebSockets
- Every event flows through Apache Kafka â€” fault-tolerant, independently scalable

---

## Architecture

```
                    â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                    â”‚       API Gateway  :8080          â”‚
                    â”‚   Spring Cloud Gateway 4.1.4      â”‚
                    â””â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                         â”‚          â”‚          â”‚
              â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”  â”Œâ”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”  â”Œâ–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
              â”‚ User Serviceâ”‚  â”‚ Emergency â”‚  â”‚  Tracking   â”‚
              â”‚    :8081    â”‚  â”‚  Service  â”‚  â”‚  Service    â”‚
              â”‚  JWT + Auth â”‚  â”‚   :8082   â”‚  â”‚  :8084 WS   â”‚
              â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”˜  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                                     â”‚ Kafka
                  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                  â”‚          Apache Kafka 3.7.1            â”‚
                  â”‚  emergency-events Â· dispatch-updates   â”‚
                  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                               â”‚              â”‚
                    â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”   â”Œâ”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                    â”‚  Dispatch   â”‚   â”‚  Notification        â”‚
                    â”‚  Service    â”‚   â”‚  Service  :8086      â”‚
                    â”‚  :8083      â”‚   â”‚  SMS via Twilio      â”‚
                    â””â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”˜   â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                           â”‚ HTTP
                    â”Œâ”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
                    â”‚     AI Service  :8085  (Python)      â”‚
                    â”‚  Priority model Â· Fake detection     â”‚
                    â”‚  Smart routing via OSRM              â”‚
                    â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜

  Data:     PostgreSQL 16.3  +  Redis 7.4
  Frontend: React 18 + Leaflet + STOMP WebSockets
  DevOps:   Docker â†’ Kubernetes (Sprint 5)
```

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Java | 21 LTS |
| Framework | Spring Boot | 3.3.2 |
| Auth | Spring Security + jjwt | 6.3 / 0.12.6 |
| Messaging | Apache Kafka | 3.7.1 |
| Database | PostgreSQL | 16.3 |
| Migrations | Flyway | 10.15.0 |
| Cache | Redis | 7.4 |
| Frontend | React + Vite | 18.3.1 / 5.3.4 |
| Maps | Leaflet + React-Leaflet | 1.9.4 / 4.2.1 |
| AI/ML | Python + FastAPI + scikit-learn | 3.12.4 / 0.112.1 / 1.5.1 |
| Routing | OSRM | 5.27.1 |
| CI/CD | GitHub Actions | latest |
| Containers | Docker + Compose | 27.x |
| Orchestration | Kubernetes | 1.31 |

---

## Sprint Progress

- [x] **Sprint 1** â€” Foundation & core services (Weeks 1â€“3)
- [ ] **Sprint 2** â€” Kafka messaging & dispatch (Weeks 4â€“6)
- [ ] **Sprint 3** â€” Real-time tracking & frontend (Weeks 7â€“9)
- [ ] **Sprint 4** â€” AI/ML integration (Weeks 10â€“12)
- [ ] **Sprint 5** â€” Scale, observability & deploy (Weeks 13â€“15)

---

## Quick Start

### Prerequisites
- Docker Desktop 27+ with Compose v2
- Java 21 LTS
- Node 20 LTS *(Sprint 3)*
- Python 3.12.4 *(Sprint 4)*

### Run locally

```bash
git clone https://github.com/YOUR_USERNAME/emergency-system.git
cd emergency-system

cp .env.example .env    # defaults work for local dev

make up                 # starts Postgres + Redis
make ps                 # verify both show "healthy"
make db-shell           # open psql to inspect schemas
```

Inside psql:
```sql
\dn          -- shows: users, incidents, dispatch, tracking, notifications, analytics
\q
```

---

## Project Structure

```
emergency-system/
â”œâ”€â”€ docker-compose.yml
â”œâ”€â”€ pom.xml                          # Maven parent â€” all versions locked here
â”œâ”€â”€ Makefile                         # make up / down / logs / db-shell
â”œâ”€â”€ .env.example
â”‚
â”œâ”€â”€ services/
â”‚   â”œâ”€â”€ user-service/                # Auth, JWT        (Sprint 1)
â”‚   â”œâ”€â”€ emergency-service/           # Incidents CRUD   (Sprint 1)
â”‚   â”œâ”€â”€ dispatch-service/            # Kafka + routing  (Sprint 2)
â”‚   â”œâ”€â”€ tracking-service/            # WebSockets + GPS (Sprint 3)
â”‚   â”œâ”€â”€ notification-service/        # SMS via Twilio   (Sprint 2)
â”‚   â””â”€â”€ api-gateway/                 # Spring Cloud GW  (Sprint 1)
â”‚
â”œâ”€â”€ ai-service/                      # FastAPI + ML     (Sprint 4)
â”œâ”€â”€ frontend/                        # React 18         (Sprint 3)
â”‚
â””â”€â”€ infrastructure/
    â”œâ”€â”€ postgres/init.sql            # Schema creation
    â”œâ”€â”€ kafka/                       # Topic config     (Sprint 2)
    â””â”€â”€ k8s/                         # K8s manifests    (Sprint 5)
```

---

## Key Decisions

**Kafka over direct REST between services** â€” Emergency Service fires an event and doesn't care who processes it. Services scale independently and consumers can replay from offset if they crash.

**One Postgres, separate schemas** â€” Simulates microservice DB isolation on a dev machine without the overhead of multiple DB instances. Production would use separate RDS per service.

**JWT in memory, not localStorage** â€” XSS cannot steal in-memory tokens. A single extra line of caution that most candidates miss.

**OSRM self-hosted routing** â€” Zero API cost, no rate limits. Shows operational thinking beyond calling paid APIs.

---

## Load Test Results
> Gatling results added in Sprint 5 â€” target: 500 concurrent users, p95 < 500ms

## Demo Video
> Recorded in Sprint 3

---
*Built one day at a time.*
