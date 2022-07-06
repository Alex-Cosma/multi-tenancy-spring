package com.multitenant.multitenancy.meta.tenant.pool;

import com.multitenant.multitenancy.config.properties.TenantPoolProps;
import com.multitenant.multitenancy.meta.tenant.TenantService;
import com.multitenant.multitenancy.persistence.TenantDatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class TenantPoolService {

  private final TenantPoolRepo tenantPoolRepo;
  private final TenantService tenantService;
  private final TenantDatabaseService tenantDatabaseService;
  private final TenantPoolProps tenantPoolProps;

  public String takeTenant() {
    TenantPool tenantFromPool = tenantPoolRepo.findFirstByUsedIsFalseOrderByDateCreatedAsc().get();
    tenantFromPool.update(true);
    return tenantFromPool.getSchema();
  }

  public void fillUpTenantPool() {
    final long unusedTenants = tenantPoolRepo.countByUsedFalse();
    if (unusedTenants < tenantPoolProps.getSize()) {
      createEmptyTenant();
    }
  }

  private void createEmptyTenant() {
    String schemaName = generateNewSchemaName();
    try {
      initializeTenant(schemaName);
    } catch (Exception e) {
      log.debug(e);
      rollbackCreateEmptyTenant(schemaName);
    }
  }

  private void initializeTenant(String schema) {
    tenantDatabaseService.initSchemas(List.of(schema));
  }


  private void rollbackCreateEmptyTenant(String schema) {
    log.error(format("Error while creating empty tenant %s", schema));
    tenantPoolRepo.deleteAllBySchema(schema);
    tenantService.delete(schema);
    tenantDatabaseService.rollbackSchema(schema);
  }

  public String generateNewSchemaName() {
    return format("t%07d", tenantPoolRepo.getNextTenantCount());
  }
}
