package com.example.informationsystemslab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class JpaConfig {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();

        // сравнивать Java-сущности (@Entity) с текущей схемой БД и создать/дополнить таблицы автоматически
        adapter.setGenerateDdl(true);

        // логирование сгенерированных SQL-запросов в консоль
        adapter.setShowSql(true);

        return adapter;
    }
}