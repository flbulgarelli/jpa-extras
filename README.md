JPA/Hibernate  Extras
==============================

This is a small library that simplifies usage of JPA/Hibernate. It adds:
  * Better support for entity manager injection using mixin-like inheritance
  * Better support for transactional code
  * Support for LocalDate/LocalDateTime persistence
  * Support for persisting lambdas
  * Schema Generation

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

## Schema Generation

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
