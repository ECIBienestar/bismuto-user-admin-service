# ECI-Bienestar User Administration Service

User administration microservice for the ECI-Bienestar platform. Manages user profiles, roles, permissions, and scheduling configurations for wellness services at Escuela Colombiana de Ingeniería Julio Garavito.

## 👥 Authors

* Andrés Felipe Chavarro Plazas
* Andrés Jacobo Sepúlveda Sánchez
* Camilo Andrés Fernández Días
* Jesús Alfonso Pinzón Vega

## 📌 Project Overview
This microservice is part of the ECI-Bienestar integrated platform designed for Escuela Colombiana de Ingeniería Julio Garavito. The User Administration Service is built using **Spring Boot** and provides comprehensive user management capabilities including profile management, role assignment, and service scheduling configuration.

## 🛠️ Technologies Used
- **Java 17**
- **Spring Boot 3.x** (Spring Web, Spring Data)
- **MongoDB** (NoSQL Database)
- **Maven**
- **Lombok**
- **JUnit 5 & Mockito** (for testing)
- **JaCoCo** (for code coverage)
- **SonarCloud** (for code quality)
- **MapStruct** (for object mapping)

## 📂 Project Structure

```
bismuto-user-admin-service/
├── pom.xml
├── .gitignore
├── README.md
├── assets/
└── src/
    ├── main/
    │   ├── java/
    │   │   └── edu/eci/cvds/users/
    │   │       ├── UserAdminApplication.java
    │   │       ├── config/            # Configuration classes
    │   │       ├── controller/        # REST Controllers
    │   │       ├── dto/               # Data Transfer Objects
    │   │       │   └── enum/          # Enumerations
    │   │       ├── exception/         # Custom Exception Handling
    │   │       ├── model/             # Entity Classes
    │   │       ├── service/           # Business Logic Services
    │   │       └── util/              # Utility classes
    │   └── resources/
    │       ├── application.properties       # Default configuration
    │       ├── application-dev.properties   # Development configuration
    │       ├── application-prod.properties  # Production configuration
    │       ├── static/                      # Static resources
    │       └── templates/                   # Templates
    └── test/
        └── java/
            └── edu/eci/cvds/users/
                └── UserAdminApplicationTest.java
```

### Class Diagram:

![DiagramaClasesUsuarios](https://github.com/user-attachments/assets/572dd755-e266-4a6a-a6d6-fa6559265052)

### Component Diagram:

![DiagramaComponentesUsuarios](https://github.com/user-attachments/assets/ed857946-ba6e-4b0f-8d86-2fe73388e58a)

### Entity-relationship model:

![DiagramaDatosUsuariosEditable](https://github.com/user-attachments/assets/6d430187-43e3-48c1-8e92-01d18da97c71)

## 🚀 How to Run the Project

### Prerequisites
- Install **Java 17**
- Install **Maven**
- Set up a **MongoDB** database

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/ECIBienestar/bismuto-user-admin-service.git
   cd bismuto-user-admin-service
   ```

2. Configure database connection in `application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/eci_bienestar_users
   spring.data.mongodb.database=eci_bienestar_users
   ```

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## 📌 API Endpoints

### User Management

| Method | Endpoint              | Description                            |
|--------|----------------------|----------------------------------------|
| GET    | /api/users           | Get all users                           |
| GET    | /api/users/{id}      | Get user by ID                          |
| POST   | /api/users           | Create a new user                       |
| PUT    | /api/users/{id}      | Update user profile                     |
| DELETE | /api/users/{id}      | Delete user                             |

### Role Management

| Method | Endpoint              | Description                            |
|--------|----------------------|----------------------------------------|
| GET    | /api/roles           | Get all roles                           |
| GET    | /api/roles/{id}      | Get role by ID                          |
| POST   | /api/roles           | Create a new role                       |
| PUT    | /api/roles/{id}      | Update role                             |
| DELETE | /api/roles/{id}      | Delete role                             |
| POST   | /api/users/{id}/roles| Assign role to user                     |

### Schedule Configuration

| Method | Endpoint                        | Description                               |
|--------|--------------------------------|-------------------------------------------|
| GET    | /api/schedules                  | Get all schedule configurations           |
| GET    | /api/schedules/{id}             | Get schedule by ID                        |
| POST   | /api/schedules                  | Create new schedule configuration         |
| PUT    | /api/schedules/{id}             | Update schedule configuration             |
| DELETE | /api/schedules/{id}             | Delete schedule configuration             |
| GET    | /api/schedules/service/{type}   | Get schedules by service type             |

### User Profile Data

| Method | Endpoint                        | Description                               |
|--------|--------------------------------|-------------------------------------------|
| GET    | /api/users/{id}/profile         | Get user profile data                     |
| PUT    | /api/users/{id}/profile         | Update user profile data                  |
| GET    | /api/users/{id}/services        | Get user service access                   |
| PUT    | /api/users/{id}/services        | Update user service access                |

## 🧪 Running Tests

To run the unit tests:
```bash
mvn test
```

To generate test coverage report:
```bash
mvn test jacoco:report
```

## 🔄 CI/CD

This project uses GitHub Actions to automate building, testing, and deployment:

- **Development Pipeline**: Triggered by commits to `develop` and `feature/*` branches, automatically deploying to the development environment.
- **Production Pipeline**: Triggered by commits to the `main` branch, deploying to the production environment after successful tests.

Each pipeline consists of three stages:
1. **Build**: Compiles the application and creates the JAR package
2. **Test**: Runs unit tests and generates code coverage reports
3. **Deploy**: Deploys the application to the AWS environment

Configuration files are located in the `.github/workflows/` directory.

## 🔄 Integration with Other Modules

This user administration service integrates with other ECI-Bienestar modules:
- **Authentication Service**: For secure user identity management
- **Statistics Service**: For user activity reporting
- **All service modules**: For providing user authorization and profile data

## 📊 Key Features

- **Comprehensive User Management**: CRUD operations for user profiles
- **Role-Based Access Control**: Manage roles and permissions for different user types
- **Schedule Configuration**: Define availability for services and resources
- **Profile Management**: Maintain detailed profiles for all system users
- **Batch Operations**: Support for bulk user management operations

## 🏗️ Future Improvements

- Enhanced role hierarchy management
- Advanced scheduling algorithms for optimized resource allocation
- Improved integration with identity providers
- Self-service user profile management features
- Enhanced audit logging for user management operations

## 📝 License

This project is licensed under the MIT License.
