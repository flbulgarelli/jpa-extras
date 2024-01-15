package io.github.flbulgarelli.jpa.extras;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.flbulgarelli.jpa.extras.javalin.JavalinJpaExtras;
import io.javalin.Javalin;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;

class JavalinJpaExtrasTest {
  @Test
  void canConfigureJavalinApp() throws IOException, InterruptedException, ExecutionException {
    var emFuture = new CompletableFuture<EntityManager>();

    Javalin.create(javalinConfig -> javalinConfig.registerPlugin(new JavalinJpaExtras()))
        .get("/", ctx -> {
          var em = ctx.with(JavalinJpaExtras.class).entityManager();
          emFuture.complete(em);
        })
        .start(8080);

    HttpClient.newHttpClient().send(
        HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/")).build(),
        HttpResponse.BodyHandlers.discarding()
    );

    assertNotNull(emFuture.get());
  }
}
