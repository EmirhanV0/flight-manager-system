# UYS - Havacılık Yönetim Sistemi

## 📋 Proje Hakkında

UYS (Uçak Yönetim Sistemi), havacılık sektörü için geliştirilmiş mikroservis mimarisinde bir yönetim sistemidir. Sistem, havayolları, uçaklar, istasyonlar ve uçuşların yönetimini sağlar.

## 🏗️ Mimari Yapı

### Mikroservisler

- **reference-manager**: Referans veri yönetimi (havayolları, uçaklar, istasyonlar)
- **flight-service**: Uçuş yönetimi ve planlama
- **archive-service**: Arşiv ve log yönetimi
- **frontend**: Kullanıcı arayüzü

### Teknolojiler

- **Backend**: Spring Boot 3.x, Java 17
- **Database**: MySQL, PostgreSQL
- **Cache**: Redis
- **Message Queue**: Apache Kafka
- **Monitoring**: Prometheus, Grafana
- **Container**: Docker, Docker Compose
- **Build Tool**: Maven

## 🚀 Hızlı Başlangıç

### Gereksinimler

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### Kurulum

1. **Projeyi klonlayın:**
   ```bash
   git clone <repository-url>
   cd uys
   ```

2. **Bağımlılıkları yükleyin:**
   ```bash
   mvn clean install
   ```

3. **Servisleri başlatın:**
   ```bash
   # Basit kurulum (sadece temel servisler)
   docker-compose -f docker-compose-simple.yml up -d
   
   # Tam kurulum (tüm servisler + monitoring)
   docker-compose -f docker-compose-full.yml up -d
   ```

## 📁 Proje Yapısı

```
uys/
├── reference-manager/          # Referans veri yönetimi
│   ├── src/main/java/
│   │   └── com/uys/reference/
│   │       ├── controller/     # REST API Controllers
│   │       ├── service/        # Business Logic
│   │       ├── repository/     # Data Access Layer
│   │       ├── entity/         # JPA Entities
│   │       ├── dto/           # Data Transfer Objects
│   │       ├── mapper/        # Object Mappers
│   │       ├── config/        # Configuration Classes
│   │       └── event/         # Event Publishing
│   └── src/test/              # Test Classes
├── flight-service/             # Uçuş yönetimi
├── archive-service/            # Arşiv servisi
├── frontend/                  # Web arayüzü
├── infra/                     # Altyapı konfigürasyonları
│   ├── docker-compose.yml
│   ├── mysql/
│   ├── postgresql/
│   ├── prometheus/
│   └── grafana/
└── docker-compose-*.yml       # Docker konfigürasyonları
```

## 🔧 Konfigürasyon

### Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=uys_reference
DB_USER=uys_user
DB_PASSWORD=uys_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka
KAFKA_BROKERS=localhost:9092
```

### Application Profiles

- **dev**: Geliştirme ortamı
- **test**: Test ortamı
- **prod**: Üretim ortamı

## 🧪 Test

```bash
# Tüm testleri çalıştır
mvn test

# Belirli bir servisin testlerini çalıştır
mvn test -pl reference-manager

# Test coverage raporu
mvn jacoco:report
```

## 📊 Monitoring

### Prometheus Metrics

- HTTP request metrics
- Database connection metrics
- Custom business metrics

### Grafana Dashboards

- Service health monitoring
- Performance metrics
- Business metrics

## 🐳 Docker

### Servisleri Başlatma

```bash
# Development
docker-compose -f docker-compose-simple.yml up -d

# Production
docker-compose -f docker-compose-full.yml up -d
```

### Servisleri Durdurma

```bash
docker-compose down
```

## 📚 API Dokümantasyonu

### Swagger UI

- Reference Manager: http://localhost:8080/api/v1/swagger-ui.html
- Flight Service: http://localhost:8081/api/v1/swagger-ui.html
- Archive Service: http://localhost:8082/api/v1/swagger-ui.html

### API Endpoints

#### Reference Manager

```
GET    /api/v1/airlines          # Havayolları listesi
POST   /api/v1/airlines          # Yeni havayolu ekleme
GET    /api/v1/airlines/{code}   # Havayolu detayı
PUT    /api/v1/airlines/{code}   # Havayolu güncelleme
DELETE /api/v1/airlines/{code}   # Havayolu silme

GET    /api/v1/aircraft          # Uçak listesi
POST   /api/v1/aircraft          # Yeni uçak ekleme
GET    /api/v1/aircraft/{code}   # Uçak detayı
PUT    /api/v1/aircraft/{code}   # Uçak güncelleme
DELETE /api/v1/aircraft/{code}   # Uçak silme

GET    /api/v1/stations          # İstasyon listesi
POST   /api/v1/stations          # Yeni istasyon ekleme
GET    /api/v1/stations/{code}   # İstasyon detayı
PUT    /api/v1/stations/{code}   # İstasyon güncelleme
DELETE /api/v1/stations/{code}   # İstasyon silme
```

## 🔒 Güvenlik

- JWT tabanlı authentication
- Role-based access control (RBAC)
- API rate limiting
- Input validation ve sanitization

## 📈 Performance

- Connection pooling (HikariCP)
- Caching (Redis)
- Database indexing
- Query optimization

## 🚨 Troubleshooting

### Yaygın Sorunlar

1. **Database bağlantı hatası**
   ```bash
   # MySQL servisini kontrol et
   docker-compose ps mysql
   
   # Logları kontrol et
   docker-compose logs mysql
   ```

2. **Port çakışması**
   ```bash
   # Kullanılan portları kontrol et
   netstat -an | findstr :8080
   ```

3. **Memory sorunları**
   ```bash
   # Docker memory limitini artır
   docker-compose down
   docker system prune
   ```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 👥 Geliştirici Ekibi

- **Emirhan Yeşildağ** - [EmirhanV0](https://github.com/EmirhanV0)
- **UYS Development Team**

## 📞 İletişim

- **Email**: emirhanyesildag32@gmail.com
- **GitHub**: [EmirhanV0](https://github.com/EmirhanV0)

---

**Not**: Bu proje geliştirme aşamasındadır. Production kullanımı için ek güvenlik ve performans optimizasyonları gerekebilir. 