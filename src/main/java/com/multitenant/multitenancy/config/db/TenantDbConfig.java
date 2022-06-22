package com.multitenant.multitenancy.config.db;

import com.multitenant.multitenancy.persistence.QuerydslJpaRepositoryFactoryBean;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
    repositoryFactoryBeanClass = QuerydslJpaRepositoryFactoryBean.class,
    basePackages = "com.multitenant.multitenancy.tenant",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager")
public class TenantDbConfig {

  @Autowired
  private JpaProperties jpaProperties;

  @Bean
  JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  @ConfigurationProperties("tenant.datasource")
  public DataSource tenantDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
      MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
      CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {

    Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
    jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
    jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(tenantDataSource());
    em.setPackagesToScan("com.multitenant.multitenancy.tenant");
    em.setJpaVendorAdapter(this.jpaVendorAdapter());
    em.setJpaPropertyMap(jpaPropertiesMap);
    em.setPersistenceUnitName("tenantDB");
    return em;
  }

  @Bean
  public PlatformTransactionManager tenantTransactionManager(
      @Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManagerFactory) {
    return new JpaTransactionManager(tenantEntityManagerFactory);
  }
}
