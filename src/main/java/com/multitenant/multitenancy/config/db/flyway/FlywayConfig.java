package com.multitenant.multitenancy.config.db.flyway;//package com.datacave.acteamo.config.db.flyway;

import static org.flywaydb.core.api.MigrationVersion.LATEST;

import java.util.Set;
import javax.annotation.PostConstruct;

import com.multitenant.multitenancy.meta.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

  @Value("${tenant.datasource.jdbc-url}")
  private String tenantDbUrl;

  @Value("${tenant.datasource.username}")
  private String tenantDbUsername;

  @Value("${tenant.datasource.password}")
  private String tenantDbPassword;

  @Value("${meta.datasource.jdbc-url}")
  private String metadataDbUrl;

  @Value("${meta.datasource.username}")
  private String metadataDbUsername;

  @Value("${meta.datasource.password}")
  private String metadataDbPassword;

  private final TenantService tenantService;

  @PostConstruct
  public void migrateFlyway() {
    final Set<String> schemas = tenantService.getSchemas();

    schemas.forEach(
        tenant -> {
          Flyway tenantDbMigration =
              Flyway.configure()
                  .dataSource(tenantDbUrl, tenantDbUsername, tenantDbPassword)
                  .locations("classpath:migrations/tenant")
                  .target(LATEST)
                  .baselineOnMigrate(true)
                  .defaultSchema(tenant)
                  .load();
          tenantDbMigration.migrate();
        });

    Flyway metadataDbMigration =
        Flyway.configure()
            .dataSource(metadataDbUrl, metadataDbUsername, metadataDbPassword)
            .locations("classpath:migrations/metadata")
            .baselineOnMigrate(true)
            .target(LATEST)
            .load();

    metadataDbMigration.migrate();
  }
}
