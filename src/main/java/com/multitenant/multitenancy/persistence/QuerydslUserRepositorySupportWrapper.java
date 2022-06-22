package com.multitenant.multitenancy.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;

@Repository
public abstract class QuerydslUserRepositorySupportWrapper extends QuerydslRepositorySupportWrapper {

  public QuerydslUserRepositorySupportWrapper(Class<?> domainClass) {
    super(domainClass);
  }

  @Autowired
  public void setEntityManager(@Qualifier("userEntityManagerFactory") EntityManager entityManager) {

    Assert.notNull(entityManager, "EntityManager must not be null!");
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
  }

}
