package com.multitenant.multitenancy.meta.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDate;

@Entity
@Table(name = "tenant")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Tenant {
  @Id
  @GeneratedValue
  private Long id;

  private String schema;

  @Enumerated(EnumType.STRING)
  private TenantState state;

  @Version
  private Long version;

  @Column(name = "date_created")
  private LocalDate dateCreated;
}
