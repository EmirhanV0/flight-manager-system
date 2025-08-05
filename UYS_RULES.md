# ✅ UYS Rule Set – Cursor Tabanlı Geliştirme (v2.0)

## 🟩 1. Cursor Geliştirme Kuralları (UYS özel)

| Kural No | Açıklama |
|----------|----------|
| C1 | Her Cursor adımı tek bir bounded context ve tek bir teknik hedef içermelidir (ör: FlightService KafkaProducer oluşturma). |
| C2 | Cursor adımı başında: "Amaç", "Bağlantılı adım", "Gereken altyapı" net şekilde yazılmalı. |
| C3 | Cursor adımı sonunda mutlaka: ✅ Checklist, 📋 Kalan Notlar, 🧪 Test Senaryosu, 🎯 Sonuç alanları yazılmalı. |
| C4 | Redis, Kafka gibi dış servislerle entegrasyon yapılacaksa önce mock ya da local docker ile test edilmeli. |
| C5 | Eğer bir adım 7/10 karmaşıklık üzerindeyse alt cursor adımlarına bölünmelidir. |
| C6 | Geçmiş cursor adımına dönüp düzeltme yapılacaksa yeni bir "rollback cursor" adımı tanımlanmalı. |

## 🔁 2. Mikroservis Geliştirme Kuralları

| Kural No | Açıklama |
|----------|----------|
| M1 | Her mikroservisin içinde kendi Dockerfile, docker-compose.override.yml, Liquibase ve README.md dosyası bulunmalı. |
| M2 | Her mikroservis Kafka veya Redis kullanıyorsa önce Testcontainers üzerinden integration test yazılmalı. |
| M3 | Spring Boot için application-dev.yml içerisinde Redis ve Kafka endpoint'leri ayrı tutulmalı. |
| M4 | Swagger/OpenAPI kullanılmalı. localhost:PORT/swagger-ui.html açık olmalı. |
| M5 | Her yeni endpoint için unit, integration, contract test üçlüsü Cursor checklist'e yazılmalı. |

## 📦 3. Docker ve Infrastructure Kuralları

| Kural No | Açıklama |
|----------|----------|
| I1 | Cursor sonunda docker compose -f docker-compose.yml -f docker-compose.override.yml up ile test yapılmalı. |
| I2 | .env.cursor dosyası içinde her mikroservise özel portlar tanımlı olmalı. |
| I3 | Kafka için healthcheck servisi eklenmeli (ör: kafkacat ile topic kontrolü). |
| I4 | Redis flush işlemleri için test modunda cache reset fonksiyonu açık olmalı. |
| I5 | CI süreci: build > test > liquibase validate > docker image build > deploy sırayla yazılmalı. |

## 📊 4. Monitoring, Test ve Logging Kuralları

| Kural No | Açıklama |
|----------|----------|
| T1 | p95 latency ölçümü yapılmalı: API ≤ 300ms, Redis ≤ 2ms, Kafka Publish ≤ 100ms |
| T2 | Prometheus metrics: /actuator/prometheus endpoint her serviste açık olmalı. |
| T3 | Testcontainers kullanılmadan Redis/Kafka entegrasyon completed sayılmaz. |
| T4 | Kafka message'ları JSON schema ile valid edilmeli. |
| T5 | Testler şunları kapsamalı: Kafka Retry, Redis TTL Testi, REST Error Case, Swagger Validator |

## 🧩 5. Frontend Kuralları (Vue 3 - Element Plus)

| Kural No | Açıklama |
|----------|----------|
| F1 | Vue 3 için form bileşenleri asyncData + Redis cache ile doldurulmalı. |
| F2 | API hataları toast mesajı + hata bileşeni şeklinde gösterilmeli. |
| F3 | Her form dirty check, ctrl+s desteği, aria-label uyumu ile yazılmalı. |
| F4 | Redis veri güncellenirse, Vue tarafında invalidate cache tetiklenmeli. |

