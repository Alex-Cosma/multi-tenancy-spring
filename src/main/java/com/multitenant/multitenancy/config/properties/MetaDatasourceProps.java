package com.multitenant.multitenancy.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "meta.datasource")
@Data
public class MetaDatasourceProps {
  private String jdbcUrl;
  private String username;
  private String password;
}
