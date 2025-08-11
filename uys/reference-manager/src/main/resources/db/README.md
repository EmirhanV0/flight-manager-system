# 🗄️ Database Migration - Liquibase

## 📋 Açıklama

Bu klasör, UYS Reference Manager Service'in veritabanı migration'larını içerir. Liquibase kullanılarak veritabanı şeması ve veri değişiklikleri yönetilir.

## 📁 Dosya Yapısı

```
db/
├── changelog/
│   ├── db.changelog-master.xml          # Ana changelog dosyası
│   └── changes/                         # Migration dosyaları
│       ├── 001-create-airline-table.xml
│       ├── 002-create-aircraft-table.xml
│       ├── 003-create-station-table.xml
│       ├── 004-create-indexes.xml
│       └── 005-insert-initial-data.xml
└── README.md                           # Bu dosya
```

## 🔄 Migration Sırası

1. **001-create-airline-table.xml** - Havayolu şirketleri tablosu
2. **002-create-aircraft-table.xml** - Uçaklar tablosu (airline foreign key ile)
3. **003-create-station-table.xml** - İstasyonlar tablosu
4. **004-create-indexes.xml** - Performans için indexler
5. **005-insert-initial-data.xml** - Test ve demo verileri

## 🗃️ Tablo Yapıları

### Airline Tablosu
```sql
CREATE TABLE airline (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    airline_code VARCHAR(10) NOT NULL UNIQUE,
    airline_name VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    version INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Aircraft Tablosu
```sql
CREATE TABLE aircraft (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration VARCHAR(20) NOT NULL UNIQUE,
    aircraft_type VARCHAR(20) NOT NULL,
    model VARCHAR(50) NOT NULL,
    manufacturer VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    max_range INT NOT NULL,
    cruise_speed INT NOT NULL,
    airline_id BIGINT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    version INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (airline_id) REFERENCES airline(id)
);
```

### Station Tablosu
```sql
CREATE TABLE station (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_code VARCHAR(10) NOT NULL UNIQUE,
    station_name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    address TEXT,
    timezone VARCHAR(50) NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    altitude INT,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    version INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 📊 Indexler

### Airline Indexleri
- `idx_airline_code` - Airline code araması için
- `idx_airline_active` - Aktif airline filtreleme için
- `idx_airline_country` - Ülke bazlı arama için

### Aircraft Indexleri
- `idx_aircraft_registration` - Registration araması için
- `idx_aircraft_active` - Aktif aircraft filtreleme için
- `idx_aircraft_airline_id` - Airline bazlı arama için
- `idx_aircraft_type` - Aircraft type araması için
- `idx_aircraft_manufacturer` - Üretici bazlı arama için

### Station Indexleri
- `idx_station_code` - Station code araması için
- `idx_station_active` - Aktif station filtreleme için
- `idx_station_city` - Şehir bazlı arama için
- `idx_station_country` - Ülke bazlı arama için
- `idx_station_coordinates` - Konum bazlı arama için

## 🧪 Test Verileri

### Airlines
- **TK** - Turkish Airlines (Turkey, Istanbul)
- **PEG** - Pegasus Airlines (Turkey, Istanbul)
- **LH** - Lufthansa (Germany, Frankfurt)
- **BA** - British Airways (United Kingdom, London)

### Stations
- **IST** - Istanbul Airport (Istanbul, Turkey)
- **SAW** - Sabiha Gökçen International Airport (Istanbul, Turkey)
- **FRA** - Frankfurt Airport (Frankfurt, Germany)
- **LHR** - Heathrow Airport (London, United Kingdom)

### Aircraft
- **TC-JRE** - A320-200 (Turkish Airlines)
- **TC-JRF** - A321-200 (Turkish Airlines)
- **TC-JRG** - 777-300ER (Turkish Airlines)
- **TC-CCD** - A320neo (Pegasus Airlines)
- **D-AIKE** - A350-900 (Lufthansa)

## 🚀 Kullanım

### Development Ortamında
```bash
# Uygulamayı başlat (Liquibase otomatik çalışır)
mvn spring-boot:run
```

### Test Ortamında
```bash
# Integration testleri çalıştır
mvn test -Dtest=LiquibaseMigrationTest
```

### Production Ortamında
```bash
# Liquibase status kontrolü
mvn liquibase:status

# Liquibase update
mvn liquibase:update

# Liquibase rollback (gerekirse)
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## 🔧 Konfigürasyon

### application.yml
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.xml
    contexts: ${spring.profiles.active}
```

### Test Konfigürasyonu
```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.xml
    contexts: test
```

## 📝 Best Practices

1. **ChangeSet ID'leri** benzersiz olmalı
2. **Author** alanı takım üyesi adı olmalı
3. **Comment** alanı değişikliği açıklamalı
4. **Rollback** her changeSet için tanımlanmalı
5. **Foreign key'ler** doğru sırada oluşturulmalı
6. **Indexler** performans için gerekli alanlarda oluşturulmalı

## 🚨 Dikkat Edilecekler

1. **Migration sırası** önemli - foreign key'ler için
2. **Test verileri** sadece development/test ortamında
3. **Production'da** sadece şema migration'ları
4. **Backup** alınmadan migration yapılmamalı
5. **Rollback planı** her zaman hazır olmalı

## 🔍 Troubleshooting

### Yaygın Sorunlar

1. **Foreign key constraint hatası**
   - Tablo sırasını kontrol edin
   - Referans edilen tablo önce oluşturulmalı

2. **Duplicate key hatası**
   - Unique constraint'leri kontrol edin
   - Test verilerinde çakışma olabilir

3. **Liquibase lock hatası**
   - `DATABASECHANGELOGLOCK` tablosunu kontrol edin
   - Gerekirse lock'u manuel temizleyin

### Debug Komutları
```sql
-- Liquibase tablolarını kontrol et
SELECT * FROM DATABASECHANGELOG ORDER BY DATEEXECUTED DESC;

-- Lock durumunu kontrol et
SELECT * FROM DATABASECHANGELOGLOCK;

-- Tablo yapısını kontrol et
DESCRIBE airline;
DESCRIBE aircraft;
DESCRIBE station;
``` 