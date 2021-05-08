package com.mikkezavala.reactor.repository;

/**
 * The interface Repository.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface Repository <T, R> {

  /**
   * Find by id r.
   *
   * @param id the id
   * @return the r
   */
  R findById(T id);
}
