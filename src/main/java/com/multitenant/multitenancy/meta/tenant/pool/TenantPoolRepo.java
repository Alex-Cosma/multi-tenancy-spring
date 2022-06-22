package com.multitenant.multitenancy.meta.tenant.pool;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static javax.persistence.LockModeType.PESSIMISTIC_READ;

public interface TenantPoolRepo extends CrudRepository<TenantPool, Long> {

  @Lock(PESSIMISTIC_READ)
  Optional<TenantPool> findFirstByUsedIsFalseOrderByDateCreatedAsc();

  long countByUsedFalse();

  @Query(nativeQuery = true, value = "SELECT nextval('tenant_sequence') AS tenantId")
  Long getNextTenantCount();

  @Transactional
  void deleteAllBySchema(String schema);

}