## 🧪 6. Cursor Bazlı Test Kuralları

| Kural No | Açıklama |
|----------|----------|
| CT1 | Her cursor adımında en az 1 unit + 1 integration test yazılmalı. |
| CT2 | Kafka consumer cursor'unda testcontainers + embedded Kafka zorunlu. |
| CT3 | Redis için @DataRedisTest veya Testcontainers Redis kullanılmalı. |
| CT4 | Frontend tarafında Cypress ile E2E testi yapılmadan cursor tamamlanmaz. |
| CT5 | Retry mekanizmaları ayrı test class'larında test edilmeli (Backoff, Dead Letter, vs). |

## 🔐 7. Güvenlik ve Yetkilendirme

| Kural No | Açıklama |
|----------|----------|
| S1 | JWT doğrulama testleri hem frontend hem backend için yapılmalı. |
| S2 | Her endpoint role bazlı erişimle korunmalı (@PreAuthorize). |
| S3 | OWASP Top 10 için basic önlemler checklist'e eklenmeli. |

## 🧱 8. Kodlama Kuralları (Soft Priority - Zorunlu Değil)

| Kural No | Açıklama |
|----------|----------|
| K1 | Her sınıf ve method, tek bir işi yapmalı (Single Responsibility). |
| K2 | @Slf4j, @Service, @Transactional gibi anotasyonlar sade ve sadece gerektiği yerde kullanılmalı. |
| K3 | DTO, Entity, Response, Request sınıfları katmanlara göre ayrılmalı. |
| K4 | MapStruct kullanılıyorsa, mapping'ler test edilmeli. |
| K5 | Magic number, hardcoded string kullanılmamalı. Constants dosyasında toplanmalı. |
| K6 | Exception fırlatırken özelleştirilmiş mesajlar kullanılmalı. |
| K7 | Her class'ta maksimum 300 satır ve fonksiyon başı maksimum 40 satır sınırı gözetilmeli. |
| K8 | Controller → Service → Repository zinciri dışında çapraz bağımlılık yapılmamalı. |

## 🧠 9. Prompt Yazım Kuralları (Cursor için)

| Kural No | Açıklama |
|----------|----------|
| P1 | Prompt, işlem amacını açıkça belirtmeli. "Kafka consumer error retry mekanizması nasıl yazılır?" gibi. |
| P2 | Promptlarda varsa teknoloji belirtilmeli (ör: "Testcontainers ile Redis mocklamak için örnek test classı"). |
| P3 | Proje yapısına göre (UYS) flight-service, notification-service, monitoring gibi modüller adla belirtilmeli. |
| P4 | Açıklamalar hem İngilizce hem Türkçe karışık olabilir ama teknik kavramlar İngilizce kalmalı (örn: offset, healthcheck). |
| P5 | Prompt, önceki cursor adımına referans verirse daha iyi sonuçlar verir. (örn: "C2 adımındaki Kafka producer'ın tüketicisini yaz"). |

---

## 📋 Cursor Adım Template

### Amaç
[Bu cursor adımının amacını açıkça belirtin]

### Bağlantılı Adım
[Önceki/ sonraki cursor adımlarına referans]

### Gereken Altyapı
- [ ] Docker & Docker Compose
- [ ] Redis
- [ ] Kafka
- [ ] Spring Boot
- [ ] Vue 3
- [ ] Testcontainers

### ✅ Checklist
- [ ] Unit test yazıldı
- [ ] Integration test yazıldı
- [ ] Docker compose test edildi
- [ ] Swagger endpoint açık
- [ ] Prometheus metrics aktif

### 📋 Kalan Notlar
[Bu adımdan sonra yapılacaklar]

### 🧪 Test Senaryosu
[Test senaryolarının açıklaması]

### 🎯 Sonuç
[Adımın sonucu ve başarı kriterleri] 