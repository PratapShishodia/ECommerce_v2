# 🛒 E-Commerce Microservices Platform

A scalable, production-ready E-Commerce application built using **Spring Boot Microservices** following modern software architecture and cloud-native principles. The project demonstrates both **Synchronous** and **Asynchronous** communication between services while maintaining loose coupling, scalability, and fault tolerance.

---

# 🚀 Tech Stack

## Backend
- Java 21
- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- Spring Cloud
- Spring Validation
- JWT Authentication
- Maven

## Frontend
- React
- Vite
- Axios
- React Router

## Database
- MySQL

## Messaging
- Apache Kafka

## Payment Gateway
- Razorpay

## Notification
- Java Mail Sender (Email)

---

# 🏗️ Microservices

## 1. Auth Service

Responsible for authentication and authorization.

### Features
- User Registration
- User Login
- JWT Token Generation
- JWT Validation
- Role-Based Authorization
- Secure API Access

---

## 2. Product Service

Manages product catalog.

### Features
- Add Product
- Update Product
- Delete Product
- Product Search
- Product Filtering
- Product Sorting
- Product Details

---

## 3. Inventory Service

Maintains product stock.

### Features
- Check Stock
- Update Stock
- Deduct Stock
- Inventory Management

---

## 4. Cart Service

Handles user shopping carts.

### Features
- Add Item
- Remove Item
- Update Quantity
- Clear Cart
- View Cart

---

## 5. Order Service

Processes customer orders.

### Features
- Place Order
- Order History
- Order Details
- Order Status

---

## 6. Payment Service

Handles payment processing.

### Features
- Initiate Payment
- Verify Payment
- Refund Payment
- Razorpay Integration

---

## 7. Notification Service

Responsible for sending notifications.

### Features
- Email Notifications
- Order Confirmation Emails
- Payment Success Emails
- Refund Notifications

---

# 🔄 Service Communication

The project uses a combination of **Synchronous** and **Asynchronous** communication patterns.

## Synchronous Communication

Used when an immediate response is required.

Examples:

- Order Service → Inventory Service
- Order Service → Product Service
- Payment Service → Order Service

**Technology**
- REST APIs
- OpenFeign Client

---

## Asynchronous Communication

Used for event-driven workflows where immediate responses are not required.

Examples:

- Order Placed
- Payment Successful
- Payment Failed
- Order Confirmed
- Refund Initiated

**Technology**
- Apache Kafka

Example Flow:

```
Order Service
      │
      ▼
Publish Order Event
      │
      ▼
Kafka Topic
      │
      ▼
Notification Service
      │
      ▼
Send Email
```

---

# 🔐 Authentication

Authentication is implemented using **JWT (JSON Web Token)**.

Flow:

```
Login
   │
   ▼
Generate JWT
   │
   ▼
Client Stores Token
   │
   ▼
Token Sent with Every Request
   │
   ▼
JWT Validation
   │
   ▼
Access Granted
```

---

# 📦 Project Structure

```
ecommerce-microservices
│
├── auth-service
├── product-service
├── inventory-service
├── cart-service
├── order-service
├── payment-service
├── notification-service
├── frontend
└── README.md
```

---

# 📌 Architecture Overview

```
                    React + Vite
                          │
                          ▼
                    Auth Service
                          │
         ┌────────────────┼────────────────┐
         ▼                ▼                ▼
 Product Service    Cart Service     Order Service
         │                │                │
         │                │                ▼
         │                │        Inventory Service
         │                │
         │                ▼
         │         Payment Service
         │                │
         └──────────────┬─┘
                        ▼
                  Apache Kafka
                        │
                        ▼
             Notification Service
                        │
                        ▼
                    Email Service
```

---

# ✨ Key Features

- Microservices Architecture
- JWT Authentication
- Role-Based Access Control
- REST APIs
- Apache Kafka Event Messaging
- OpenFeign Client
- Razorpay Payment Integration
- Email Notifications
- Product Search
- Product Filtering
- Product Sorting
- Inventory Management
- Shopping Cart
- Order Management
- Refund Processing
- Clean Layered Architecture
- Production-Oriented Design

---

# 🛠️ Future Enhancements

- API Gateway
- Service Discovery (Eureka)
- Config Server
- Redis Caching
- Circuit Breaker (Resilience4j)
- Distributed Tracing
- Centralized Logging
- Docker
- Docker Compose
- Kubernetes
- Prometheus & Grafana Monitoring
- ELK Stack
- CI/CD Pipeline
- Swagger/OpenAPI Documentation

---

# 📚 Learning Objectives

This project demonstrates:

- Microservices Design
- Event-Driven Architecture
- Synchronous vs Asynchronous Communication
- Spring Security with JWT
- Kafka Producer & Consumer
- OpenFeign Client
- Payment Gateway Integration
- Email Notification Service
- REST API Development
- Production-Level Project Structure
- Scalable Backend Development

---

# 👨‍💻 Author

**Pratap Shishodia**

Java Backend Developer

---

## ⭐ If you found this project helpful, consider giving it a Star.