package io.github.flbulgarelli.jpa.extras;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PerThreadEntityManagersTest {

  private PerThreadEntityManagerAccess access = WithSimplePersistenceUnit.PER_THREAD_ENTITY_MANAGER_ACCESS;

  @BeforeEach
  public void disposeEntityManager() {
    access.dispose();
  }

  @Test
  public void entityManagerCanBeConfiguredBeforeDispose() {
    var anotherAccess = new PerThreadEntityManagerAccess(WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME);

    assertDoesNotThrow(() ->
      anotherAccess.configure(properties -> properties
          .set("hibernate.connection.url", "jdbc:h2:mem:test")
          .putAll(System.getenv())
          .load("src/test/resources/test.properties"))
    );
  }

  @Test
  public void entityManagerCannotBeConfiguredAfterDispose() {
    assertThrows(IllegalStateException.class, () -> access.configure(p -> {}));
  }

  @Test
  public void entityManagerIsNotInitiallyAttached() {
    assertFalse(access.isAttached());
  }
  
  @Test
  public void entityManagerIsAttachedOnDemand() {
    assertNotNull(access.get());
    assertTrue(access.isAttached());
  }
  
  @Test
  public void theSameEntityManagerIsConsistentlyReturned() {
    assertSame(access.get(), access.get());
  }
  
  @Test
  public void aDifferentEntityManagerIsReturnedIfDisposed() {
    EntityManager e1 = access.get();
    
    access.dispose();
    
    assertNotSame(e1, access.get());
  }
  
  @Test
  public void aDifferentEntityManagerIsReturnedIfExternallyClosed() {
    EntityManager e1 = access.get();
    
    e1.close();
    
    assertNotSame(e1, access.get());
  }  
}
