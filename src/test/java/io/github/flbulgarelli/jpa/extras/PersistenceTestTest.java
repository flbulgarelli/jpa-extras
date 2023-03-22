package io.github.flbulgarelli.jpa.extras;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.flbulgarelli.jpa.extras.test.PersistenceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersistenceTestTest implements PersistenceTest, WithSimplePersistenceUnit {

  @Test
  public void transactionIsActive() {
    Assertions.assertTrue(getTransaction().isActive());
  }

  @Test
  public void entityManagerIsAttached() {
    Assertions.assertTrue(perThreadEntityManagerAccess().isAttached());
  }

  @Test
  public void accessIsActive() {
    Assertions.assertTrue(perThreadEntityManagerAccess().isActive());
  }
}
