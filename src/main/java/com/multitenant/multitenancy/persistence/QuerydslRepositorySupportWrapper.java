package com.multitenant.multitenancy.persistence;

import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

@Repository
public abstract class QuerydslRepositorySupportWrapper {

  protected final PathBuilder<?> builder;

  @Nullable protected EntityManager entityManager;
  @Nullable protected Querydsl querydsl;

  public QuerydslRepositorySupportWrapper(Class<?> domainClass) {

    Assert.notNull(domainClass, "Domain class must not be null!");
    this.builder = new PathBuilderFactory().create(domainClass);
  }

  @PostConstruct
  public void validate() {
    Assert.notNull(entityManager, "EntityManager must not be null!");
    Assert.notNull(querydsl, "Querydsl must not be null!");
  }

  @Nullable
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  protected JPQLQuery<Object> from(EntityPath<?>... paths) {
    return getRequiredQuerydsl().createQuery(paths);
  }

  protected <T> JPQLQuery<T> from(EntityPath<T> path) {
    return getRequiredQuerydsl().createQuery(path).select(path);
  }

  protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {
    return new JPADeleteClause(getRequiredEntityManager(), path);
  }

  protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {
    return new JPAUpdateClause(getRequiredEntityManager(), path);
  }

  @SuppressWarnings("unchecked")
  protected <T> PathBuilder<T> getBuilder() {
    return (PathBuilder<T>) builder;
  }

  @Nullable
  protected Querydsl getQuerydsl() {
    return this.querydsl;
  }

  private Querydsl getRequiredQuerydsl() {

    if (querydsl == null) {
      throw new IllegalStateException("Querydsl is null!");
    }

    return querydsl;
  }

  private EntityManager getRequiredEntityManager() {

    if (entityManager == null) {
      throw new IllegalStateException("EntityManager is null!");
    }

    return entityManager;
  }

  protected <T> Page<T> paginatedQuery(JPQLQuery<T> query, Pageable pageable) {
    final long totalElements = query.fetchCount();
    List<T> result = getQuerydsl().applyPagination(pageable, query).fetch();

    return new PageImpl<>(result, pageable, totalElements);
  }

  protected <T> void addSortingToQuery(Sort sorting, JPAQuery<T> q, Class<?> c, String variable) {
    for (Sort.Order o : sorting) {
      PathBuilder<Object> orderByExpression = new PathBuilder<>(c, variable);
      q.orderBy(
          new OrderSpecifier(o.isAscending() ? ASC : DESC, orderByExpression.get(o.getProperty())));
    }
  }
}
