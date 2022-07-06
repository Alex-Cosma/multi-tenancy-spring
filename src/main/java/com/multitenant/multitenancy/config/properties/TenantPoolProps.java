package com.multitenant.multitenancy.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenant.pool")
@Data
public class TenantPoolProps {
  private int size;
}
