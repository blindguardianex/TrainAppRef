package com.smartru.hibernate.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    @Bean
    public SessionFactory sessionFactory(MetadataSources metadataSources){
        return metadataSources.getMetadataBuilder().build().buildSessionFactory();
    }

    @Bean
    public ServiceRegistry serviceRegistry(){
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.configure("hibernate.cfg.xml");

        return serviceRegistryBuilder.build();
    }

    @Bean
    public MetadataSources metadataSources(ServiceRegistry registry){
        MetadataSources metadataSources = new MetadataSources(registry);

        return metadataSources;
    }
}
