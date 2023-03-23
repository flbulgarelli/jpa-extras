package io.github.flbulgarelli.jpa.extras.perthread;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * @author raniagus
 */
public class PerThreadEntityManagerProperties {
  private final Properties properties = new Properties();

  /**
   * Sets a property before the entity manager factory is created.
   *
   * @param key the property key
   * @param value the property value
   * @throws NullPointerException if the key or value is null
   */
  public PerThreadEntityManagerProperties set(String key, String value) {
    this.properties.setProperty(key, value);
    return this;
  }

  /**
   * Sets a map of properties to be used when creating the entity manager factory.
   *
   * @param properties the properties to set
   * @throws NullPointerException if the properties map is null
   */
  public <K, V> PerThreadEntityManagerProperties putAll(Map<K, V> properties) {
    this.properties.putAll(properties);
    return this;
  }

  /**
   * Loads properties from a file and sets them before the entity manager factory is created.
   *
   * @param path the path to the properties file
   * @throws UncheckedIOException if the file can not be read
   */
  public PerThreadEntityManagerProperties load(String path) {
    try (var stream = Files.newInputStream(Paths.get(path))) {
      this.properties.load(stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return this;
  }

  /**
   * Returns the properties for the entity manager.
   */
  public Properties get() {
    return properties;
  }
}
