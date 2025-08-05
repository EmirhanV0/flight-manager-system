# 🚀 UYS Project - Unified Yield System

## 📋 Proje Genel Bakış

UYS (Unified Yield System), havacılık sektörü için geliştirilmiş mikroservis tabanlı bir uçuş yönetim sistemidir. Sistem, referans veri yönetimi, uçuş operasyonları ve arşivleme işlemlerini kapsar.

## 🏗️ Mimari Yapı

```
uys/
├── reference-manager/    # Referans veri yönetimi (Airline, Aircraft, Station)
├── flight-service/       # Uçuş operasyonları
├── archive-service/      # Event arşivleme ve sorgulama
├── frontend/            # Vue 3 + Element Plus UI
└── infra/              # Docker, Kafka, Redis, Database
```

## 🚀 Hızlı Başlangıç

### Gereksinimler
- **Docker & Docker Compose** (v20.10+)
- **Java 17+** (OpenJDK veya Oracle JDK)
- **Node.js 18+** (npm veya yarn)
- **Maven 3.8+** (Spring Boot projeleri için)
- **Git** (versiyon kontrolü için) - [Kurulum Talimatları](setup-git.md)

### Sistem Gereksinimleri
- **RAM:** En az 8GB (16GB önerilen)
- **Disk:** En az 20GB boş alan
- **CPU:** 4+ çekirdek önerilen

### Kurulum

1. **Repository'yi klonlayın veya Git setup yapın:**
```bash
# Eğer GitHub'dan klonluyorsanız:
git clone <repository-url>
cd uys

# Veya mevcut projeyi Git ile yönetmek için:
# Git kurulumu için setup-git.md dosyasını inceleyin
# Otomatik setup için: .\setup-git.ps1
```

2. **Ortam değişkenlerini ayarlayın:**
```bash
# .env.cursor.example dosyasını .env.cursor olarak kopyalayın
cp .env.cursor.example .env.cursor
# .env.cursor dosyasını düzenleyin (gerekirse)
```

3. **Infrastructure'ı başlatın:**
```bash
cd infra
docker-compose up -d

# Servislerin durumunu kontrol edin
docker-compose ps

# Logları izleyin (opsiyonel)
docker-compose logs -f
```

4. **Mikroservisleri başlatın:**
```bash
# Reference Manager
cd reference-manager
mvn spring-boot:run

# Flight Service
cd ../flight-service
mvn spring-boot:run

# Archive Service
cd ../archive-service
mvn spring-boot:run
```

5. **Frontend'i başlatın:**
```bash
cd ../frontend
npm install
npm run dev
```

## 📊 Servis Endpoint'leri

### 🏢 Reference Manager Service (Port: 8081)
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **Health Check:** http://localhost:8081/actuator/health
- **Prometheus:** http://localhost:8081/actuator/prometheus
- **API Base:** http://localhost:8081/api

### ✈️ Flight Service (Port: 8082)
- **Swagger UI:** http://localhost:8082/swagger-ui.html
- **Health Check:** http://localhost:8082/actuator/health
- **Prometheus:** http://localhost:8082/actuator/prometheus
- **API Base:** http://localhost:8082/api

### 📦 Archive Service (Port: 8083)
- **Swagger UI:** http://localhost:8083/swagger-ui.html
- **Health Check:** http://localhost:8083/actuator/health
- **Prometheus:** http://localhost:8083/actuator/prometheus
- **API Base:** http://localhost:8083/api

### 🎨 Frontend (Port: 3000)
- **Application:** http://localhost:3000

### 🏗️ Infrastructure Services

#### 📡 Kafka Cluster
- **Kafka UI:** http://localhost:8080
- **Broker 1:** localhost:9092
- **Broker 2:** localhost:9093
- **Broker 3:** localhost:9094
- **Zookeeper:** localhost:2181
- **Schema Registry:** localhost:8081

#### 🗄️ Databases
- **MySQL:** localhost:3306
- **PostgreSQL:** localhost:5432
- **Redis:** localhost:6379

#### 📈 Monitoring
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3001 (admin/admin)

## 🧪 Test

