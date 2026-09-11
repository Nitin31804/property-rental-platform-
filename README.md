# Property Rental Platform

## Overview
A modern, scalable full-stack web application for booking and hosting rental properties. Built from the ground up using React Server Components for maximum SEO and performance.

## Tech Stack
- **Frontend/Backend:** Next.js 14+ (App Router)
- **Styling:** Tailwind CSS v3
- **Database:** MySQL
- **ORM:** Prisma
- **Security:** Bcrypt (Cryptographic Hashing), HTTP-Only Secure Cookies

## Key Features
- **Secure Authentication:** Protection against IDOR and DoS attacks via in-memory Rate Limiting and secure HttpOnly cookie sessions.
- **Type-Safe Database Queries:** Prisma ensures strict TypeScript adherence from the UI down to the database schema.
- **Graceful Error Handling:** Explicit edge-case trapping (e.g., catching `NaN` router crashes and Prisma `P2002` unique constraint violations).
- **Database Seeding:** Automated mock data generation for streamlined developer onboarding.

## Getting Started
1. Start your local MySQL server.
2. Install dependencies: 
   ```bash
   npm install
   ```
3. Seed the database with mock properties and admins: 
   ```bash
   npm run prisma:seed
   ```
4. Start the development server: 
   ```bash
   npm run dev
   ```
