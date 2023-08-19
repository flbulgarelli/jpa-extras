package io.github.flbulgarelli.jpa.extras;


import javax.persistence.EntityManager;

public interface WithEntityManager {

  /**
   * Injects a persistence context
   *
   * @return an open, ready to use {@link EntityManager}
   */
  EntityManager entityManager();
}
