package io.github.flbulgarelli.jpa.extras;


import jakarta.persistence.EntityManager;

public interface WithEntityManager {

  /**
   * Injects a persistence context
   *
   * @return an open, ready to use {@link EntityManager}
   */
  EntityManager entityManager();
}
