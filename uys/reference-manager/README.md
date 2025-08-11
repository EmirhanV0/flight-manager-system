# 🏢 Reference Manager Service

## 📋 Açıklama

Reference Manager Service, UYS sisteminin referans veri yönetimi mikroservisidir. Havayolu şirketleri, uçaklar ve istasyonlar gibi temel verileri yönetir.

## 🚀 Hızlı Başlangıç

### Gereksinimler
- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### Kurulum

1. **Bağımlılıkları yükleyin:**
```bash
mvn clean install
```

2. **Uygulamayı başlatın:**
```bash
mvn spring-boot:run
```

3. **Docker ile çalıştırın:**
```bash
# Using Docker Compose
docker-compose -f docker-compose.override.yml up --build -d

# Using orchestration script
./scripts/docker-compose-run.sh

# Using direct Docker build
./scripts/docker-build.sh
```

## 🐳 Docker

### Multi-Stage Build
```bash
# Build optimized production image
docker build -t uys/reference-manager:latest .

# Build for development (includes source code)
docker build --target build -t uys/reference-manager:dev .
```

### Run Container
```bash
# Using Docker directly
docker run -d \
  --name uys-reference-manager \
  --network uys-network \
  -p 8081:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -v $(pwd)/logs:/app/logs \
  uys/reference-manager:latest

# Using build script
./scripts/docker-build.sh
```

### Docker Compose
```bash
# Start with infrastructure orchestration
./scripts/docker-compose-run.sh

# Or manually
docker-compose -f docker-compose.override.yml up --build -d

# Stop
docker-compose -f docker-compose.override.yml down
```

### Scripts
- `scripts/docker-build.sh` - Build and run container
- `scripts/docker-compose-run.sh` - Orchestrate with infrastructure

### Health Check
- Endpoint: `http://localhost:8081/api/actuator/health`
- Docker health check configured
- Automatic restart on failure

## 📊 API Endpoint'leri

### Swagger UI
- **URL:** http://localhost:8081/api/swagger-ui.html

### Health Check
- **URL:** http://localhost:8081/api/actuator/health

### Prometheus Metrics
- **URL:** http://localhost:8081/api/actuator/prometheus

### Actuator Endpoints
- **Health:** http://localhost:8081/api/actuator/health
- **Info:** http://localhost:8081/api/actuator/info
- **Metrics:** http://localhost:8081/api/actuator/metrics
- **Prometheus:** http://localhost:8081/api/actuator/prometheus

## 🗄️ Veritabanı

### MySQL
- **Port:** 3306
- **Database:** uys_reference
- **Migration:** Liquibase

### Database Schema
- **Airline Table:** Havayolu şirketleri (TK, PEG, LH, BA)
- **Aircraft Table:** Uçaklar (TC-JRE, TC-JRF, TC-JRG, TC-CCD, D-AIKE)
- **Station Table:** İstasyonlar (IST, SAW, FRA, LHR)

### Migration Files
- `001-create-airline-table.xml` - Havayolu şirketleri tablosu
- `002-create-aircraft-table.xml` - Uçaklar tablosu
- `003-create-station-table.xml` - İstasyonlar tablosu
- `004-create-indexes.xml` - Performans indexleri
- `005-insert-initial-data.xml` - Test verileri

### Redis Cache
- **Port:** 6379
- **TTL:** 3600s (1 saat)

## 📡 Kafka Topics

### Producer Topics
- `airline-events` - Havayolu şirketi değişiklik eventleri
- `aircraft-events` - Uçak değişiklik eventleri  
- `station-events` - İstasyon değişiklik eventleri

### Event Types
- `CREATED` - Yeni kayıt oluşturuldu
- `UPDATED` - Kayıt güncellendi
- `DELETED` - Kayıt silindi (soft delete)
- `STATUS_CHANGED` - Durum değişikliği (aktif/pasif)

### Event Structure
```json
{
  "eventId": "uuid",
  "eventType": "CREATED",
  "eventTimestamp": "2024-01-01T12:00:00",
  "sourceService": "reference-manager",
  "version": "1.0",
  // Entity specific fields...
}
```

## 🧪 Test

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Testcontainers
- MySQL
- Redis
- Kafka

## 📈 Monitoring

### Metrics
- Cache hit/miss oranları
- Database query latency
- Kafka producer throughput
- API response times

### Alerts
- Cache miss rate > 20%
- Database latency > 100ms
- Kafka producer errors

## 🔧 Konfigürasyon

### application-dev.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/uys_reference
  redis:
    host: localhost
    port: 6379
  kafka:
    bootstrap-servers: localhost:9092
```

## 📝 API Örnekleri

### Havayolu Ekleme
```bash
curl -X POST http://localhost:8081/api/airlines \
  -H "Content-Type: application/json" \
  -d '{
    "code": "TK",
    "name": "Turkish Airlines",
    "country": "Turkey"
  }'
```

### Uçak Ekleme
```bash
curl -X POST http://localhost:8081/api/aircraft \
  -H "Content-Type: application/json" \
  -d '{
    "registration": "TC-JRE",
    "type": "Boeing 737-800",
    "capacity": 189
  }'
```

### İstasyon Ekleme
```bash
curl -X POST http://localhost:8081/api/stations \
  -H "Content-Type: application/json" \
  -d '{
    "code": "IST",
    "name": "Istanbul Airport",
    "city": "Istanbul",
    "country": "Turkey"
  }'
```

## 🚨 Troubleshooting

### Yaygın Sorunlar

1. **Redis bağlantı hatası:**
   - Redis servisinin çalıştığını kontrol edin
   - Port 6379'un açık olduğunu kontrol edin

2. **Kafka bağlantı hatası:**
   - Kafka servisinin çalıştığını kontrol edin
   - Topic'lerin oluşturulduğunu kontrol edin

3. **Database bağlantı hatası:**
   - MySQL servisinin çalıştığını kontrol edin
   - Database'in oluşturulduğunu kontrol edin

## 📚 Daha Fazla Bilgi

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data Redis](https://spring.io/projects/spring-data-redis)
- [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream)
- [Liquibase](https://www.liquibase.org/) 