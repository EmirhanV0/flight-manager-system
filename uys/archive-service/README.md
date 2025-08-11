# UYS Archive Service

Archive and logging microservice for UYS project.

## 🚀 Quick Start

```bash
# Build and run with Docker
docker build -t uys-archive-service .
docker run -p 8083:8080 uys-archive-service

# Or with Maven
mvn spring-boot:run
```

## 📚 API Documentation

- **Swagger UI**: http://localhost:8083/api/swagger-ui.html
- **API Docs**: http://localhost:8083/api/v3/api-docs
- **Health Check**: http://localhost:8083/api/actuator/health

## 🛠 API Endpoints

### Event Log Management
- `POST /api/event-logs` - Create event log
- `GET /api/event-logs/{id}` - Get event log
- `GET /api/event-logs` - List event logs (paginated)
- `GET /api/event-logs/search` - Search event logs

### Audit Log Management  
- `POST /api/audit-logs` - Create audit log
- `GET /api/audit-logs` - List audit logs

## 🔧 Configuration

- **Service Port**: 8083
- **Database**: PostgreSQL (uys_archive)
- **Kafka Consumer**: Listens to reference.events, flight.events, system.events

## 📊 Monitoring

- **Prometheus**: http://localhost:8083/api/actuator/prometheus
- **Health**: http://localhost:8083/api/actuator/health

## 🗄 Database

Uses PostgreSQL database `uys_archive` with Liquibase migrations.

## 📝 Status

✅ **Phase B3.1 Completed** - Basic Archive Service structure