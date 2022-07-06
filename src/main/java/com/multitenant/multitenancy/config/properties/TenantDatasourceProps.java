package com.multitenant.multitenancy.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenant.datasource")
@Data
public class TenantDatasourceProps {
  private String jdbcUrl;
  private String username;
  private String password;
}
