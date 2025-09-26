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