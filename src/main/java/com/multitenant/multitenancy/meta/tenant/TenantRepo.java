package com.multitenant.multitenancy.meta.tenant;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface TenantRepo extends CrudRepository<Tenant, Long> {
  Stream<Tenant> findAllByStateNot(TenantState state);

  void deleteBySchema(String schema);
}
