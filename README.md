[//]: # " Copyright (c) 2019, 2026 Contributors to the Eclipse Foundation. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Distribution License v. 1.0, which is available at "
[//]: # " http://www.eclipse.org/org/documents/edl-v10.php. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: BSD-3-Clause "

# Jakarta Persistence project

[![Build Status](https://github.com/jakartaee/persistence/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/jakartaee/persistence/actions/workflows/maven.yml?branch=main)
[![Jakarta Staging (Snapshots)](https://central.sonatype.com/repository/maven-snapshots/jakarta/persistence/jakarta.persistence-api/)](https://central.sonatype.com/repository/maven-snapshots/jakarta/persistence/jakarta.persistence-api/)

[PDF]: https://jakartaee.github.io/persistence/latest-nightly/nightly.pdf
[HTML]: https://jakartaee.github.io/persistence/latest-nightly/nightly.html
[API]: https://jakartaee.github.io/persistence/latest-nightly/api/jakarta.persistence/module-summary.html
[4.0]: https://jakarta.ee/specifications/persistence/4.0/

Jakarta Persistence defines the industry standard for management of 
persistence and object/relational mapping in Java&reg; environments.
It is the most widely used persistence solution in the Java ecosystem
and by far the most successful object/relational mapping API in any 
programming language.

This specification was originally developed by the Java Community
Process and was known as JPA (the Java Persistence API) prior to it 
being made open source and donated to the Eclipse Foundation.

[Jakarta Persistence 4.0][4.0] is a major revision of the specification
and is currently under very active development with release targeted
for late 2026.

The latest information and milestone builds may be found at:

<https://jakartaee.github.io/persistence/>

In particular, the last drafts of the specification are available in
[PDF][] and [HTML][] format, along with [Javadoc API documentation][API].

Jakarta Persistence 4 is designed to work hand in hand with:

- Java SE 21 and JDBC
- [Jakarta Data](https://github.com/jakartaee/data)
- [Jakarta Query](https://github.com/jakartaee/query)
- [Jakarta Validation](https://github.com/jakartaee/validation)
- [Jakarta Transactions](https://github.com/jakartaee/transactions)
- [CDI](https://github.com/jakartaee/cdi)
- The entire [Jakarta EE platform](https://github.com/jakartaee/platform)

The Persistence specification integrates with the EE platform and other 
Jakarta EE technologies but does not require them. Implementations of 
Jakarta Persistence are able to operate inside or outside of a container
environment, and in practice Jakarta Persistence is extremely widely used 
outside of the Jakarta EE platform.

## License

* Most of the Jakarta Persistence project source code is licensed
  under the [Eclipse Public License (EPL) v2.0](https://www.eclipse.org/legal/epl-2.0/)
  and [Eclipse Distribution License (EDL) v1.0.](https://www.eclipse.org/org/documents/edl-v10.php);
  see the license information at the top of each source file.
* The source code for the Jakarta Persistence Specification project
  is licensed under the [Eclipse Public License (EPL) v2.0](https://www.eclipse.org/legal/epl-2.0/)
  and [GNU General Public License (GPL) v2 with Classpath Exception](https://www.gnu.org/software/classpath/license.html);
  again, the license is in each source file.
* The binary jar files published to the Maven repository are licensed
  under the same licenses as the corresponding source code;
  see the file `META-INF/LICENSE.txt` in each jar file.

You’ll find the text of the licenses in the workspace in various 
`LICENSE.txt` or `LICENSE.md` files. Don’t let the presence of these 
license files in the workspace confuse you into thinking that they 
apply to all files in the workspace.

You should always read the license file included with every download
and read the license text applying to each source file.

## Contributing

We have a [contribution policy](CONTRIBUTING.md), which means we can only accept 
contributions under the terms of the [Eclipse Contributor Agreement](http://www.eclipse.org/legal/ECA.php).
