# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Campus Forum backend REST API built with Spring Boot. No frontend code in this repo. Chinese-language project (code comments, API docs, commit messages).

## Build & Run

```bash
# Full build (skip tests)
mvn clean install -DskipTests

# Build only the server module
mvn clean install -pl CampusForum-Server -am

# Package executable JAR
mvn clean package -pl CampusForum-Server -DskipTests

# Run tests
mvn test
mvn test -pl CampusForum-Server
mvn test -pl CampusForum-Server -Dtest=ClassName
```

Main class: `cn.zuo.CampusForumApplication`

## Module Structure

```
CampusForum-Commons    → Shared utilities, constants, exceptions, JWT, Ali OSS helpers
CampusForum-Pojo       → Entities, DTOs, VOs (data models)
CampusForum-Server     → Spring Boot app (controllers, services, mappers, config)
```

Base package: `cn.zuo`

## Architecture

Classic layered Spring Boot MVC:

- **Controllers** (`cn.zuo.controller`): Split into `user/` (8 controllers) and `admin/` (6 controllers)
- **Services** (`cn.zuo.service`): Interface + `impl/` pattern, extend MyBatis-Plus `ServiceImpl`
- **Mappers** (`cn.zuo.mapper`): MyBatis-Plus mapper interfaces
- **Config** (`cn.zuo.config`): `agent/` (AI), `database/`, `threadpool/`, `web/`

## Key Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.5.9, Java 17 |
| ORM | MyBatis-Plus 3.5.5 |
| Database | MySQL 8.0.33, Druid connection pool |
| Cache | Redis (Lettuce) |
| Auth | JWT (jjwt 0.11.5), Bearer token scheme |
| AI | Spring AI Alibaba 1.1.2.0 (DashScope), models: `qwen3-max` + default |
| File Storage | Ali OSS 3.17.4 |
| API Docs | Knife4j 3.0.2 (Swagger) |
| JSON | Fastjson 1.2.76 |

## API Routes

All under `/api/`. User endpoints: `/api/users/*`, `/api/discussions/*`, `/api/replies/*`, `/api/likes/*`, `/api/favorites/*`, `/api/notifications/*`, `/api/ai/*`, `/api/common/*`. Admin endpoints: `/api/admin/*`.

## Auth Flow

- JWT interceptor on `/api/**` (excludes `/api/users/login` and `/api/users/register`)
- Token via `Authorization: Bearer <token>` header
- User ID stored in `ThreadLocal` via `ThreadLocalUtil`
- Token blacklist on logout via Redis (`token:blacklist:` prefix)

## Response Format

All endpoints return `Result<T>`: `{ code: 200/500, message, data }`. Paginated: `PageResult<T>` with `total` and `records`.

## Configuration

- `application.yml` — main config with externalized placeholders (`${...}`)
- `application-dev.yml` — **git-ignored**, contains actual credentials. You must create this file locally.

## Soft Deletes

Discussions, replies, and users use soft deletes (status field, not physical row removal).

## Conventions

- Services extend `ServiceImpl<Mapper, Entity>` from MyBatis-Plus
- Admin and user controllers are in separate packages
- AI chat conversation ID format: `{userId}_{sessionId}`
- Discussion categories: `learnAndCommunicate`, `campusLife`, `JobHuntingAndEmployment`, `ClubActivities`
