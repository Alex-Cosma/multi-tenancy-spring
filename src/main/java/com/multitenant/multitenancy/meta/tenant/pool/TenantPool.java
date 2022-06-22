package com.multitenant.multitenancy.meta.tenant.pool;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Entity
@Table(name = "tenant_pool")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class TenantPool {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "date_created")
  private LocalDate dateCreated;

  @Column(name = "schema")
  private String schema;

  @Column(name = "used")
  @Builder.Default
  private boolean used = false;

  void update(boolean isUsed) {
    this.used = isUsed;
  }
}
