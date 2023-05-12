package com.zeptolab.zeptolabchatservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

@Configuration
public class PersistenceContext {

    @Value("${zeptolab.datasource.driver}")
    private String driverClassName;

    @Value("${zeptolab.datasource.url}")
    private String jdbcUrl;

    @Value("${zeptolab.database.maxConnections}")
    private int maxPoolSize;

    @Value("${zeptolab.database.minConnections}")
    private int minIdle;

    @Value("${zeptolab.datasource.username}")
    private String username;

    @Value("${zeptolab.datasource.password}")
    private String password;

    @Value("${zeptolab.datasource.dialect}")
    private String dialect;

    @Value("${zeptolab.database.show-sql}")
    private boolean showSql;

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        final Properties properties = new Properties();
        properties.put("url", jdbcUrl);
        properties.put("user", username);
        properties.put("password", password);

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(minIdle);
        hikariConfig.setDataSourceProperties(properties);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final HikariDataSource dataSource) {
        final Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.globally_quoted_identifiers", true);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", true);

        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.zeptolab.zeptolabchatservice.repositories");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Bean("transactionManager")
    public JpaTransactionManager jpaTransactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}