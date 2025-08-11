# UYS Flight Service

Flight management microservice for UYS project.

## 🚀 Quick Start

```bash
# Build and run with Docker
docker build -t uys-flight-service .
docker run -p 8082:8080 uys-flight-service

# Or with Maven
mvn spring-boot:run
```

## 📚 API Documentation

- **Swagger UI**: http://localhost:8082/api/swagger-ui.html
- **API Docs**: http://localhost:8082/api/v3/api-docs
- **Health Check**: http://localhost:8082/api/actuator/health

## 🛠 API Endpoints

### Flight Management
- `POST /api/flights` - Create new flight
- `GET /api/flights/{id}` - Get flight by ID
- `GET /api/flights/number/{flightNumber}` - Get flight by number
- `PUT /api/flights/{id}` - Update flight
- `DELETE /api/flights/{id}` - Delete flight
- `GET /api/flights` - List all flights (paginated)

### Flight Operations
- `PATCH /api/flights/{id}/status` - Update flight status
- `PATCH /api/flights/{id}/delay` - Delay flight
- `GET /api/flights/airline/{airlineCode}` - Get flights by airline
- `GET /api/flights/route` - Get flights by route
- `GET /api/flights/time-range` - Get flights by time range

## 🔧 Configuration

Service runs on port **8082** by default.

## 📊 Monitoring

- **Prometheus**: http://localhost:8082/api/actuator/prometheus
- **Health**: http://localhost:8082/api/actuator/health

## 🗄 Database

Uses MySQL database `uys_flight` with Liquibase migrations.

## 🧪 Testing

```bash
mvn test
```

## 📝 Status

✅ **Phase B2 Completed** - Basic Flight Service with CRUD operations