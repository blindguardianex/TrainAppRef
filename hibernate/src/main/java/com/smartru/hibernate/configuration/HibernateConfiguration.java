package com.smartru.hibernate.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;
    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriver;
    @Value("${spring.jpa.database-platform}")
    private String hibernateDialect;
    @Value("${spring.jpa.show-sql}")
    private String hibernateIsShowSql;

    @Bean
    public SessionFactory sessionFactory(MetadataSources metadataSources){
        return metadataSources.getMetadataBuilder().build().buildSessionFactory();
    }

    @Bean
    public ServiceRegistry serviceRegistry(){
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.configure("hibernate.cfg.xml")
                .applySetting("hibernate.connection.url", databaseUrl)
                .applySetting("hibernate.connection.username", databaseUsername)
                .applySetting("hibernate.connection.password", databasePassword)
                .applySetting("hibernate.connection.driver_class", databaseDriver)
                .applySetting("hibernate.dialect",hibernateDialect)
                .applySetting("hibernate.show_sql", hibernateIsShowSql);

        return serviceRegistryBuilder.build();
    }

    @Bean
    public MetadataSources metadataSources(ServiceRegistry registry){
        MetadataSources metadataSources = new MetadataSources(registry);

        return metadataSources;
    }
}
