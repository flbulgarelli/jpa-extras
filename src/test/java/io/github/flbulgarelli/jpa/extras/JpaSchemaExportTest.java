package io.github.flbulgarelli.jpa.extras;

import io.github.flbulgarelli.jpa.extras.export.JpaSchemaExport;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class JpaSchemaExportTest {

  @Test
  void canGenerateSchemaWithoutFormat() throws IOException {
    var schema = Files.createTempFile("schema", ".sql");
    JpaSchemaExport.execute(WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME, schema.toString(), false);
    assertEquals(
        """
        create sequence hibernate_sequence start with 1 increment by 1;%1$s\
        create table Persistables (id bigint not null, aDate date, aString varchar(255), primary key (id));%1$s\
        create table Users (id bigint not null, primary key (id));%1$s\
        """.formatted(System.lineSeparator()),
        Files.readString(schema)
    );
  }

  @Test
  void canGenerateSchemaWithFormat() throws IOException {
    var schema = Files.createTempFile("schema", ".sql");
    JpaSchemaExport.execute(WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME, schema.toString(), true);
    assertEquals(
        """
        create sequence hibernate_sequence start with 1 increment by 1;%1$s\
        %1$s\
            create table Persistables (%1$s\
               id bigint not null,%1$s\
                aDate date,%1$s\
                aString varchar(255),%1$s\
                primary key (id)%1$s\
            );%1$s\
        %1$s\
            create table Users (%1$s\
               id bigint not null,%1$s\
                primary key (id)%1$s\
            );%1$s\
        """.formatted(System.lineSeparator()),
        Files.readString(schema)
    );
  }

}
