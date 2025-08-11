# UYS Project - Local Development Guide

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- IDE (VS Code, IntelliJ IDEA, Eclipse)

### Build & Run

#### 1. Build All Services
```bash
mvn clean install -DskipTests
```

#### 2. Run Services (Local Profile)

**Reference Manager (Port 8080):**
```bash
cd reference-manager
mvn spring-boot:run
```

**Flight Service (Port 8082):**
```bash
cd flight-service
mvn spring-boot:run -Dspring.profiles.active=local
```

**Archive Service (Port 8083):**
```bash
cd archive-service
mvn spring-boot:run -Dspring.profiles.active=local
```

### 🗄️ Local Database Access

All services use **H2 In-Memory Database** for local development:

- **Reference Manager**: `http://localhost:8080/api/h2-console`
- **Flight Service**: `http://localhost:8082/api/h2-console`
- **Archive Service**: `http://localhost:8083/api/h2-console`

**H2 Console Settings:**
- JDBC URL: `jdbc:h2:mem:testdb` (Reference Manager)
- JDBC URL: `jdbc:h2:mem:flightdb` (Flight Service)
- JDBC URL: `jdbc:h2:mem:archivedb` (Archive Service)
- Username: `sa`
- Password: (empty)

### 📚 API Documentation

- **Reference Manager**: `http://localhost:8080/api/swagger-ui.html`
- **Flight Service**: `http://localhost:8082/api/swagger-ui.html`
- **Archive Service**: `http://localhost:8083/api/swagger-ui.html`

### 🔍 Health Checks

- **Reference Manager**: `http://localhost:8080/api/actuator/health`
- **Flight Service**: `http://localhost:8082/api/actuator/health`
- **Archive Service**: `http://localhost:8083/api/actuator/health`

## 🏗️ Architecture

### Local Development Stack
- **Database**: H2 In-Memory
- **Cache**: In-Memory (Simple Cache)
- **Message Broker**: Embedded Kafka (if needed)
- **API Documentation**: SpringDoc OpenAPI

### Service Ports
- Reference Manager: `8080`
- Flight Service: `8082`
- Archive Service: `8083`

## 🧪 Testing

### Unit Tests Only (Local Development)
```bash
mvn test -DskipITs
```

### All Tests (CI/CD)
```bash
mvn verify
```

## 🔧 Configuration

### Local Profiles
- `application.yml` - Default configuration
- `application-local.yml` - Local development overrides

### Key Local Settings
- H2 In-Memory Database
- Liquibase disabled
- Redis disabled
- Kafka embedded (if needed)
- In-Memory caching

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

2. **Maven Build Issues**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Database Connection Issues**
   - Check H2 console is accessible
   - Verify JDBC URL in application logs

### Logs
- Log files: `logs/` directory in each service
- Console logs: Check IDE console or terminal output

## 📝 Development Workflow

1. **Start with Reference Manager** (Core data)
2. **Develop Flight Service** (Business logic)
3. **Add Archive Service** (Logging/Audit)

### Vertical Slice Development
- Complete one feature end-to-end before moving to next
- Test each service independently
- Use Postman/curl for API testing

## 🚀 Next Steps

After local development is complete:
1. Add Docker support
2. Enable integration tests
3. Set up CI/CD pipeline
4. Deploy to staging/production
