Got it — I kept everything exactly the same and only adjusted the JDBC URL placeholders to clearly distinguish **local** vs **production**.

---

# student-management-system-spring-boot-backend

Production-ready Spring Boot 3 backend for Student Management — **Java 17**, **Docker**, **MySQL 8**, **JPA/Hibernate**, **JWT (access + rotating refresh)**, and configuration via **AWS Secrets Manager** on **EC2**.

---

> ### Docs live on the `development` branch
>
> I keep detailed, evolving documentation on the **development** branch while keeping `main` clean and stable.
>
> • 👉 **Full README:** [backend README (development)](https://github.com/ahmadyardimli/student-management-system-spring-boot-backend/blob/development/README.md)
>
> • **Browse branch:** [development branch](https://github.com/ahmadyardimli/student-management-system-spring-boot-backend/tree/development)

---

## Highlights

* Secure **JWT**: short-lived access + **rotating refresh** tokens
* **12-factor config**: secrets in **AWS Secrets Manager** (no secrets in VCS)
* **Dockerized**: API and MySQL as separate containers on a private Docker network
* Clean **JPA/Hibernate** layering (controllers → services → repositories)

## Quick start (local)

1. Make sure MySQL 8 is running locally.
2. Run the app from your IDE with these environment variables:

```
# Local laptop/dev
db_sms_url=jdbc:mysql://localhost:3306/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db_sms_username=<DB_USERNAME>
db_sms_password=<DB_PASSWORD>
sms_app_secret=<JWT_SECRET>
```

3. In program arguments, disable AWS import:

```
--spring.config.import=
```

> Opening `http://localhost:8080/` should return **401** (expected: app is up & secured).

## Production snapshot

* EC2 (Ubuntu) with Docker network **`smsnet`**

  * `sms_backend` (Java 17 JRE image)
  * `zeka_tm_qebele_mysql_db` (MySQL 8, named volume)
* Secrets in **`prod/sms/backend`** (AWS Secrets Manager), read via **EC2 IAM role**
* JDBC points to DB by **container name** (inside Docker network):
  `jdbc:mysql://<DB_CONTAINER_NAME>:3306/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

Full step-by-step: see the **development README** (link above).

## Branching

I develop on **`development`** and merge to **`main`** for stable releases.

```
git switch development
git switch -c feature/some-change
git push -u origin feature/some-change  # PR: feature → development → main
```

## License

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
