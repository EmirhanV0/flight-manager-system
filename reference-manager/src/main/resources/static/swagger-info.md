# 📖 UYS Reference Manager API Documentation

## 🚀 Quick Start

### Swagger UI Access
- **Local Development**: http://localhost:8081/api/swagger-ui.html
- **API Docs JSON**: http://localhost:8081/api/v3/api-docs

### Authentication
Currently using basic authentication:
- **Username**: admin
- **Password**: admin

## 📋 API Groups

### 🏢 Airline Management
- **Base Path**: `/airlines`
- **Features**: CRUD operations, search, status management
- **Key Endpoints**:
  - `POST /airlines` - Create new airline
  - `GET /airlines/{code}` - Get airline by code
  - `PUT /airlines/{code}` - Update airline
  - `DELETE /airlines/{code}` - Delete airline
  - `PATCH /airlines/{code}/status` - Change status
  - `GET /airlines/active` - List active airlines
  - `GET /airlines/search` - Search airlines

### ✈️ Aircraft Management  
- **Base Path**: `/aircraft`
- **Features**: CRUD operations, airline association
- **Key Endpoints**:
  - `POST /aircraft` - Create new aircraft
  - `GET /aircraft/{registration}` - Get aircraft by registration
  - `PUT /aircraft/{registration}` - Update aircraft
  - `GET /aircraft/airline/{airlineCode}` - Get aircraft by airline

### 🛫 Station Management
- **Base Path**: `/stations`
- **Features**: CRUD operations, geographical search
- **Key Endpoints**:
  - `POST /stations` - Create new station
  - `GET /stations/{code}` - Get station by code
  - `PUT /stations/{code}` - Update station
  - `GET /stations/country/{country}` - Get stations by country

## 🔧 Example Requests

### Create Airline
```json
POST /airlines
{
  "airlineCode": "TK",
  "airlineName": "Turkish Airlines", 
  "country": "Turkey",
  "city": "Istanbul",
  "description": "National flag carrier of Turkey"
}
```

### Create Aircraft
```json
POST /aircraft
{
  "registration": "TC-JJA",
  "aircraftType": "A350",
  "model": "A350-900",
  "manufacturer": "Airbus",
  "capacity": 314,
  "maxRange": 15000,
  "cruiseSpeed": 903,
  "airlineCode": "TK"
}
```

### Create Station
```json
POST /stations
{
  "stationCode": "IST",
  "stationName": "Istanbul Airport",
  "city": "Istanbul",
  "country": "Turkey",
  "timezone": "Europe/Istanbul",
  "latitude": 41.275278,
  "longitude": 28.751944,
  "altitude": 106
}
```

## 📊 Response Formats

### Success Response
```json
{
  "id": 1,
  "airlineCode": "TK",
  "airlineName": "Turkish Airlines",
  "country": "Turkey",
  "city": "Istanbul", 
  "description": "National flag carrier of Turkey",
  "active": true,
  "createdAt": "2024-07-30T10:15:30",
  "updatedAt": "2024-07-30T10:15:30",
  "version": 0
}
```

### Error Response
```json
{
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed for request",
  "path": "/api/v1/airlines",
  "timestamp": "2024-07-30T10:15:30",
  "details": [
    {
      "field": "airlineCode",
      "rejectedValue": "",
      "message": "Airline code is required"
    }
  ]
}
```

## 🔍 Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid request data |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Duplicate resource |
| 500 | Internal Server Error |

## 🧪 Testing

### Health Check
```bash
curl http://localhost:8081/api/actuator/health
```

### Get All Airlines (Paginated)
```bash
curl "http://localhost:8081/api/airlines?page=0&size=10&sort=airlineName,asc"
```

### Search Airlines
```bash
curl "http://localhost:8081/api/airlines/search?query=Turkish"
```

## 📈 Monitoring

- **Metrics**: http://localhost:8081/api/actuator/prometheus
- **Info**: http://localhost:8081/api/actuator/info
- **Health**: http://localhost:8081/api/actuator/health

## 🔗 Related Services

- **Kafka UI**: http://localhost:8080
- **Grafana**: http://localhost:3001
- **Prometheus**: http://localhost:9090