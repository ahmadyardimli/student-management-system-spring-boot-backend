# Student Management System — Backend

*Spring Boot 3 · Java 17 · MySQL 8 · Docker · AWS EC2 · AWS Secrets Manager*

A production-minded backend you can run **locally** or on **AWS EC2** with Docker.
Beyond CRUD, this repo demonstrates **secure JWT auth** (access + rotating refresh), **secret management** with **AWS Secrets Manager**, clean **JPA/Hibernate** usage, and a practical **containerized** deploy story.

---

## Why this project stands out

* **Security first:** short-lived access tokens, rotating refresh tokens, secure client storage, single-fire session expiry UX.
* **12-factor config:** DB/JWT secrets live in **AWS Secrets Manager** (prod). No secrets in VCS.
* **Containerized runtime:** API and DB run as separate Docker containers on a private Docker network.
* **Resilient client:** the Android app uses an OkHttp **Authenticator** for automatic refresh & retry.
* **Clear separation of concerns:** layered API, managers, DTOs/requests, and clean error model.
* **Realistic deploy loop:** build on laptop → ship JAR → run in a tiny Java 17 image on EC2.

---

## Architecture (simple)

```
EC2 (Ubuntu)
└── Docker network: smsnet
    ├── zeka_tm_qebele_mysql_db  (mysql:8.1)      ← persistent volume, internal only
    └── sms_backend              (Java 17 JRE)    ← runs Spring Boot fat JAR
```

* **Secrets**: stored in `prod/sms/backend` (AWS Secrets Manager).
* **Backend ↔ DB**: internal Docker DNS by container name (`jdbc:mysql://zeka_tm_qebele_mysql_db:3306/...`).
* **Edge**: port `8080` exposed for the API (front with ALB/NGINX in real prod).

---

## Key files (in this repo)

**`application.properties`** (prod imports secrets; env overrides allowed)

```properties
spring.config.import=optional:aws-secretsmanager:prod/sms/backend
spring.cloud.aws.region.static=eu-north-1

spring.datasource.url=${db_sms_url}
spring.datasource.username=${db_sms_username}
spring.datasource.password=${db_sms_password}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

sms.app.secret=${sms_app_secret}
server.address=0.0.0.0
```

**`Dockerfile`** (multi-stage; small runtime)

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests -T 1C -Dmaven.compiler.release=17 package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar /app/app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
```

**`docker-compose.ec2.yml`** (backend service on existing network)

```yaml
services:
  app:
    build: .
    container_name: sms_backend
    ports: ["8080:8080"]
    restart: unless-stopped
    networks: [smsnet]

networks:
  smsnet:
    external: true
