package com.uni.kitcheniq.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class DataSourceShutdown {

    private final HikariDataSource hikariDataSource;

    public DataSourceShutdown(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @PreDestroy
    public void shutdown() {
        if (hikariDataSource != null) {
            hikariDataSource.close();
            System.out.println("HikariCP cerrado correctamente");
        }
    }
}
