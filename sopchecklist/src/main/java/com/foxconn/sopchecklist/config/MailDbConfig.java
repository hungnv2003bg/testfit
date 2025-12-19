package com.foxconn.sopchecklist.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

@Configuration
public class MailDbConfig {

    @Bean
    @ConfigurationProperties("mail.datasource")
    @ConditionalOnProperty(name = "mail.queue.enabled", havingValue = "true", matchIfMissing = true)
    public DataSourceProperties mailDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mailDataSource")
    @ConfigurationProperties("mail.datasource.hikari")
    @ConditionalOnProperty(name = "mail.queue.enabled", havingValue = "true", matchIfMissing = true)
    public DataSource mailDataSource() {
        return mailDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "mailJdbcTemplate")
    @ConditionalOnProperty(name = "mail.queue.enabled", havingValue = "true", matchIfMissing = true)
    public JdbcTemplate mailJdbcTemplate(@Qualifier("mailDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mainJdbcTemplate")
    public JdbcTemplate mainJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

