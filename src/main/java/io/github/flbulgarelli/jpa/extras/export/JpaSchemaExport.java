package io.github.flbulgarelli.jpa.extras.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hibernate.jpa.HibernatePersistenceProvider;

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

    // org.hibernate.tool.hbm2ddl.SchemaExport was removed in Hibernate 6; schema
    // generation is now driven entirely through the standard JPA
    // "jakarta.persistence.schema-generation.*" properties, applied against both
    // the database and a script target (mirroring the previous createOnly(DATABASE, SCRIPT)
    // call, which performed create-without-drop against both targets).
    Map<String, Object> properties = new HashMap<>();
    properties.put("jakarta.persistence.schema-generation.database.action", "create");
    properties.put("jakarta.persistence.schema-generation.scripts.action", "create");
    properties.put("jakarta.persistence.schema-generation.scripts.create-target", destination);
    properties.put("hibernate.format_sql", String.valueOf(format));

    new HibernatePersistenceProvider().generateSchema(persistenceUnitName, properties);

    System.out.println("Schema exported to " + destination);
  }
}
