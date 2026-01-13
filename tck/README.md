[//]: # " Copyright (c) 2021, 2026 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Public License v. 2.0, which is available at "
[//]: # " http://www.eclipse.org/legal/epl-2.0. "
[//]: # "  "
[//]: # " This Source Code may also be made available under the following Secondary "
[//]: # " Licenses when the conditions for such availability set forth in the "
[//]: # " Eclipse Public License v. 2.0 are satisfied: GNU General Public License, "
[//]: # " version 2 with the GNU Classpath Exception, which is available at "
[//]: # " https://www.gnu.org/software/classpath/license.html. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 "

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


