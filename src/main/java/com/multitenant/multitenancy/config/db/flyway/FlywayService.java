package com.multitenant.multitenancy.config.db.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.flywaydb.core.api.MigrationVersion.LATEST;

@Service
public class FlywayService {
  private static final String METADATA_SCHEMA_NAME = "public";

  @Value("${tenant.datasource.jdbc-url}")
  private String tenantDbUrl;

  @Value("${tenant.datasource.username}")
  private String tenantDbUsername;

  @Value("${tenant.datasource.password}")
  private String tenantDbPassword;

  @Value("${meta.datasource.jdbc-url}")
  private String metaDbUrl;

  @Value("${meta.datasource.username}")
  private String metaDbUsername;

  @Value("${meta.datasource.password}")
  private String metaDbPassword;

  public void initNewTenantSchema(String schema) {
    Flyway tenantDbMigration =
        Flyway.configure()
            .dataSource(tenantDbUrl, tenantDbUsername, tenantDbPassword)
            .locations("classpath:migrations/tenant")
            .target(LATEST)
            .baselineOnMigrate(true)
            .schemas(schema)
            .load();
    tenantDbMigration.migrate();
  }

  public void initMetadataSchema() {
    Flyway tenantDbMigration =
        Flyway.configure()
            .dataSource(metaDbUrl, metaDbUsername, metaDbPassword)
            .locations("classpath:migrations/metadata")
            .target(LATEST)
            .baselineOnMigrate(true)
            .schemas(METADATA_SCHEMA_NAME)
            .load();
    tenantDbMigration.migrate();
  }
}
