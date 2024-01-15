package io.github.flbulgarelli.jpa.extras.javalin;

import io.github.flbulgarelli.jpa.extras.EntityManagerOps;
import io.github.flbulgarelli.jpa.extras.TransactionalOps;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerProperties;
import io.github.flbulgarelli.jpa.extras.perthread.WithPerThreadEntityManager;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.plugin.ContextPlugin;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;

public class JavalinJpaExtras
      extends ContextPlugin<PerThreadEntityManagerAccess, JavalinJpaExtras>
      implements WithPerThreadEntityManager, EntityManagerOps, TransactionalOps {
  private static final String SIMPLE_PERSISTENCE_UNIT_NAME = WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME;

  public JavalinJpaExtras() {
    this(SIMPLE_PERSISTENCE_UNIT_NAME);
  }

  public JavalinJpaExtras(String persistenceUnitName) {
    this(persistenceUnitName, properties -> {});
  }

  public JavalinJpaExtras(Consumer<PerThreadEntityManagerProperties> config) {
    this(SIMPLE_PERSISTENCE_UNIT_NAME, config);
  }

  public JavalinJpaExtras(String persistenceUnitName, Consumer<PerThreadEntityManagerProperties> config) {
    super(
        perThreadEntityManagerAccess -> perThreadEntityManagerAccess.configure(config),
        new PerThreadEntityManagerAccess(persistenceUnitName)
    );
  }

  @Override
  public void onInitialize(JavalinConfig config) {
    config.router.mount(router -> router.after(ctx -> {
      if (perThreadEntityManagerAccess().isAttached()) {
        if (getTransaction().isActive()) {
          throw new IllegalStateException("Can not dispose entity manager if a transaction is active. Ensure it has been already terminated");
        }
        perThreadEntityManagerAccess().dispose();
      }
    }));
  }

  @Override
  public JavalinJpaExtras createExtension(@NotNull Context context) {
    return this;
  }

  @Override
  public EntityManager entityManager() {
    return perThreadEntityManagerAccess().get();
  }

  @Override
  public PerThreadEntityManagerAccess perThreadEntityManagerAccess() {
    return pluginConfig;
  }
}
