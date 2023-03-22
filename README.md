JPA/Hibernate Java 11+ Extras
==============================

This is a small library that simplifies usage of JPA/Hibernate. It adds:

  1. Better support for entity manager injection using mixin-like inheritance
  2. Better support for transactional code
  3. Support for persisting lambdas
  4. Junit 5 Persistence Tests
  5. Schema Generation * `remove`

## 1. Entity Manager injection

Just add `WithSimplePersistenceUnit`:

```java
import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class UserRepository implements WithSimplePersistenceUnit {

  public static UserRepository INSTANCE = new UserRepository();

  public void addUser(User user) {
    persist(user);
  }

  public User find(Long id) {
    return find(User.class, id);
  }
}
```

By using this mixin, you'll get access to an `entityManager()`, plus some helper methods that act as shortcuts to
the corresponding `EntityManager`:

 * `persist`
 * `merge`
 * `remove`
 * `find`
 * `createQuert`

## 4. Persistence Tests

In order to test code that has access to persistence context, just add the `SimplePersistenceTest` mixin to your Junit 5 test classes:

```java
import com.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

public class ATest implements SimplePersistenceTest {
  @Test
  void aTestThatRequiresAPersistenceContext() {
    // ....
  }
}
```

By using this mixin, all your code is going to be run within a transaction. Also, you get access to an `entityManager()` and
the `EntityManagerOps` helpers:

```java
import com.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

public class ATest implements SimplePersistenceTest {
  @Test
  void aTestThatRequiresAPersistenceContext() {
    User user = new User();
    persist(user);
    assertNotNull(user.getId());
  }
}
```

## 5. Schema Generation

JPA does not provide an easy way to generate the SQL schema. With this library, you can generate it by executing `JpaSchemaExport`.

- Usage: `JpaSchemaExport [...options]`

- Options:
  - `-o,--output <arg>`: Output file. Defaults to `schema.sql`.
  - `-f,--format`: If the output file should be formatted. Defaults to `false`.
  - `-t,--target <arg>`: Target persistence unit. Defaults to `simple-persistence-unit`.
  - `-h,--help`: Print this message.

If you are using IntelliJ, you can add a Run Configuration with the following parameters:

- Main class: `com.github.flbulgarelli.jpa.extras.export.JpaSchemaExport`
- Program arguments: `-o schema.sql -f`
- `Modify Options` -> `Add Run Options`-> `Java` -> `Add provided dependencies with "provided" scope to classpath`

![run/debug configuration](https://user-images.githubusercontent.com/39303639/194677296-86d6395e-5f42-4500-962a-677ad28d613b.png)

## Installation

jpa-extras is hosted in the Maven Central Repository. Simply add the following
dependency into your `pom.xml` file:

```xml
    <dependency>
      <groupId>com.github.flbulgarelli</groupId>
      <artifactId>jpa-extras</artifactId>
      <version>1.0.0</version>
    </dependency>
```

### Snapshots

Also, snapshots of the master branch are deployed automatically with each
successful commit. Instead of Maven Central, use the Sonatype snapshots
repository at:

```xml
<url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
```

You can add the repository in your `pom.xml` file:

```xml
  <repositories>
    <repository>
      <id>sonatype-snapshots</id>
      <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>
  </repositories>
```
