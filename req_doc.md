# Requirements Document – MaaS  
## Fleet & Vehicle Rental Management System

---

## 1. Purpose

This document describes the functional and non-functional requirements for a system that manages a vehicle fleet and rental operations. The goal is to provide a centralized platform for administrators, operators, and clients to manage vehicles, rentals, maintenance, and reporting efficiently.

---

## 2. Business Scenario

A company operates a fleet of vehicles available for rental. Clients search and rent vehicles, operators manage contracts and vehicle assignments, and administrators oversee fleet maintenance, compliance, and reporting. The system automates validations and notifications to reduce operational risks.

---

## 3. Actors

- **Client** – searches and rents vehicles  
- **Operator** – manages rental contracts and vehicle assignments  
- **Admin** – manages fleet, users, and reports  
- **System** – performs automated checks and notifications  

---

## 4. Scope

### In Scope
- User authentication and role-based access
- Vehicle fleet management
- Rental contract creation and validation
- Maintenance, insurance, and towing tracking
- Reporting and analytics
- Automated notifications

### Out of Scope
- Payment processing
- External insurance provider integrations

---

## 5. Functional Requirements

### Authentication & User Management
- **FR-01** The system shall allow users to securely log in using username and password.
- **FR-02** The system shall support role-based access control (Admin, Operator, Client).
- **FR-03** Admins shall be able to manage users and assign roles.

### Vehicle Management
- **FR-04** Admins shall be able to add new vehicles to the fleet.
- **FR-05** Admins shall be able to update vehicle details (mileage, status, license category).
- **FR-06** Admins shall be able to remove decommissioned vehicles.
- **FR-07** Clients shall be able to search vehicles using multiple criteria.
- **FR-08** Operators shall be able to search vehicles by registration or chassis number.
- **FR-09** Operators shall be able to view all available vehicles.

### Rental Contracts
- **FR-10** Operators shall be able to create rental contracts between clients and vehicles.
- **FR-11** The system shall prevent overlapping rental contracts for the same vehicle.

### Maintenance & Events
- **FR-12** Admins shall be able to record vehicle service entries.
- **FR-13** Admins shall be able to manage vehicle insurance details (RCA/CASCO).
- **FR-14** Operators shall be able to register towing events.
- **FR-15** Admins shall be able to view full maintenance and towing history per vehicle.
- **FR-16** Admins shall be able to view rental history per vehicle.

### Automation & Reporting
- **FR-17** The system shall automatically notify admins of expired insurance or overdue service.
- **FR-18** Admins shall be able to generate monthly fleet reports.
- **FR-19** The system shall automatically verify client eligibility before rental.

---

## 6. Non-Functional Requirements

- **NFR-01** The system shall enforce security best practices for authentication and authorization.
- **NFR-02** The system shall respond to user requests within acceptable performance limits.
- **NFR-03** The system shall be accessible via modern web browsers.
- **NFR-04** The system shall ensure data consistency and integrity.
- **NFR-05** The system shall support future scalability.

---

## 7. Business Rules

- A vehicle cannot have more than one active rental contract for the same period.
- Only admins can add or remove vehicles.
- Only eligible clients may rent vehicles.
- Automated notifications must not require manual intervention.

---

## 8. Traceability (Requirements ↔ User Stories)

Each functional requirement is directly derived from one or more user stories provided by the Product Owner, ensuring full traceability between business needs and system behavior.