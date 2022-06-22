package com.multitenant.multitenancy.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.multitenant.multitenancy.meta",
    entityManagerFactoryRef = "metaEntityManagerFactory",
    transactionManagerRef = "metaTransactionManager")
public class MetaDbConfig {

  @Primary
  @Bean
  @ConfigurationProperties("meta.datasource")
  public DataSource metaDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean metaEntityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(metaDataSource())
        .packages("com.multitenant.multitenancy.meta")
        .persistenceUnit("metaDB")
        .build();
  }

  @Primary
  @Bean
  public PlatformTransactionManager metaTransactionManager(
      @Qualifier("metaEntityManagerFactory") EntityManagerFactory userEntityManagerFactory) {
    return new JpaTransactionManager(userEntityManagerFactory);
  }
}

