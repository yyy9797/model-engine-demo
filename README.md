# Model Engine Demo

This repository contains a demo for a configurable model/data element engine.

Contents:
- frontend/: Vue 3 demo (model editor, form renderer, dictionary manager, approval UI)
- backend/: Spring Boot example with PostGIS integration
- openapi/: OpenAPI specification
- scripts/: DB init and docker-compose for quick demo

Quick start (recommended):
1. Start Postgres+PostGIS and backend via Docker Compose (requires Docker & Docker Compose):
   docker compose up --build

2. Backend will be available at http://localhost:8080

3. Frontend (development):
   cd frontend
   npm install
   npm run dev

Notes:
- The backend is a simplified demo and uses Hibernate DDL auto-update for convenience. For production use Flyway/Liquibase and better secrets management.
- The frontend can run against the mock json-server or the Java backend. Adjust `src/api/index.js` `baseURL` accordingly.
