package com.multitenant.multitenancy.config.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import static com.multitenant.multitenancy.config.multitenancy.TenantContext.DEFAULT_SCHEMA;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantUUID = TenantContext.getCurrentTenant();
    return tenantUUID != null ? tenantUUID : DEFAULT_SCHEMA;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
