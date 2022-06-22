package com.multitenant.multitenancy;

import com.multitenant.multitenancy.config.db.flyway.FlywayService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

//@SpringBootApplication
@RequiredArgsConstructor
public class InitRunner {

  private final FlywayService flywayService;
  private final ApplicationContext context;

  public static void main(String[] args) {
    new SpringApplicationBuilder(InitRunner.class).web(WebApplicationType.NONE).run(args);
  }

  @PostConstruct
  public void run() {
    flywayService.initMetadataSchema();
    flywayService.initNewTenantSchema("public");
    System.exit(SpringApplication.exit(context, () -> 0));
  }

}
