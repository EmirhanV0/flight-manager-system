# 🧪 UYS Reference Manager Service - Tests

## 📋 Test Strategy

Bu proje kapsamlı test stratejisi ile geliştirilmiştir:

### 🎯 Test Piramidi
```
        /\
       /  \
      /E2E \     <- Integration Tests (Controller)
     /______\
    /        \
   / Unit     \   <- Service, Repository, Mapper Tests
  /__________\
```

## 🧪 Test Katmanları

### 1. **Unit Tests**
- **Konum**: `src/test/java/com/uys/reference/service/`
- **Amaç**: İzole test edilen business logic
- **Teknoloji**: JUnit 5, Mockito
- **Kapsam**: Service layer metodları

**Örnek:**
```java
@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {
    @Mock private AirlineRepository repository;
    @InjectMocks private AirlineService service;
    
    @Test
    void createAirline_Success() {
        // Given, When, Then
    }
}
```

### 2. **Repository Integration Tests**
- **Konum**: `src/test/java/com/uys/reference/repository/`
- **Amaç**: JPA repository metodları ve custom query'ler
- **Teknoloji**: @DataJpaTest, Testcontainers MySQL
- **Kapsam**: Database etkileşimi

**Örnek:**
```java
@DataJpaTest
@Testcontainers
class AirlineRepositoryIT {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
}
```

### 3. **Controller Integration Tests**
- **Konum**: `src/test/java/com/uys/reference/controller/`
- **Amaç**: REST API endpoint'leri end-to-end test
- **Teknoloji**: @SpringBootTest, MockMvc, Testcontainers
- **Kapsam**: HTTP request/response, validation, serialization

### 4. **Kafka Integration Tests**
- **Konum**: `src/test/java/com/uys/reference/event/`
- **Amaç**: Event publishing ve Kafka entegrasyonu
- **Teknoloji**: Testcontainers Kafka, KafkaConsumer
- **Kapsam**: Event message'ları ve Kafka interaction

## 🚀 Test Çalıştırma

### Tüm Testler
```bash
mvn test
```

### Sadece Unit Tests
```bash
mvn test -Dtest="*Test"
```

### Sadece Integration Tests
```bash
mvn test -Dtest="*IT"
```

### Specific Test Class
```bash
mvn test -Dtest="AirlineServiceTest"
```

### Test Coverage
```bash
mvn clean test jacoco:report
```

## 📊 Test Coverage Hedefleri

| Katman | Hedef Coverage |
|--------|----------------|
| Service Layer | %90+ |
| Repository Layer | %85+ |
| Controller Layer | %80+ |
| Event Publisher | %85+ |
| **Genel** | **%85+** |

## 🐳 Testcontainers Kullanımı

### MySQL Container
```java
@Container
static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
    .withDatabaseName("uys_reference_test")
    .withUsername("uys_user")
    .withPassword("uys_password");
```

### Kafka Container
```java
@Container
static KafkaContainer kafka = new KafkaContainer(
    DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
);
```

### Redis Container (Gelecek)
```java
@Container
static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
    .withExposedPorts(6379);
```

## 🎯 Test Data Management

### Test Verileri
- Her test metodu kendi test verisini oluşturur
- `@BeforeEach` ile setup
- `@Transactional` ile isolation

### Test Profiles
- **test**: Testcontainers ile gerçek servisler
- **integration**: In-memory H2 database
- **unit**: Mock'lar ile izole testler

## 🔧 Configuration

### application-test.yml
```yaml
spring:
  datasource:
    url: jdbc:tc:mysql:8.0://localhost:3306/uys_reference_test
  jpa:
    hibernate:
      ddl-auto: validate
  liquibase:
    enabled: true
    contexts: test
```

## 📈 Test Metrics

### JaCoCo Reports
Test coverage raporları `target/site/jacoco/` klasöründe oluşturulur.

### Test Execution Time
- Unit Tests: < 5 saniye
- Integration Tests: < 30 saniye
- Full Test Suite: < 60 saniye

## 🛠️ Test Utilities

### Custom Assertions
```java
public class AirlineAssertions {
    public static void assertAirlineEquals(Airline expected, Airline actual) {
        assertEquals(expected.getAirlineCode(), actual.getAirlineCode());
        assertEquals(expected.getAirlineName(), actual.getAirlineName());
        // ...
    }
}
```

### Test Data Builders
```java
public class AirlineTestDataBuilder {
    public static Airline.AirlineBuilder defaultAirline() {
        return Airline.builder()
            .airlineCode("TK")
            .airlineName("Turkish Airlines")
            .country("Turkey")
            .active(true);
    }
}
```

## 🚨 Test Best Practices

### 1. **Naming Convention**
- Test class: `{ClassUnderTest}Test` veya `{ClassUnderTest}IT`
- Test method: `{methodName}_{scenario}_{expectedBehavior}`

### 2. **AAA Pattern**
```java
@Test
void createAirline_ValidInput_ReturnsCreatedAirline() {
    // Given (Arrange)
    AirlineDto.CreateRequest request = createValidRequest();
    
    // When (Act)
    AirlineDto.Response result = airlineService.createAirline(request);
    
    // Then (Assert)
    assertNotNull(result);
    assertEquals("TK", result.getAirlineCode());
}
```

### 3. **Test Independence**
- Her test bağımsız çalışabilmeli
- Test sırası önemli olmamalı
- Shared state kullanılmamalı

### 4. **Meaningful Assertions**
```java
// ❌ Kötü
assertTrue(result.size() > 0);

// ✅ İyi
assertEquals(3, result.size(), "Should return exactly 3 active airlines");
```

## 🔍 Debugging Tests

### IDE'de Debug
```java
@Test
void debugTest() {
    // Breakpoint koy ve debug mode'da çalıştır
    AirlineDto.Response result = airlineService.createAirline(request);
    System.out.println("Result: " + result); // Debug için
}
```

### Log Levels
```yaml
logging:
  level:
    com.uys.reference: DEBUG
    org.springframework.test: DEBUG
    org.testcontainers: DEBUG
```

## 📝 Test Documentation

### Test Scenarios
Her major feature için test scenarios dokümante edilmiştir:

1. **Airline CRUD Operations**
   - ✅ Create valid airline
   - ✅ Create duplicate airline (error)
   - ✅ Update existing airline
   - ✅ Delete airline
   - ✅ Get airline by code
   - ✅ List airlines with pagination

2. **Event Publishing**
   - ✅ Publish CREATE event
   - ✅ Publish UPDATE event
   - ✅ Publish DELETE event
   - ✅ Publish STATUS_CHANGE event

3. **Error Handling**
   - ✅ Invalid input validation
   - ✅ Not found scenarios
   - ✅ Duplicate key violations

## 🎯 Gelecek Test İyileştirmeleri

- [ ] Performance tests (load testing)
- [ ] Contract tests (Pact)
- [ ] Security tests (penetration testing)
- [ ] Chaos engineering tests
- [ ] End-to-end UI tests (Cypress)