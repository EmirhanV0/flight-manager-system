package com.uys.flight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Flight Service - Ana uygulama sınıfı
 *
 * Bu servis havacılık sektörü için uçuş yönetimini sağlar:
 * - Flight (Uçuş) yönetimi
 * - FlightLeg (Uçuş bacağı) yönetimi  
 * - FlightSchedule (Uçuş programı) yönetimi
 * - Reference Manager entegrasyonu
 *
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableKafka
public class FlightServiceApplication {

    /**
     * Ana uygulama başlatma metodu
     *
     * @param args komut satırı argümanları
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightServiceApplication.class, args);
    }
}