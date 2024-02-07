package io.github.flbulgarelli.jpa.extras;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.flbulgarelli.jpa.extras.javalin.JavalinJpaExtras;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.javalin.Javalin;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JavalinJpaExtrasTest {
  private PerThreadEntityManagerAccess managerAccess;
  private int port;

  @BeforeEach
  void setUp() throws IOException {
    managerAccess = mock(PerThreadEntityManagerAccess.class);
    try (var socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }
  }

  @Test
  void canConfigureJavalinApp() {
    var app = Javalin.create(javalinConfig ->
        javalinConfig.registerPlugin(
            new JavalinJpaExtras(jpaExtrasConfig ->
                jpaExtrasConfig.managerAccess = managerAccess
            )
        )
    );

    app.start(port);

    verify(managerAccess, times(1)).configure(any());
  }
}
