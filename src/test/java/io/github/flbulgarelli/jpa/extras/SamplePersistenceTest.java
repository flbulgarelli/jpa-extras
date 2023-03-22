package io.github.flbulgarelli.jpa.extras;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.flbulgarelli.jpa.extras.test.PersistenceTest;
import org.junit.jupiter.api.Test;

public class SamplePersistenceTest implements PersistenceTest, WithSimplePersistenceUnit {
  @Test
  void persistableIsInserted() {
    Persistable persistable = new Persistable();

    assertNull(persistable.getId());
    persist(persistable);
    assertNotNull(persistable.getId());
  }

  @Entity
  @Table(name = "Persistables")
  private static class Persistable {
    @Id
    @GeneratedValue
    private Long id;
    private String aString;
    private LocalDate aDate;

    public Long getId() {
      return id;
    }
  }
}
