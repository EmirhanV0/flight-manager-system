# UYS Proje Yapısı

## 📁 Ana Dizin Yapısı

```
uys/
├── 📁 reference-manager/          # Referans veri yönetimi servisi
├── 📁 flight-service/             # Uçuş yönetimi servisi  
├── 📁 archive-service/            # Arşiv ve log yönetimi servisi
├── 📁 frontend/                  # Web kullanıcı arayüzü
├── 📁 infra/                     # Altyapı konfigürasyonları
├── 📄 README.md                  # Proje dokümantasyonu
├── 📄 pom.xml                    # Ana Maven projesi
├── 📄 docker-compose-simple.yml  # Basit Docker kurulumu
├── 📄 docker-compose-full.yml    # Tam Docker kurulumu
└── 📄 PROJECT_STRUCTURE.md       # Bu dosya
```

## 🏗️ Mikroservis Yapıları

### 📁 reference-manager/

Referans veri yönetimi (havayolları, uçaklar, istasyonlar)

```
reference-manager/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/uys/reference/
│   │   │   ├── 📁 controller/     # REST API Controllers
│   │   │   │   ├── AircraftController.java
│   │   │   │   ├── AirlineController.java
│   │   │   │   ├── StationController.java
│   │   │   │   └── TestController.java
│   │   │   ├── 📁 service/        # Business Logic Layer
│   │   │   │   ├── AircraftService.java
│   │   │   │   ├── AirlineService.java
│   │   │   │   └── StationService.java
│   │   │   ├── 📁 repository/     # Data Access Layer
│   │   │   │   ├── AircraftRepository.java
│   │   │   │   ├── AirlineRepository.java
│   │   │   │   └── StationRepository.java
│   │   │   ├── 📁 entity/         # JPA Entities
│   │   │   │   ├── Aircraft.java
│   │   │   │   ├── Airline.java
│   │   │   │   └── Station.java
│   │   │   ├── 📁 dto/           # Data Transfer Objects
│   │   │   │   ├── AircraftDto.java
│   │   │   │   ├── AirlineDto.java
│   │   │   │   ├── StationDto.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── 📁 mapper/        # Object Mappers (MapStruct)
│   │   │   │   ├── AircraftMapper.java
│   │   │   │   ├── AirlineMapper.java
│   │   │   │   └── StationMapper.java
│   │   │   ├── 📁 config/        # Configuration Classes
│   │   │   │   ├── KafkaConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── 📁 event/         # Event Publishing
│   │   │   │   ├── 📁 dto/
│   │   │   │   │   ├── AircraftEventDto.java
│   │   │   │   │   ├── AirlineEventDto.java
│   │   │   │   │   └── StationEventDto.java
│   │   │   │   ├── 📁 enums/
│   │   │   │   │   └── EventType.java
│   │   │   │   └── 📁 publisher/
│   │   │   │       └── EventPublisherService.java
│   │   │   └── ReferenceManagerApplication.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.yml
│   │       ├── 📄 application-dev.yml
│   │       ├── 📄 application-test.yml
│   │       ├── 📁 db/
│   │       │   └── 📁 changelog/
│   │       │       ├── 📁 changes/
│   │       │       │   ├── 001-create-airline-table.xml
│   │       │       │   ├── 002-create-aircraft-table.xml
│   │       │       │   ├── 003-create-station-table.xml
│   │       │       │   ├── 004-create-indexes.xml
│   │       │       │   └── 005-insert-initial-data.xml
│   │       │       └── 📄 db.changelog-master.xml
│   │       └── 📁 static/
│   │           └── 📄 swagger-info.md
│   └── 📁 test/
│       ├── 📁 java/com/uys/reference/
│       │   ├── 📁 config/
│       │   │   └── LiquibaseMigrationTest.java
│       │   ├── 📁 controller/
│       │   │   └── AirlineControllerIT.java
│       │   ├── 📁 event/
│       │   │   ├── KafkaIntegrationTest.java
│       │   │   └── 📁 publisher/
│       │   │       └── EventPublisherServiceTest.java
│       │   ├── 📁 performance/
│       │   │   └── AirlinePerformanceTest.java
│       │   ├── 📁 repository/
│       │   │   └── AirlineRepositoryIT.java
│       │   └── 📁 service/
│       │       └── AirlineServiceTest.java
│       ├── 📁 resources/
│       │   ├── 📄 application-test.yml
│       │   └── 📄 performance-test-results.md
│       └── 📄 README.md
├── 📄 Dockerfile
├── 📄 Dockerfile.simple
├── 📄 pom.xml
└── 📄 README.md
```

### 📁 flight-service/

Uçuş yönetimi ve planlama servisi

