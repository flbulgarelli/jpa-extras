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
            "create sequence hibernate_sequence start with 1 increment by 1;\n" +
                    "create table Persistables (id bigint not null, aDate date, aString varchar(255), primary key (id));\n" +
                    "create table Users (id bigint not null, primary key (id));\n",
            Files.readString(schema));
  }

  @Test
  void canGenerateSchemaWithFormat() throws IOException {
    var schema = Files.createTempFile("schema", ".sql");
    JpaSchemaExport.execute(WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME, schema.toString(), true);
    assertEquals("create sequence hibernate_sequence start with 1 increment by 1;\n" +
                    "\n" +
                    "    create table Persistables (\n" +
                    "       id bigint not null,\n" +
                    "        aDate date,\n" +
                    "        aString varchar(255),\n" +
                    "        primary key (id)\n" +
                    "    );\n" +
                    "\n" +
                    "    create table Users (\n" +
                    "       id bigint not null,\n" +
                    "        primary key (id)\n" +
                    "    );\n",
            Files.readString(schema));
  }

}
