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
      extends ContextPlugin<Consumer<PerThreadEntityManagerProperties>, JavalinJpaExtras>
      implements WithPerThreadEntityManager, EntityManagerOps, TransactionalOps {
  public final PerThreadEntityManagerAccess managerAccess;

  public JavalinJpaExtras() {
    this(WithSimplePersistenceUnit.PER_THREAD_ENTITY_MANAGER_ACCESS);
  }

  public JavalinJpaExtras(PerThreadEntityManagerAccess managerAccess) {
    this(managerAccess, properties -> {});
  }

  public JavalinJpaExtras(Consumer<PerThreadEntityManagerProperties> pluginConfig) {
    this(WithSimplePersistenceUnit.PER_THREAD_ENTITY_MANAGER_ACCESS, pluginConfig);
  }

  public JavalinJpaExtras(PerThreadEntityManagerAccess managerAccess,
                          Consumer<PerThreadEntityManagerProperties> pluginConfig) {
    this.managerAccess = managerAccess;
    this.pluginConfig = pluginConfig;
  }

  @Override
  public void onInitialize(JavalinConfig config) {
    this.managerAccess.configure(pluginConfig);
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
    return managerAccess;
  }
}
