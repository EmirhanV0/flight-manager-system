package com.uys.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Archive Service - Ana uygulama sınıfı
 *
 * Bu servis sistem genelinde arşivleme ve loglama işlemlerini yönetir:
 * - Event Log arşivleme
 * - System Audit Log
 * - Data Archive (eski veriler)
 * - Search & Reporting
 *
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableKafka
@EnableScheduling
public class ArchiveServiceApplication {

    /**
     * Ana uygulama başlatma metodu
     *
     * @param args komut satırı argümanları
     */
    public static void main(String[] args) {
        SpringApplication.run(ArchiveServiceApplication.class, args);
    }
}