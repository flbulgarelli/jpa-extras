package io.github.flbulgarelli.jpa.extras;

import org.hibernate.Transaction;

import java.util.function.Supplier;

import javax.persistence.EntityTransaction;

/**
 * @author flbulgarelli
 * @author gprieto
 */
public interface TransactionalOps extends WithEntityManager {

  /**
   * Runs an action in the context of a transaction.
   * This method beings a transaction and then commits or rollbacks it
   * as necessary.
   *
   * This method is a more specific version of
   * {@link #withTransaction(Supplier)},
   * which also allows to return an object within the action.
   *
   * @param action the action to execute
   * @see #withTransaction(Supplier)
   */
  default void withTransaction(Runnable action) {
    withTransaction(() -> {
      action.run();
      return null;
    });
  }

  /**
   * Runs an action in the context of a transaction.
   * This method beings a transaction and then commits or rollbacks it
   * as necessary.
   *
   * @param action the action to execute
   * @return the suppliers result
   * @throws RuntimeException if actions fails with a RuntimeException
   *
   * @see Transaction#begin()
   * @see Transaction#commit()
   * @see Transaction#rollback()
   */
  default <A> A withTransaction(Supplier<A> action) {
    beginTransaction();
    try {
      A result = action.get();
      commitTransaction();
      return result;
    } catch (Throwable e) {
      rollbackTransaction();
      throw e;
    }
  }

  default EntityTransaction getTransaction() {
    return entityManager().getTransaction();
  }

  /**
   * Begins a transaction if there is no active current transaction yet.
   * <p>
   * Unlike {@link EntityTransaction#begin()}, this method never fails with
   * {@link IllegalStateException}
   *
   * @return the current active transaction
   * @see EntityTransaction#begin()
   */
  default EntityTransaction beginTransaction() {
    EntityTransaction tx = getTransaction();
    if (!tx.isActive()) {
      tx.begin();
    }
    return tx;
  }

  default void commitTransaction() {
    EntityTransaction tx = getTransaction();
    if (tx.isActive()) {
      tx.commit();
    }
  }

  default void rollbackTransaction() {
    EntityTransaction tx = getTransaction();
    if (tx.isActive()) {
      tx.rollback();
    }
  }
}
