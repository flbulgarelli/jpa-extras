package io.github.flbulgarelli.jpa.extras.simple;

import io.github.flbulgarelli.jpa.extras.EntityManagerOps;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerProperties;
import io.github.flbulgarelli.jpa.extras.perthread.WithPerThreadEntityManager;

import java.util.function.Consumer;

/**
 * This mixin-style interface allows implementors
 * to have simple, rich-interfaced, per-thread access to an entity manager
 * created from a persistence unit named "simple-persistence-unit".
 */
public interface WithSimplePersistenceUnit extends WithPerThreadEntityManager, EntityManagerOps, TransactionalOps {

  String SIMPLE_PERSISTENCE_UNIT_NAME = "simple-persistence-unit";
  PerThreadEntityManagerAccess PER_THREAD_ENTITY_MANAGER_ACCESS = new PerThreadEntityManagerAccess(SIMPLE_PERSISTENCE_UNIT_NAME);

  @Override
  default PerThreadEntityManagerAccess perThreadEntityManagerAccess() {
    return PER_THREAD_ENTITY_MANAGER_ACCESS;
  }

  /**
   * Exposes the properties that will be used to create the entity manager factory.
   *
   * @param propertiesConsumer a consumer that will be called with the properties object
   * @throws IllegalStateException if the entity manager factory has already been created
   */
  static void configure(Consumer<PerThreadEntityManagerProperties> propertiesConsumer) {
    PER_THREAD_ENTITY_MANAGER_ACCESS.configure(propertiesConsumer);
  }

  /**
   * Disposes the current entity manager and
   * prevents new entity managers to be allocated globally.
   * <p>
   * This method does not close
   * other active managers, so they should be closed before or data may be lost
   *
   * @see PerThreadEntityManagerAccess#shutdown()
   */
  static void shutdown() {
    dispose();
    PER_THREAD_ENTITY_MANAGER_ACCESS.shutdown();
  }

  /**
   * Disposes the current entity manager attached to the current thread, if any.
   * As an additional validation, it will fail if transaction is active
   *
   * @see PerThreadEntityManagerAccess#dispose()
   */
  static void dispose() {
    if (PER_THREAD_ENTITY_MANAGER_ACCESS.isAttached()) {
      if (PER_THREAD_ENTITY_MANAGER_ACCESS.get().getTransaction().isActive()) {
        throw new IllegalStateException("Can not dispose entity manager if a transaction is active. Ensure it has been already terminated");
      }
      PER_THREAD_ENTITY_MANAGER_ACCESS.dispose();
    }
  }
}