```

**`.dockerignore`** (keep images lean)

```
target/
.git/
.gitignore
.idea/
*.iml
.vscode/
.DS_Store
docker-compose*.yml
.env
```

**`.env.example`** (local template — do not commit secrets)

```
db_sms_url=jdbc:mysql://localhost:3306/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db_sms_username=root
db_sms_password=CHANGE_ME
sms_app_secret=CHANGE_ME
```

---

## Security & token flow

This backend uses a **stateless** auth model: a short-lived **JWT access token** plus a **rotating refresh token**. It’s intentionally **client-agnostic**. The same flow works for Android, web, or any HTTP client.

### What the server issues & stores

* **Access token (JWT)**

  * Short TTL (minutes), signed.
  * Carries `sub` (user id) and authorities/roles.
  * Used on protected routes via `Authorization: Bearer <access>`.

* **Refresh token (opaque)**

  * Random, long(er) TTL.
  * **Stored hashed** in the database (never in plaintext).
  * **Rotated on every refresh**: old token becomes invalid the moment a new pair is issued.

### Core endpoints

* **`POST /auth/login`**

  * Validates credentials.
  * Returns `{ accessToken, refreshToken }`.
  * Persists a **hashed** refresh token record (user, expiry, fingerprint/metadata).

* **`POST /auth/refresh`**

  * Accepts the current refresh token.
  * Verifies it against the **hashed** record and checks expiry/revocation.
  * **Rotates**: invalidates the old refresh, issues **new `{ access, refresh }`**, saves the new hashed refresh.
  * Replay protection: presenting an **already-used** refresh is rejected (can optionally revoke the session family).

* **`POST /auth/logout`**

  * Invalidates the active refresh token (server-side record).

### Request protection

* **`JwtAuthenticationFilter`** validates `Authorization: Bearer <access>` on protected routes.
* On failure, **401** with a structured `ApiError` (stable shape for all errors).
* **Role gates** (`/admin/**`, `/user/**`, etc.) enforced via Spring Security annotations/config.

### Error model (consistent responses)

All auth errors (expired/invalid token, forbidden, bad refresh) return a JSON `ApiError` with:

* `timestamp`
* `status` (HTTP)
* `code` (machine-readable)
* `message` (human-readable)
* Optional `details`

This lets any client (mobile or web) implement uniform handling.

### Example payloads

```http
POST /auth/login
Content-Type: application/json

{ "username": "<user>", "password": "<pass>" }

-- 200 --
{
  "accessToken": "<JWT_ACCESS>",
  "refreshToken": "<OPAQUE_REFRESH>"
}
```

```http
POST /auth/refresh
Content-Type: application/json

{ "refreshToken": "<OPAQUE_REFRESH>" }

-- 200 --
{
  "accessToken": "<NEW_JWT_ACCESS>",
  "refreshToken": "<NEW_OPAQUE_REFRESH>"
}
```

```http
GET /user/profile
Authorization: Bearer <JWT_ACCESS>
```

> Hitting a protected route without/with a bad token returns **401**; that’s expected and indicates the app is up and secured.

### Security defaults & hardening

* **Stateless**: no server session; access is self-contained in JWT.
* **Hash refresh tokens** at rest; never log tokens.
* **One-time rotation** on refresh prevents replay if a token leaks.
* **Least privilege** with role checks and method security.
* **HTTPS** in production; strict CORS for web clients.
* Small **clock-skew** tolerance on JWT verification.
* Admin actions (password change, manual revoke) can invalidate outstanding refresh tokens.

### Client notes (brief)

* Any client (Android, iOS, **web**) can use this flow:

  * Save the **refresh** securely (e.g., encrypted storage, HTTP-only cookie for web).
  * Send the **access** as `Authorization: Bearer …`.
  * On **401**, call `/auth/refresh` **once**, replace both tokens, retry the original request once.
* Because the backend is **stateless** and uses standard HTTP/JSON, swapping the UI (mobile → web) requires **no backend changes**.


## Data layer (JPA/Hibernate)

* **MySQL 8** with `org.hibernate.dialect.MySQLDialect`.
* **Schema** auto-updated in dev via `spring.jpa.hibernate.ddl-auto=update` (use Flyway/Liquibase for strict prod).
* Entities are quoted globally to avoid reserved keyword clashes.
* Clean DTOs/Requests for admin/user domain objects (students, teachers, exam/user details like categories, groups, sections, etc.).

---

## Separation of concerns (high level)

* **Controllers**: REST endpoints (auth, admin, user domains).
* **Services/Managers**: business & orchestration logic; Android mirrors this with `*Manager` classes and Retrofit interfaces.
* **Repositories**: JPA interfaces.
* **Requests/Responses**: input validation & transport models.
* **Security**: token issue/verify/refresh, access control, exception advice → `ApiError`.

---

## How it runs (end-to-end)

1. **EC2** hosts two containers on the private Docker network `smsnet`:

   * `mysql:8.1` → `zeka_tm_qebele_mysql_db` (with a named volume).
   * `eclipse-temurin:17-jre-alpine` → `sms_backend` (runs the fat JAR).
2. **AWS Secrets Manager** holds `db_sms_url`, `db_sms_username`, `db_sms_password`, `sms_app_secret`.
3. **Spring Boot** imports the secret (via `spring.config.import`) and connects to MySQL by **container name**.
4. **Health check**: `curl http://<EC2_IP>:8080/` returns **401** (expected: app is up & secured).

---

## Production setup (what I did on EC2)

> Replace placeholders for your account/region/hostnames.

**1) Create shared Docker network**

```bash
docker network create smsnet
```

**2) Run MySQL**

```bash
docker run -d --name zeka_tm_qebele_mysql_db \
  --restart unless-stopped \
  --network smsnet \
  -e MYSQL_ROOT_PASSWORD='<secure-password>' \
  -v mysql_data:/var/lib/mysql \
  mysql:8.1
```

> Tip: keep port 3306 **internal** (don’t `-p 3306:3306`) and use Workbench via **SSH tunnel** for admin access.

**3) IAM role for EC2** (can read one secret, one region)

```json
{
  "Version": "2012-10-17",
  "Statement": [{
    "Sid": "ReadBackendSecret",
    "Effect": "Allow",
    "Action": ["secretsmanager:GetSecretValue","secretsmanager:DescribeSecret"],
    "Resource": "arn:aws:secretsmanager:<REGION>:<ACCOUNT_ID>:secret:prod/sms/backend-*",
    "Condition": { "StringEquals": { "aws:RequestedRegion": "<REGION>" } }
  }]
}
```

Attach this role to the EC2 instance.

**4) Create the secret**

```bash
REGION=<REGION>
aws secretsmanager create-secret \
  --region "$REGION" \
  --name prod/sms/backend \
  --secret-string '{
    "db_sms_url":"jdbc:mysql://<DB_CONTAINER_NAME>:<DB_PORT>/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
    "db_sms_username":"<db-user>",
    "db_sms_password":"<db-pass>",
    "sms_app_secret":"<jwt-secret>"
  }'
# use update-secret later to rotate/change values
```

**5) Build the app locally & ship the JAR**

```bash
mvn -DskipTests -Dmaven.compiler.release=17 package

scp -C -i <your-key.pem> \
  target/*-SNAPSHOT.jar \
  ubuntu@<EC2_PUBLIC_IP>:~/sms-backend/app.jar
```

**6) Run the backend container**

```bash
docker stop sms_backend 2>/dev/null || true
docker rm   sms_backend 2>/dev/null || true

docker run -d --name sms_backend \
  --restart unless-stopped \
  --network smsnet \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xms128m -Xmx512m" \
  -v "$HOME/sms-backend/app.jar:/app/app.jar:ro" \
  -w /app eclipse-temurin:17-jre-alpine \
  sh -c 'java $JAVA_OPTS -jar /app/app.jar'

docker logs -f --tail=200 sms_backend
```

**7) Verify**

```bash
docker ps                          # both containers up
curl -i http://localhost:8080/     # 401 Unauthorized (expected)
```

> I also created an **SSH tunnel** in **MySQL Workbench** to the EC2 to safely view schemas/tables without exposing 3306 publicly.

---

## Local development (IntelliJ, no AWS dependency)

* In **Run/Debug Configuration → Environment variables**, set:

  ```
  db_sms_url=jdbc:mysql://localhost:3306/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
  db_sms_username=<local-user>
  db_sms_password=<local-pass>
  sms_app_secret=<any-strong-string>
  ```
* In **Program arguments**, disable AWS import:

  ```
  --spring.config.import=
  ```
* Start a local MySQL (container or native), then run the app from IntelliJ.
* Expect `http://localhost:8080/` → **401**.

> Alternative: run DB+API with `docker compose` (see the provided compose file and `.env.example` for local).

---

## Android client (how it ties in)

* Retrofit + OkHttp (BOM-pinned).
* `AuthInterceptor` adds the bearer token each call.
* `TokenAuthenticator` handles 401 → `/auth/refresh` → retry once → on failure, **single** session-expired event and redirect to Login.
* `TokenStore` uses **EncryptedSharedPreferences** (AES-GCM) with safe migration; `SessionManager` debounces duplicate messages.
* Profile screens cache “self” responses briefly to keep the UI snappy.

---

## Troubleshooting

* **401 on root**: normal; indicates the app is alive and secured by Spring Security.
* **Secrets not loading on EC2**: check the **instance role**, **secret name/region**, and `spring.config.import` value.
* **Local run hits AWS**: you forgot `--spring.config.import=` in program args.
* **DB version mismatch**: reuse a MySQL image compatible with your data dir (e.g., `mysql:8.1`).

---

## Short “How do I run this like you did?” (cheat sheet)

### Production-style (EC2 + Docker + Secrets Manager)

1. Launch an **EC2 Ubuntu** instance and install Docker.
2. `docker network create smsnet`
3. Run MySQL on that network with a persistent **named volume**.
4. Create an **IAM role** allowing `GetSecretValue` on `prod/sms/backend`; attach to EC2.
5. Create the **Secrets Manager** secret with DB creds + JWT secret. Point `db_sms_url` to the **container name**.
6. Build the fat JAR locally, **SCP** it to EC2, and run it in a **Java 17** container on `smsnet`.
7. Put ALB/NGINX in front for HTTPS. Use **SSH tunnel** for Workbench; don’t expose 3306.

### Local laptop

1. Start a local MySQL (or connect to EC2 via SSH tunnel).
2. In IntelliJ, set env vars (`db_sms_*`, `sms_app_secret`) and **disable** AWS import with `--spring.config.import=`.
3. Run the app; expect `401` on `/`.

---

## License

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/ahmadyardimli/student-management-system-spring-boot-backend/blob/main/LICENSE)  
This project is licensed under the **MIT License** — see the
[LICENSE](https://github.com/ahmadyardimli/student-management-system-spring-boot-backend/blob/main/LICENSE) file for details.

---

If you’re reviewing this as part of my portfolio: this project is about **secure, realistic backend engineering**—not just code that runs, but code that **deploys safely**, **manages secrets correctly**, and **treats tokens and sessions with care**.