### Unit Tests
```bash
# Her servis için
mvn test

# Belirli bir servis için
cd reference-manager && mvn test
cd flight-service && mvn test
cd archive-service && mvn test
```

### Integration Tests
```bash
# Testcontainers ile
mvn verify

# Infrastructure'ın çalıştığından emin olun
cd infra && docker-compose ps
```

### E2E Tests
```bash
# Frontend klasöründe
cd frontend
npm run test:e2e
```

### Health Checks
```bash
# Tüm servislerin sağlık durumunu kontrol edin
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

## 📈 Monitoring

### Prometheus
- **URL:** http://localhost:9090
- **Targets:** Tüm Spring Boot uygulamaları ve Kafka broker'ları
- **Metrics:** API response times, cache hit/miss, Kafka throughput

### Grafana
- **URL:** http://localhost:3001
- **Default Credentials:** admin/admin
- **Dashboards:** UYS Overview, Kafka Metrics, Redis Metrics, Database Metrics

### Kafka UI
- **URL:** http://localhost:8080
- **Features:** Topic management, consumer groups, message inspection

### Health Checks
```bash
# Infrastructure health
docker-compose ps

# Application health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health

# Database health
docker exec uys-mysql mysqladmin ping -h localhost -u root -puys-mysql-root
docker exec uys-postgresql pg_isready -U uys_user -d uys_archive
```

## 🔧 Geliştirme

### Cursor Adım Kuralları
Bu proje UYS Rule Set v2.0 kurallarına göre geliştirilmektedir. Detaylar için `UYS_RULES.md` dosyasını inceleyin.

### Temel Komutlar

#### Infrastructure Yönetimi
```bash
# Infrastructure'ı başlat
cd infra && docker-compose up -d

# Infrastructure'ı durdur
cd infra && docker-compose down

# Logları izle
cd infra && docker-compose logs -f

# Servis durumunu kontrol et
cd infra && docker-compose ps
```

#### Mikroservis Geliştirme
```bash
# Reference Manager
cd reference-manager && mvn spring-boot:run

# Flight Service
cd flight-service && mvn spring-boot:run

# Archive Service
cd archive-service && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

#### Test ve Build
```bash
# Tüm testleri çalıştır
mvn clean test

# Integration testleri
mvn verify

# Docker image build
docker build -t uys-reference-manager reference-manager/
```

### Commit Mesajları
```
feat: yeni özellik
fix: hata düzeltmesi
docs: dokümantasyon
test: test ekleme/düzenleme
refactor: kod refactoring
style: kod formatı
perf: performans iyileştirmesi
ci: CI/CD değişiklikleri
```

### Branch Stratejisi
- `main`: Production
- `dev`: Development
- `feature/*`: Yeni özellikler
- `hotfix/*`: Acil düzeltmeler
- `release/*`: Release hazırlığı

## 🛠️ Teknolojiler

### Backend
- **Spring Boot 3.x** - Ana framework
- **Spring Data JPA** - Veritabanı erişimi
- **Spring Security** - Güvenlik ve authentication
- **Spring Cloud Stream (Kafka)** - Event-driven messaging
- **Spring Data Redis** - Cache yönetimi
- **Liquibase** - Database migration
- **MapStruct** - Object mapping
- **Testcontainers** - Integration testing

### Frontend
- **Vue 3** - Progressive JavaScript framework
- **Element Plus** - UI component library
- **Axios** - HTTP client
- **Cypress** - E2E testing
- **Vite** - Build tool ve dev server

### Infrastructure
- **Docker & Docker Compose** - Containerization
- **Apache Kafka** - Event streaming platform
- **Redis** - In-memory cache
- **MySQL** - Reference ve Flight verileri
- **PostgreSQL** - Archive verileri
- **Prometheus** - Metrics collection
- **Grafana** - Monitoring dashboard
- **Kafka UI** - Kafka management interface

### Development Tools
- **Maven** - Java build tool
- **Node.js & npm** - JavaScript runtime
- **Git** - Version control
- **Cursor IDE** - Development environment

## 📝 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 🤝 Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit edin (`git commit -m 'feat: add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📞 İletişim

Proje hakkında sorularınız için issue açabilirsiniz. 