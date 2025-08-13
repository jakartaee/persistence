///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
The following sections should be customized for the technology.
This text was originally from the JAX-RS TCK.  Most references
to JAX-RS have been parameterized to serve as a simple starting
point for customization.  There are still many details that will
need to be changed or removed.  The major sections 4.1, 4.2, and
4.3 should be preserved.  If their titles are changed, the links
at the top of config.adoc will need to be changed as well as well
as toc.adoc.
///////////////////////////////////////////////////////////////////////

[[GBFVU]][[configuring-your-environment-to-run-the-tck-against-the-reference-implementation]]

4.1 Configuring Your Environment to Run the TCK Against the Compatible Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<JAVA_HOME>` becomes `$JAVA_HOME` on
Solaris/Linux and `%JAVA_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================


1.  Set the following environment variables in your shell environment:
  a.  `JAVA_HOME` to the directory in which Java SE {SEversion} is installed
  b.  `M2_HOME` to the directory in which the Apache Maven build tool is installed.
  c.  +{TechnologyHomeEnv}+ to the directory in which the {TechnologyShortName}
  {TechnologyVersion} CI has been installed
  d.  `PATH` to include the following directories: `JAVA_HOME/bin`,
  and `M2_HOME/bin`
2.  set the following system properties:
  a.  Set `jpa.classes` to include all of the necessary JAR files that
  pertain to your implementation using comma separator in the TCK runner.
  b.  Set `jdbc.lib.classpath` to the location where the JDBC drivers are
  installed using comma separator in the TCK runner.
  c.  Set `jdbc.driver.classes` to the location where the JDBC drivers are
  installed using CLASSPATH separator in the TCK runner.
  d.  Set `jdbc.db` to the name of the database under test. Valid values
  include:
+
--
[source,oac_no_warn]
----
derby
mysql
sybase
db2
mssqlserver
oracle
postgresql
----
--
+
  e.  Set `jakarta.persistence.provider`, `jakarta.persistence.jdbc.driver`,
  `jakarta.persistence.jdbc.url`, `jakarta.persistence.jdbc.user`, and
  `jakarta.persistence.jdbc.password` to the appropriate values for the
  database and persistence provider under test. +
  These properties are passed to the Persistence provider during the
  creation of the `EntityManagerFactory`. Any additional values, for
  example setting an implementation's logging level, must be set by
  following the instructions in Step 2e.
  f.  Set the `jpa.provider.implementation.specific.properties` property
  to include any implementation-specific settings that need to be passed
  to the provider when the `EntityManagerFactory` is created.
  g.  Set `sigTestClasspath` to include any additional classes not
  specified with the `jpa.classes` property.
  h.  Set db.supports.sequence to false if the database does not support
  the use of SEQUENCE. +
  The default value is true.
  i. Set `persistence.second.level.caching.supported` to false if your
  persistence provider does not support second level caching. +
  The default value is true.
+
3.  If you are using MySQL or MS SQL Server, do the following:
  a.  If you are using MySQL, see link:#GJLIB[Section 4.3.1, "Setup
  Considerations for MySQL,"] and proceed to Step 4.
  b.  If you are using MS SQL Server, see link:#GJLHU[Section 4.3.2, "Setup
  Considerations for MS SQL Server,"] and proceed to Step 4.
+
4.  Start the database under test.
5.  Initialize the database under test. Execute following SQL scripts for database initilization:
  a. <jdbc.db>.ddl.persistence.sql
  b. <jdbc.db>.ddl.persistence.sprocs.sql


[[GCLHU]][[configuring-your-environment-to-repackage-and-run-the-tck-against-the-vendor-implementation]]

4.2 Configuring Your Environment to Repackage and Run the TCK Against the Vendor Implementation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


[NOTE]
=======================================================================

In these instructions, variables in angle brackets need to be expanded
for each platform. For example, `<JAVA_HOME>` becomes `$JAVA_HOME` on
Solaris/Linux and `%JAVA_HOME%` on Windows. In addition, the forward
slashes (`/`) used in all of the examples need to be replaced with
backslashes (`\`) for Windows. Finally, be sure to use the appropriate
separator for your operating system when specifying multiple path
entries (`;` on Windows, `:` on UNIX/Linux).

On Windows, you must escape any backslashes with an extra backslash in
path separators used in any of the following properties, or use forward
slashes as a path separator instead.

=======================================================================

Adapt the instructions above as appropriate for your implementation.


[[GHGDG]][[setup-considerations]]

4.3 Setup Considerations
~~~~~~~~~~~~~~~~~~~~~~~~


[[GJLIB]][[setup-considerations-for-mysql]]

=== 4.3.1 Setup Considerations for MySQL

The {TechnologyFullName} tests require delimited identifiers for
the native query tests. If you are using delimited identifiers on MySQL,
modify the `sql-mode` setting in the `my.cnf` file to set the
ANSI_QUOTES option. After setting this option, reboot the MySQL server.
Set the option as shown in this example:

[source,oac_no_warn]
----
sql-mode=
    "STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,ANSI_QUOTES"
----

[[GJLHU]][[setup-considerations-for-ms-sql-server]]

=== 4.3.2 Setup Considerations for MS SQL Server

If your database already exists and if you use a case-sensitive
collation on MS SQL Server, execute the following command to modify the
database and avert errors caused by case-sensitive collation:

[source,oac_no_warn]
----
ALTER DATABASE ctsdb COLLATE Latin1_General_CS_AS ;
----

