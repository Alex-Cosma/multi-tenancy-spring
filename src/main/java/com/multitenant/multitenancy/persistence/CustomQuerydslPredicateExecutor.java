package com.multitenant.multitenancy.persistence;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CustomQuerydslPredicateExecutor<T> extends QuerydslPredicateExecutor<T> {
  <P> Optional<P> findOne(@NonNull JPQLQuery<P> query);

  <P> Optional<P> findOne(@NonNull FactoryExpression<P> factoryExpression,
                          @NonNull Predicate predicate);

  <P> List<P> findAll(@NonNull JPQLQuery<P> query);

  <P> Page<P> findAll(@NonNull JPQLQuery<P> query, @NonNull Pageable pageable);

  <P> List<P> findAll(@NonNull FactoryExpression<P> factoryExpression,
                      @NonNull Predicate predicate);

  <P> Page<P> findAll(@NonNull FactoryExpression<P> factoryExpression,
                      @NonNull Predicate predicate,
                      @NonNull Pageable pageable);
}
