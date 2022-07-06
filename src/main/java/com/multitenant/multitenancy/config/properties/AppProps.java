package com.multitenant.multitenancy.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProps {
  private String jwtSecret;
  private Long jwtExpirationMs;
}
