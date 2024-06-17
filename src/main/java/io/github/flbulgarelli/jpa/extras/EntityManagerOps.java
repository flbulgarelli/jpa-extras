package io.github.flbulgarelli.jpa.extras;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.intellij.lang.annotations.Language;

/**
 * Mixin for adding simple access to common CRUD {@link EntityManager} operations.
 * <p>
 * Implementors need to implement {@link WithEntityManager#entityManager()} only
 *
 * @author flulbarelli
 */
public interface EntityManagerOps extends WithEntityManager {

  /**
   * @see EntityManager#persist(Object)
   */
  default void persist(Object entity) {
    entityManager().persist(entity);
  }

  /**
   * @see EntityManager#merge(Object)
   */
  default <T> T merge(T entity) {
    return entityManager().merge(entity);
  }

  /**
   * @see EntityManager#remove(Object)
   */
  default void remove(Object entity) {
    entityManager().remove(entity);
  }

  /**
   * @see EntityManager#find(Class, Object)
   */
  default <T> T find(Class<T> entityClass, Object primaryKey) {
    return entityManager().find(entityClass, primaryKey);
  }

  /**
   * @see EntityManager#createQuery(String)
   */
  default Query createQuery(@Language("JPAQL") String qlString) {
    return entityManager().createQuery(qlString);
  }

  /**
   * @see EntityManager#createQuery(String, Class)
   */
  default <T> TypedQuery<T> createQuery(@Language("JPAQL") String qlString, Class<T> clazz) {
    return entityManager().createQuery(qlString, clazz);
  }

}
