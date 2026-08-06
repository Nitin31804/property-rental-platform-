# Dacha Stays 🏡

A modern, full-stack luxury property rental and hospitality management platform built from scratch. 

Dacha Stays solves the problem of hidden fees and unclear liabilities in the premium rental market by offering transparent, dynamic pricing and strict, enforceable house rules directly inside a seamless checkout flow.

## 🚀 Features
*   **Modern Vanilla UI:** Designed with a premium "Bento-Grid" property gallery, glassmorphism aesthetics, and professional typography (Outfit & Playfair Display fonts)—all without relying on heavy frontend frameworks like Bootstrap or Tailwind.
*   **Dynamic Checkout Engine:** Real-time, JavaScript-powered dynamic pricing calculates total costs instantly as guests select premium add-ons (e.g., Extra Mattress, Airport Transfer).
*   **Strict Access Control:** Robust Java Servlet Session management ensures strict Role-Based Access Control (RBAC). Unauthenticated users are completely blocked from checkout and admin panels.
*   **Admin Master Ledger:** A secure backend portal providing administrators with a bird's-eye view of all system bookings, tracking specific metrics like guest ages, accurate guest counts, and special requests.
*   **Legal/Liability Enforcement:** Mandatory digital agreement to cleanliness and property inspection policies built directly into the booking flow to protect hosts.

## 💻 Tech Stack
*   **Frontend:** HTML5, CSS3 (Vanilla), JavaScript (ES6+), Flatpickr (Calendar library)
*   **Backend:** Java 8, Java Servlets (J2EE Architecture), Google Gson (for JSON parsing)
*   **Database:** MySQL 8.0 (connected via JDBC)
*   **Server Environment:** Apache Tomcat 9.0
*   **Architecture Pattern:** DAO (Data Access Object) Pattern & decoupled RESTful API design.

## ⚙️ How to Run Locally

### Prerequisites
1.  Install **Java JDK 8** (or higher).
2.  Install **MySQL 8.0**.
3.  Install **Apache Tomcat 9.0**.

### Database Setup
1. Open MySQL Workbench.
2. Create a database named `property_rental_db`.
3. Import the provided `property_rental_db.sql` database dump file to automatically build the schema and populate the demo data.

### Server Setup
1. Copy all Java backend files to your Tomcat `WEB-INF/classes/` directory and compile them using `javac`.
2. Copy the frontend HTML/CSS/JS files directly into Tomcat's `webapps/property-rental-app/` directory.
3. Start the Tomcat server.
4. Navigate to `http://localhost:8080/property-rental-app/index.html` (Note: Ensure your Tomcat port matches, it defaults to 8080 but may be configured to 9090).

## 🔒 Default Credentials
To access the Admin panel for evaluation:
*   **Email:** `admin@dachastays.com`
*   **Password:** `admin`

---
*Developed as a Final Year Computer Science Project.*
