Jakarta Persistence TCK
=======================

To run from the command line:

    cd tck
    mvn -pl spec-tests test -P "hibernate,postgresql" \
    -Djakarta.persistence.jdbc.user=user \
    -Djakarta.persistence.jdbc.password=password \
    -Djakarta.persistence.jdbc.url=jdbc:postgresql:database \
    -Djakarta.persistence.schema-generation.database.action=drop-and-create

where `database` is the name of the database and `user` and 
`password` are the corresponding credentials.

To accelerate the test suite, first export the TCK DDL schema 
to the database and remove the last line of the command above.

To run from IntelliJ:

1. Open the project in IntelliJ
2. Make sure the TCK is visible in the Maven view; 
   if it is not, right-click on the `tck/pom.xml` file 
   and select `Add as Maven Project`
3. In the Maven view, activate the profiles for database 
   and Persistence provider you wish to test, for example, 
   `postgresql` and `hibernate`
4. Create a run configuration for the tests you wish to run;
   for all tests, select the `persistence-tck-spec-tests`
   module
5. In the Run/Debug Configuration dialog, set the VM options 
   for your database, for example:
   - `-Djakarta.persistence.jdbc.user=user`
   - `-Djakarta.persistence.jdbc.password=password`
   - `-Djakarta.persistence.jdbc.url=jdbc:postgresql://localhost/database`
6. Export the TCK DDL schema to your database;
   alternatively, add: 
   - `-Djakarta.persistence.schema-generation.database.action=drop-and-create`
7. Run the configuration


