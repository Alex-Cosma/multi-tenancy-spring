package com.multitenant.multitenancy.persistence;

import com.multitenant.multitenancy.config.db.flyway.FlywayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(value = "tenantTransactionManager")
public class TenantDatabaseService {

  @Qualifier("tenantEntityManagerFactory")
  private final EntityManager tenantEntityManager;

  private final FlywayService flywayService;

  @SneakyThrows
  public void initSchemas(List<String> tenantIds) {
    tenantIds.forEach(
        schemaName -> {
          rollbackSchema(schemaName);
          flywayService.initNewTenantSchema(schemaName);
        });
  }

  public void rollbackSchema(String schemaName) {
    execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
  }

  public void execute(String sql) {
    tenantEntityManager.createNativeQuery(sql).executeUpdate();
  }
}
