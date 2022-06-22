package com.multitenant.multitenancy.config.multitenancy;

import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.multitenant.multitenancy.config.multitenancy.TenantContext.DEFAULT_SCHEMA;


@Component
@Log4j2
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

  private final DataSource dataSource;

  public TenantConnectionProvider(@Qualifier("tenantDataSource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    log.info("Get connection for tenant {}", tenantIdentifier);
    final Connection connection = getAnyConnection();
    connection.setSchema(tenantIdentifier);
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
    log.info("Release connection for tenant {}", tenantIdentifier);
    connection.setSchema(DEFAULT_SCHEMA);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @Override
  public boolean isUnwrappableAs(Class unwrapType) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return null;
  }
}
