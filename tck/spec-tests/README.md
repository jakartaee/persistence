[//]: # " Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved. "
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

# JPA spec tests

## Running in IntelliJ

Create a run configuration for a test and add the following system properties for e.g. PostgreSQL with Hibernate ORM

* `-Dplatform.mode=standalone` 
* `-Dpersistence.unit.name=JPATCK`
* `-Dpersistence.unit.name.2=JPATCK2` 
* `-Djakarta.persistence.jdbc.user=hibernate_orm_test`
* `-Djakarta.persistence.jdbc.password=hibernate_orm_test` 
* `-Djdbc.db=postgresql`
* `-Ddb.supports.sequence=true`
* `-Djakarta.persistence.provider=org.hibernate.jpa.HibernatePersistenceProvider`
* `-Djakarta.persistence.jdbc.driver=org.postgresql.Driver`
* `-Djakarta.persistence.jdbc.url=jdbc:postgresql://localhost/hibernate_orm_test?preparedStatementCacheQueries=0&escapeSyntaxCallMode=callIfNoReturn`
* `-Djpa.provider.implementation.specific.properties=hibernate.query.jpaql_strict_compliance=true:hibernate.id.new_generator_mappings=true:hibernate.cache.region.factory_class=org.hibernate.testing.cache.CachingRegionFactory:hibernate.model.generator_name_as_sequence_name=true:hibernate.jpa.compliance.transaction=true:hibernate.jpa.compliance.closed=true:hibernate.jpa.compliance.query=true:hibernate.jpa.compliance.list=true:hibernate.jpa.compliance.caching=true:hibernate.jpa.compliance.global_id_generators=true:hibernate.jpa.compliance=true:hibernate.type.wrapper_array_handling=legacy:hibernate.type.preferred_uuid_jdbc_type=char:hibernate.show_sql=true`
* `-Dpersistence.second.level.caching.supported=true`
