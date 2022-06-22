package com.multitenant.multitenancy.job;

import com.multitenant.multitenancy.meta.tenant.pool.TenantPoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@Transactional
@RequiredArgsConstructor
public class TenantPoolJob {

  private final TenantPoolService tenantPoolService;

  @Scheduled(cron = "0 * * * * *")
  public void execute() {
    log.info("TenantPoolJob");
    tenantPoolService.fillUpTenantPool();
    log.info("TenantPoolJob finished");
  }
}
