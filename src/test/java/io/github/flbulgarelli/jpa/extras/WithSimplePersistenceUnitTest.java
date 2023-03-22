package io.github.flbulgarelli.jpa.extras;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WithSimplePersistenceUnitTest implements SimplePersistenceTest {

  @Test
  void canDefineARepositoryUsingThisMixin() {
    var user = new SampleUser();
    SampleUserRepository.INSTANCE.addUser(user);

    assertNotNull(user.getId());
  }
}