```
flight-service/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/uys/flight/
│   │   │   ├── 📁 controller/
│   │   │   │   └── FlightController.java
│   │   │   ├── 📁 service/
│   │   │   │   └── FlightService.java
│   │   │   ├── 📁 repository/
│   │   │   │   └── FlightRepository.java
│   │   │   ├── 📁 entity/
│   │   │   │   ├── Flight.java
│   │   │   │   ├── FlightLeg.java
│   │   │   │   └── FlightSchedule.java
│   │   │   ├── 📁 dto/
│   │   │   │   ├── FlightDto.java
│   │   │   │   └── FlightLegDto.java
│   │   │   ├── 📁 mapper/
│   │   │   │   └── FlightMapper.java
│   │   │   ├── 📁 enums/
│   │   │   │   ├── FlightStatus.java
│   │   │   │   └── FlightType.java
│   │   │   └── FlightServiceApplication.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.yml
│   │       └── 📁 db/
│   │           └── 📁 changelog/
│   │               ├── 📁 changes/
│   │               │   ├── 001-create-flight-table.xml
│   │               │   ├── 002-create-flight-leg-table.xml
│   │               │   ├── 003-create-flight-schedule-table.xml
│   │               │   ├── 004-create-indexes.xml
│   │               │   └── 005-insert-initial-data.xml
│   │               └── 📄 db.changelog-master.xml
│   └── 📁 test/
├── 📄 Dockerfile
├── 📄 pom.xml
└── 📄 README.md
```

### 📁 archive-service/

Arşiv ve log yönetimi servisi

```
archive-service/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/uys/archive/
│   │   │   ├── 📁 controller/
│   │   │   │   └── EventLogController.java
│   │   │   ├── 📁 service/
│   │   │   │   └── EventLogService.java
│   │   │   ├── 📁 repository/
│   │   │   │   └── EventLogRepository.java
│   │   │   ├── 📁 entity/
│   │   │   │   ├── AuditLog.java
│   │   │   │   └── EventLog.java
│   │   │   ├── 📁 dto/
│   │   │   │   └── EventLogDto.java
│   │   │   ├── 📁 mapper/
│   │   │   │   └── EventLogMapper.java
│   │   │   └── ArchiveServiceApplication.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.yml
│   │       └── 📁 db/
│   │           └── 📁 changelog/
│   │               ├── 📁 changes/
│   │               │   ├── 001-create-event-log-table.xml
│   │               │   ├── 002-create-audit-log-table.xml
│   │               │   └── 003-create-indexes.xml
│   │               └── 📄 db.changelog-master.xml
│   └── 📁 test/
├── 📄 Dockerfile
├── 📄 pom.xml
└── 📄 README.md
```

### 📁 frontend/

Web kullanıcı arayüzü

```
frontend/
├── 📁 src/
│   ├── 📁 components/
│   ├── 📁 views/
│   ├── 📁 router/
│   ├── 📁 store/
│   └── 📁 assets/
├── 📄 package.json
├── 📄 vite.config.js
└── 📄 README.md
```

### 📁 infra/

Altyapı konfigürasyonları

```
infra/
├── 📄 docker-compose.yml
├── 📁 grafana/
│   ├── 📁 dashboards/
│   └── 📁 provisioning/
│       └── 📁 datasources/
│           └── 📄 prometheus.yml
├── 📁 mysql/
│   └── 📁 init/
│       ├── 📄 01-create-databases.sql
│       └── 📄 01-init-databases.sql
├── 📁 postgresql/
│   └── 📁 init/
│       ├── 📄 01-create-databases.sql
│       └── 📄 01-init-database.sql
├── 📁 prometheus/
│   └── 📄 prometheus.yml
└── 📄 README.md
```

## 🔧 Konfigürasyon Dosyaları

### 📄 pom.xml (Ana Proje)
- Maven parent project
- Dependency management
- Plugin configuration
- Module definitions

### 📄 docker-compose-simple.yml
- Temel servisler (MySQL, Redis)
- Development ortamı için

### 📄 docker-compose-full.yml
- Tüm servisler (MySQL, PostgreSQL, Redis, Kafka, Prometheus, Grafana)
- Production ortamı için

## 📊 Monitoring ve Logging

### 📁 logs/
- Application logları
- Performance test sonuçları
- Error logları

### 📁 k8s/ (Kubernetes)
- Deployment configurations
- Service definitions
- Ingress rules

## 🧪 Test Yapısı

### Unit Tests
- Service layer testleri
- Repository layer testleri
- Controller layer testleri

### Integration Tests
- Database integration testleri
- Kafka integration testleri
- End-to-end API testleri

### Performance Tests
- Load testing
- Stress testing
- Benchmark testleri

## 📝 Dokümantasyon

### 📄 README.md
- Proje genel bakışı
- Kurulum talimatları
- API dokümantasyonu

### 📄 UYS_RULES.md
- Geliştirme kuralları
- Coding standards
- Best practices

### 📄 setup-git.md
- Git kurulum talimatları
- Repository yönetimi

## 🔒 Güvenlik

### Authentication
- JWT token based
- Role-based access control
- API rate limiting

### Data Protection
- Input validation
- SQL injection prevention
- XSS protection

## 📈 Performance

### Caching
- Redis cache implementation
- Database query optimization
- Connection pooling

### Monitoring
- Prometheus metrics
- Grafana dashboards
- Health checks

## 🚀 Deployment

### Docker
- Multi-stage builds
- Environment-specific configurations
- Health checks

### Kubernetes
- Horizontal pod autoscaling
- Resource limits
- Rolling updates

---

**Not**: Bu yapı sürekli geliştirilmektedir. Yeni özellikler eklendikçe güncellenir. 