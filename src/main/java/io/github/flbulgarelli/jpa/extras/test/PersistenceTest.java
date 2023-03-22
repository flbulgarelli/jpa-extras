package io.github.flbulgarelli.jpa.extras.test;

import io.github.flbulgarelli.jpa.extras.EntityManagerOps;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Mixin for Junit 5 transactional test cases.
 * This mixin does not provide an {@link #entityManager()}, so you still need
 * to define this method.
 *
 * If you want an even simpler, ready-to-use version, use {@link SimplePersistenceTest} instead
 *
 * @author flbulgarell
 */
public interface PersistenceTest extends TransactionalOps, EntityManagerOps {

  @BeforeEach
  default void setupTransaction() {
    beginTransaction();
  }

  @AfterEach
  default void tearDownTransaction() {
    rollbackTransaction();
  }

}
