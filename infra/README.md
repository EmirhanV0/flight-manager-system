# 🏗️ Infrastructure

## 📋 Açıklama

UYS projesinin infrastructure bileşenlerini içeren klasör. Docker Compose ile tüm servisleri (Kafka, Redis, MySQL, PostgreSQL, Prometheus, Grafana) yönetir.

## 🚀 Hızlı Başlangıç

### Gereksinimler
- Docker 20.10+
- Docker Compose 2.0+
- En az 8GB RAM
- En az 20GB disk alanı

### Kurulum

1. **Infrastructure'ı başlatın:**
```bash
cd infra
docker-compose up -d
```

2. **Servisleri kontrol edin:**
```bash
docker-compose ps
```

3. **Logları izleyin:**
```bash
docker-compose logs -f
```

## 📊 Servis Endpoint'leri

### Kafka
- **Broker 1:** localhost:9092
- **Broker 2:** localhost:9093
- **Broker 3:** localhost:9094
- **Zookeeper:** localhost:2181
- **Schema Registry:** localhost:8081

### Redis
- **Port:** localhost:6379
- **Sentinel:** localhost:26379

### Databases
- **MySQL:** localhost:3306
- **PostgreSQL:** localhost:5432

### Monitoring
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3001

## 🐳 Docker Compose Yapısı

### Services
```yaml
services:
  # Kafka Cluster
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    ports:
      - "2181:2181"
  
  kafka1:
    image: confluentinc/cp-kafka:7.4.0
    ports:
      - "9092:9092"
  
  kafka2:
    image: confluentinc/cp-kafka:7.4.0
    ports:
      - "9093:9093"
  
  kafka3:
    image: confluentinc/cp-kafka:7.4.0
    ports:
      - "9094:9094"
  
  # Redis
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
  
  # Databases
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
  
  postgresql:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
  
  # Monitoring
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
  
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3001:3000"
```

## 🔧 Konfigürasyon

### Environment Variables
```env
# Kafka
KAFKA_BROKER_1_PORT=9092
KAFKA_BROKER_2_PORT=9093
KAFKA_BROKER_3_PORT=9094
KAFKA_ZOOKEEPER_PORT=2181

# Redis
REDIS_PORT=6379
REDIS_PASSWORD=uys-redis-password

# MySQL
MYSQL_ROOT_PASSWORD=uys-mysql-root
MYSQL_DATABASE=uys_reference
MYSQL_USER=uys_user
MYSQL_PASSWORD=uys_password

# PostgreSQL
POSTGRES_DB=uys_archive
POSTGRES_USER=uys_user
POSTGRES_PASSWORD=uys_password

# Prometheus
PROMETHEUS_PORT=9090

# Grafana
GRAFANA_PORT=3001
GRAFANA_ADMIN_PASSWORD=admin
```

### Volumes
```yaml
volumes:
  kafka_data:
  redis_data:
  mysql_data:
  postgresql_data:
  prometheus_data:
  grafana_data:
```

### Networks
```yaml
networks:
  uys-network:
    driver: bridge
```

## 📈 Monitoring

### Prometheus Targets
- Reference Manager: localhost:8081/actuator/prometheus
- Flight Service: localhost:8082/actuator/prometheus
- Archive Service: localhost:8083/actuator/prometheus

### Grafana Dashboards
- **UYS Overview:** Sistem genel durumu
- **Kafka Metrics:** Topic throughput, lag
- **Redis Metrics:** Hit/miss oranları
- **Database Metrics:** Query latency
- **Application Metrics:** API response times

## 🧪 Health Checks

### Kafka Health Check
```bash
# Topic listesi
docker exec kafka1 kafka-topics --list --bootstrap-server localhost:9092

# Consumer group durumu
docker exec kafka1 kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group uys-flight-consumer
```

### Redis Health Check
```bash
# Redis ping
docker exec redis redis-cli ping

# Redis info
docker exec redis redis-cli info
```

### Database Health Check
```bash
# MySQL
docker exec mysql mysql -u root -p -e "SHOW DATABASES;"

# PostgreSQL
docker exec postgresql psql -U uys_user -d uys_archive -c "SELECT version();"
```

## 🚨 Troubleshooting

### Yaygın Sorunlar

1. **Port çakışması:**
   ```bash
   # Port kullanımını kontrol edin
   netstat -an | findstr :9092
   ```

2. **Disk alanı yetersiz:**
   ```bash
   # Docker volume'larını temizleyin
   docker system prune -a
   ```

3. **Memory yetersiz:**
   ```bash
   # Docker memory limit'ini artırın
   # Docker Desktop > Settings > Resources > Memory
   ```

### Log Analizi
```bash
# Tüm servislerin logları
docker-compose logs

# Belirli servisin logları
docker-compose logs kafka1

# Real-time log takibi
docker-compose logs -f redis
```

## 📚 Daha Fazla Bilgi

- [Docker Compose](https://docs.docker.com/compose/)
- [Apache Kafka](https://kafka.apache.org/)
- [Redis](https://redis.io/)
- [MySQL](https://www.mysql.com/)
- [PostgreSQL](https://www.postgresql.org/)
- [Prometheus](https://prometheus.io/)
- [Grafana](https://grafana.com/) 