package com.multitenant.multitenancy.meta.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantService {
  private final TenantRepo tenantRepo;

  public List<Tenant> findAll() {
    return StreamSupport.stream(tenantRepo.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  public Set<String> getSchemas() {
    return findAll().stream().map(Tenant::getSchema).collect(Collectors.toSet());
  }

  public void delete(String schema) {
    tenantRepo.deleteBySchema(schema);
  }
}
