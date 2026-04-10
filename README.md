Aura Store
Aura Store is a full-featured e-commerce backend built with Spring Boot, Spring Data JPA, and PostgreSQL. It powers the complete shopping journey from product discovery to cart management, order placement, payment processing, shipment tracking, and delivery status updates.

Designed as a clean and scalable backend foundation, Aura Store focuses on real-world commerce workflows instead of only basic CRUD operations. The project demonstrates layered architecture, domain modeling, DTO-based APIs, persistence with JPA, and automated testing of both business logic and REST endpoints.

Features
User registration
Address management
Product catalog APIs
Cart creation and cart item management
Order placement from cart
Payment processing workflow
Shipment creation and shipment status updates
Order tracking
DTO-based request and response models
Automated unit and integration tests
Tech Stack
Java 21
Spring Boot 3
Spring Web
Spring Data JPA
PostgreSQL
H2 Database for tests
JUnit 5
Mockito
Maven
Lombok
Project Structure
src/main/java/com/ecommerce/aura_store
├── controller
├── dto
├── entity
├── repository
└── service
API Overview
Base path: /api

Main endpoints include:

POST /api/user
POST /api/address/add/{userId}
GET /api/products
GET /api/products/{productId}
GET /api/cart/{userId}
POST /api/cart/add
PUT /api/cart/item/{cartItemId}
DELETE /api/cart/item/{cartItemId}
POST /api/orders/place
GET /api/orders/{orderId}
POST /api/payments/process
PUT /api/shipments/{shipmentId}
GET /api/orders/{orderId}/track
Architecture
Aura Store follows a layered backend architecture:

Controller handles HTTP requests and responses
Service contains business logic
Repository manages database access
Entity models the domain
DTO keeps API contracts separate from persistence objects
This structure keeps the codebase organized, readable, and easy to extend.

Running Locally
1. Clone the repository
git clone https://github.com/your-username/aura-store.git
cd aura-store/aura-store
2. Configure environment variables
Set the following values before starting the application:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/auraStore
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
3. Run the application
Using Maven Wrapper:

./mvnw spring-boot:run
On Windows:

mvnw.cmd spring-boot:run
Running Tests
./mvnw clean test
On Windows:

mvnw.cmd clean test
The test suite uses an isolated H2 in-memory database and covers both service-layer behavior and API endpoint integration flows.

Highlights
Real commerce lifecycle implementation
Clean Spring Boot project structure
Safer enum persistence
Externalized database configuration
Integration-tested REST API flow
Strong starting point for a production-scale e-commerce platform
Current Status
Aura Store is a strong backend foundation and portfolio-ready project. It already supports the full shopping workflow and includes meaningful automated testing. The next steps toward full production readiness would be authentication, authorization, payment gateway integration, robust exception handling, schema migrations, and a dedicated frontend client.

Future Improvements
Spring Security with JWT authentication
Password hashing
Role-based access control
Flyway or Liquibase migrations
Real payment gateway integration
Admin dashboard
Frontend application in React or Next.js
Docker support
CI/CD pipeline
Monitoring and observability
Why This Project Stands Out
Aura Store is not just a CRUD demo. It models the core mechanics of a real online store and shows how to design backend systems around business workflows, not only database tables. It is a practical, extensible codebase that can evolve into a full-stack production-grade commerce platform.

License
This project is open for learning, experimentation, and further development. Add your preferred license here before publishing publicly.

If you want, I can also turn this into a polished README.md file in your repo with badges, setup sections, sample request bodies, and a professional GitHub layout.
