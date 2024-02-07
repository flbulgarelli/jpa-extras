package io.github.flbulgarelli.jpa.extras.javalin;

import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerAccess;
import io.github.flbulgarelli.jpa.extras.perthread.PerThreadEntityManagerProperties;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.config.JavalinConfig;
import io.javalin.plugin.Plugin;
import java.util.function.Consumer;

public class JavalinJpaExtras extends Plugin<JavalinJpaExtras.Config> {
  public static class Config {
    public PerThreadEntityManagerAccess managerAccess = WithSimplePersistenceUnit.PER_THREAD_ENTITY_MANAGER_ACCESS;
    public final PerThreadEntityManagerProperties properties = new PerThreadEntityManagerProperties();
  }

  public JavalinJpaExtras(Consumer<Config> pluginConfig) {
    super(pluginConfig, new Config());
  }

  @Override
  public void onInitialize(JavalinConfig config) {
    pluginConfig.managerAccess.configure(properties -> properties.putAll(pluginConfig.properties.get()));
    config.router.mount(router ->
        router.after(ctx -> {
          if (pluginConfig.managerAccess.isAttached()) {
            if (pluginConfig.managerAccess.get().getTransaction().isActive()) {
              throw new IllegalStateException("Can not dispose entity manager if a transaction is active. Ensure it has been already terminated");
            }
            pluginConfig.managerAccess.dispose();
          }
        })
    );
  }
}
