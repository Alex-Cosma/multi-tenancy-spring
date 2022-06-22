package com.multitenant.multitenancy.config.multitenancy;

public class TenantContext {

  public static String DEFAULT_SCHEMA = "public";

  private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  public static void setCurrentTenant(String tenantUUID) {
    currentTenant.set(tenantUUID);
  }

  public static void clear() {
    currentTenant.set(null);
  }
}
