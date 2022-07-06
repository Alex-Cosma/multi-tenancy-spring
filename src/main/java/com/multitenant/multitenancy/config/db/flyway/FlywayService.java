package com.multitenant.multitenancy.config.db.flyway;

import com.multitenant.multitenancy.config.properties.MetaDatasourceProps;
import com.multitenant.multitenancy.config.properties.TenantDatasourceProps;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.flywaydb.core.api.MigrationVersion.LATEST;

@Service
@RequiredArgsConstructor
public class FlywayService {
  private static final String METADATA_SCHEMA_NAME = "public";

  private final TenantDatasourceProps tenantDatasourceProps;
  private final MetaDatasourceProps metaDatasourceProps;

  public void initNewTenantSchema(String schema) {
    Flyway tenantDbMigration =
        Flyway.configure()
            .dataSource(
                tenantDatasourceProps.getJdbcUrl(),
                tenantDatasourceProps.getUsername(),
                tenantDatasourceProps.getPassword()
            )
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
            .dataSource(
                metaDatasourceProps.getJdbcUrl(),
                metaDatasourceProps.getUsername(),
                metaDatasourceProps.getPassword()
            )
            .locations("classpath:migrations/metadata")
            .target(LATEST)
            .baselineOnMigrate(true)
            .schemas(METADATA_SCHEMA_NAME)
            .load();
    tenantDbMigration.migrate();
  }
}
