# HLR Telecom Web Application

Implementation based on the HLR requirements document using:
- React (frontend)
- Spring Boot (middleware)
- PostgreSQL (database)

## Implemented capabilities
- Subscriber lifecycle: create, activate/suspend/terminate, SIM swap, MSISDN change
- Identity mapping: IMSI/MSISDN/ICCID uniqueness and historical mapping
- Service profile model: ODB and key supplementary fields
- Roaming policy model with VPLMN lists
- Maker-checker flow: change requests, approval/rejection, no self-approval
- Audit logging with actor/action/before/after/correlation ID
- RBAC-ready API security (basic auth sample users)

## Project structure
- `backend`: Spring Boot 3 + JPA + Flyway
- `frontend`: React + Vite
- `docker-compose.yml`: PostgreSQL local runtime

## Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 20+
- Docker Desktop (for Postgres)

## Run locally
1. Start PostgreSQL:
```bash
docker compose up -d postgres
```
2. Start backend:
```bash
cd backend
mvn spring-boot:run
```
3. Start frontend:
```bash
cd frontend
npm install
npm run dev
```
4. Open `http://localhost:5173`

## Sample API credentials
Configured in `backend/src/main/java/com/hlr/app/config/SecurityConfig.java`:
- `admin / admin123`
- `provisioner / prov123`
- `approver / approve123`
- `viewer / viewer123`

## Main API endpoints
- `POST /api/subscribers`
- `GET /api/subscribers/search?key={value}`
- `POST /api/subscribers/{id}/sim-swap`
- `POST /api/subscribers/{id}/number-change`
- `POST /api/subscribers/{id}/state/{ACTIVE|SUSPENDED|TERMINATED|INACTIVE}`
- `POST /api/change-requests`
- `GET /api/change-requests/pending`
- `POST /api/change-requests/{id}/approval`

## Notes
- This is a strong functional scaffold aligned to the HLR product requirements and ready for extension.
- Vendor HLR adapters, OSS/BSS webhooks, bulk import engine, reconciliation scheduler, and SSO/OIDC integration are left as next-phase modules.
