package com.uys.reference.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) Configuration
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api/v1}")
    private String contextPath;

    @Value("${spring.application.name:reference-manager}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8081" + contextPath)
                        .description("Development Server"),
                    new Server()
                        .url("https://api.uys.com" + contextPath)
                        .description("Production Server")
                ))
                .tags(List.of(
                    new Tag()
                        .name("Airline")
                        .description("Havayolu şirketi yönetimi API'leri"),
                    new Tag()
                        .name("Aircraft")
                        .description("Uçak yönetimi API'leri"),
                    new Tag()
                        .name("Station")
                        .description("Havaalanı yönetimi API'leri"),
                    new Tag()
                        .name("Health")
                        .description("Sistem sağlık kontrol API'leri")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("UYS Reference Manager Service API")
                .description("""
                    ## UYS Reference Manager Service
                    
                    Havacılık sektörü için referans veri yönetim sistemi. Bu servis aşağıdaki ana fonksiyonları sağlar:
                    
                    ### 🏢 Airline Management
                    - Havayolu şirketi CRUD operasyonları
                    - Aktif/pasif durum yönetimi
                    - Ülke ve şehir bazlı filtreleme
                    - Arama ve sayfalama
                    
                    ### ✈️ Aircraft Management  
                    - Uçak CRUD operasyonları
                    - Havayolu şirketi ile ilişkilendirme
                    - Teknik özellik yönetimi
                    - Kapasite ve menzil bilgileri
                    
                    ### 🛫 Station Management
                    - Havaalanı CRUD operasyonları
                    - Coğrafi konum bilgileri
                    - Zaman dilimi yönetimi
                    - Şehir ve ülke bazlı gruplama
                    
                    ### 📡 Event Streaming
                    - Kafka ile gerçek zamanlı event publishing
                    - Mikroservisler arası iletişim
                    - Event-driven architecture desteği
                    
                    ### 💾 Caching
                    - Redis ile performans optimizasyonu
                    - Akıllı cache invalidation
                    - Hızlı veri erişimi
                    
                    ---
                    
                    **Teknik Özellikler:**
                    - Spring Boot 3.2.0
                    - Java 17
                    - MySQL 8.0
                    - Redis 7
                    - Apache Kafka
                    - Liquibase DB Migration
                    - MapStruct Mapping
                    - Testcontainers Testing
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("UYS Development Team")
                    .email("dev@uys.com")
                    .url("https://uys.com/contact"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
    }
}