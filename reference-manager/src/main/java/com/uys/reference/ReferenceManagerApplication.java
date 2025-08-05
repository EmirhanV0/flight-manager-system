package com.uys.reference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Reference Manager Service - Ana uygulama sınıfı
 * 
 * Bu servis havacılık sektörü için referans veri yönetimini sağlar:
 * - Airline (Havayolu şirketi) yönetimi
 * - Aircraft (Uçak) yönetimi  
 * - Station (Havaalanı) yönetimi
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class ReferenceManagerApplication {

    /**
     * Ana uygulama başlatma metodu
     * 
     * @param args komut satırı argümanları
     */
    public static void main(String[] args) {
        SpringApplication.run(ReferenceManagerApplication.class, args);
    }
} 