# Property Rental Platform

![Next.js](https://img.shields.io/badge/Next.js-16-black?style=for-the-badge&logo=next.js&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue?style=for-the-badge&logo=typescript&logoColor=white)
![Prisma](https://img.shields.io/badge/Prisma-ORM-2D3748?style=for-the-badge&logo=prisma&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

> A full-stack property rental web platform with secure authentication, property listings, booking management and an OTP-based password reset system.

---

## Features

- **Secure Authentication** — Bcrypt password hashing, HTTP-only cookie sessions
- **OTP Password Reset** — Forgot password flow with 6-digit OTP via email/SMS
- **DoS Rate Limiting** — IP-based request throttling on all auth endpoints (5 req/min)
- **Type-Safe Database** — Prisma ORM with full TypeScript schema
- **Server Actions** — Next.js 16 Server Actions for zero-boilerplate API layer
- **Crash-Proof Routing** — NaN URL parameter guard returns 404 instead of 500 crash
- **Database Seeding** — Automated seed script for instant local dev setup

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Next.js 16 (App Router + Server Actions) |
| Language | TypeScript |
| Styling | Tailwind CSS |
| ORM | Prisma |
| Database | MySQL |
| Auth Security | Bcrypt + HTTP-only Cookies |

---

## Getting Started

```bash
# Clone the repository
git clone https://github.com/Nitin31804/property-rental-platform-.git
cd property-rental-platform-

# Install dependencies
npm install

# Set up your database connection in .env
DATABASE_URL="mysql://user:password@localhost:3306/rental_db"

# Push schema and seed database
npx prisma db push
npx ts-node prisma/seed.ts

# Start development server
npm run dev
```

---

## User Flows

```
Register --> Bcrypt Hash --> MySQL
Login --> Bcrypt Compare --> HTTP-only Cookie Session
Forgot Password --> Random OTP --> HTTP-only Cookie --> Reset
Browse Properties --> Filter --> Book
```

---

## Security Architecture

| Threat | Protection |
|---|---|
| Password Theft | Bcrypt hashing (cost factor 10) |
| Session Hijacking | HTTP-only Secure Cookies |
| Brute Force / DoS | IP Rate Limiting (5 req/min) |
| IDOR Attack | Server-side cookie validation |
| URL Injection | NaN parameter guard |
| Duplicate Email Crash | Prisma P2002 try/catch |

---

## Project Structure

```
app/
|-- page.tsx              # Property listing homepage
|-- properties/[id]/      # Dynamic property detail (NaN-protected)
|-- login/                # Auth with rate limiting
|-- register/             # Registration with bcrypt
|-- forgot-password/      # OTP request
|-- reset-password/       # OTP verification + password update
lib/
|-- prisma.ts             # Database client singleton
|-- rateLimit.ts          # IP-based rate limiter
prisma/
|-- schema.prisma         # Full database schema
|-- seed.ts               # Demo data seeder
```

