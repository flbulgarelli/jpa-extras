package io.github.flbulgarelli.jpa.extras.export;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import static io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit.SIMPLE_PERSISTENCE_UNIT_NAME;

/**
 * Tool for exporting JPA schema. This class depends on Hibernate classes
 * <code>
 * usage: JpaSchemaExport [...options]
 *  -f,--format             If the output file should be formatted. Defaults
 *                          to 'false'.
 *  -h,--help               Print this message.
 *  -o,--output [arg]       Output file. Defaults to 'schema.sql'.
 *  -t,--target [arg]       Target persistence unit. Defaults to
 *                          'simple-persistence-unit'.
 * </code>
 */
public class JpaSchemaExport {

  public static void main(String[] args) {
    CommandLine cmd = parse(args).orElseGet(() -> {
      System.exit(1);
      return null;
    });

    execute(
        cmd.getOptionValue("target", SIMPLE_PERSISTENCE_UNIT_NAME),
        cmd.getOptionValue("output", "schema.sql"),
        cmd.hasOption("format"));
  }

  public static Optional<CommandLine> parse(String[] args) {
    Options options = new Options()
        .addOption("h", "help", false,
            "Print this message.")
        .addOption("f", "format", false,
            "If the output file should be formatted. Defaults to 'false'.")
        .addOption("o", "output", true,
            "Output file. Defaults to 'schema.sql'.")
        .addOption("t", "target", true,
            "Target persistence unit. Defaults to '"+ SIMPLE_PERSISTENCE_UNIT_NAME +"'.");

    DefaultParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();

    try {
      return Optional.of(parser.parse(options, args));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("JpaSchemaExport [...options]", options);
      return Optional.empty();
    }
  }

  public static void execute(String persistenceUnitName, String destination, boolean format) {
    System.out.println("Starting schema export");
    new HibernatePersistenceProvider() {
      {
        EntityManagerFactoryBuilderImpl emfb = (EntityManagerFactoryBuilderImpl) this
                .getEntityManagerFactoryBuilderOrNull(persistenceUnitName, new HashMap<>());
        emfb.generateSchema();

        SchemaExport schemaExport = new SchemaExport()
            .setOutputFile(destination)
            .setFormat(format);

        schemaExport.createOnly(
                EnumSet.of(TargetType.DATABASE, TargetType.SCRIPT),
                emfb.getMetadata());

        System.out.println("Schema exported to " + destination);
      }
    };

  }
}
