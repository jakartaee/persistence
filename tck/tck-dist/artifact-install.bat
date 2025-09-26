@echo off
REM
REM  Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
REM
REM  This program and the accompanying materials are made available under the
REM  terms of the Eclipse Public License v. 2.0, which is available at
REM  http://www.eclipse.org/legal/epl-2.0.
REM
REM  This Source Code may also be made available under the following Secondary
REM  Licenses when the conditions for such availability set forth in the
REM  Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
REM  version 2 with the GNU Classpath Exception, which is available at
REM  https://www.gnu.org/software/classpath/license.html.
REM
REM  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
REM

@setlocal

REM script to install the artifact directory contents into a local maven repository

echo %1|findstr /r "^[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*.*$">nul 2>&1
if errorlevel 1 (SET VERSION="3.2.0") else (SET VERSION=%1)

SET JAKARTAEE_VERSION="11.0.0-M1"

REM dbprocedures jar
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=dbprocedures-%VERSION%.jar" "-DgroupId=jakarta.tck" -DartifactId=dbprocedures -Dversion=%VERSION% -Dpackaging=jar

REM persistence-tck pom
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=persistence-tck-%VERSION%.pom" "-DgroupId=jakarta.tck" -DartifactId=persistence-tck -Dversion=%VERSION% -Dpackaging=pom

REM persistence-tck-common jar
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=persistence-tck-common-%VERSION%.jar" "-DgroupId=jakarta.tck" -DartifactId=persistence-tck-common -Dversion=%VERSION% -Dpackaging=jar

REM persistence-tck-common pom
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=persistence-tck-common-%VERSION%.pom" "-DgroupId=jakarta.tck" -DartifactId=persistence-tck-common -Dversion=%VERSION% -Dpackaging=pom

REM persistence-tck-spec-tests jar
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=persistence-tck-spec-tests-%VERSION%.jar" "-DgroupId=jakarta.tck" -DartifactId=persistence-tck-spec-tests -Dversion=%VERSION% -Dpackaging=jar

REM persistence-tck-spec-tests pom
call mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file "-Dfile=persistence-tck-spec-tests-%VERSION%.pom" "-DgroupId=jakarta.tck" -DartifactId=persistence-tck-spec-tests -Dversion=%VERSION% -Dpackaging=pom
