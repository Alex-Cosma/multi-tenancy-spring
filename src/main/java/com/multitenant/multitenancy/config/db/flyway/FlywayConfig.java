package com.multitenant.multitenancy.config.db.flyway;//package com.datacave.acteamo.config.db.flyway;

import static org.flywaydb.core.api.MigrationVersion.LATEST;

import java.util.Set;
import javax.annotation.PostConstruct;

import com.multitenant.multitenancy.config.properties.MetaDatasourceProps;
import com.multitenant.multitenancy.config.properties.TenantDatasourceProps;
import com.multitenant.multitenancy.meta.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

  private final TenantDatasourceProps tenantDatasourceProps;
  private final MetaDatasourceProps metaDatasourceProps;
  private final TenantService tenantService;

  @PostConstruct
  public void migrateFlyway() {
    final Set<String> schemas = tenantService.getSchemas();

    schemas.forEach(
        tenant -> {
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
                  .defaultSchema(tenant)
                  .load();
          tenantDbMigration.migrate();
        });

    Flyway metadataDbMigration =
        Flyway.configure()
            .dataSource(
                metaDatasourceProps.getJdbcUrl(),
                metaDatasourceProps.getUsername(),
                metaDatasourceProps.getPassword()
            )
            .locations("classpath:migrations/metadata")
            .baselineOnMigrate(true)
            .target(LATEST)
            .load();

    metadataDbMigration.migrate();
  }
}
