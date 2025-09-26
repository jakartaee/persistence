# Jakarta Persistence Specification Project

This is the project for the Jakarta Persistence Specification. The Jakarta Persistence Specification defines a standard
for management of persistence and object/relational mapping in Java(R) environments.

## API Specifications and Javadocs

The official location for all of the Jakarta EE API Specifications and Javadocs, including Jakarta Persistence,
is on the [Jakarta EE Specifications](https://jakarta.ee/specifications/) page.
This web provides access to the Jakarta Persistence Specification DRAFTs and to the nigthly builds.

Latest stable DRAFT builds of the API and javadocs is STG_VER and it is available for use by vendors for implementation
and for the general public to provide feedback on the latest additions, corrections and other updates in the specification.

NIGHTLY versions of the specification documents and Javadocs are for reference only and are not expected
nor recommended for general use. They provide access to the latest, greatest features, which were just completed,
but which are not yet available for direct implementation and/or use. 

## <a name="Development_Releases"></a>Development Releases

After each commit snapshot releases of the next version of Jakarta Persistence
under development are published to the
[Jakarta Sonatype OSS repository](https://jakarta.oss.sonatype.org).
These snapshot releases have received only minimal testing, but may
provide previews of bug fixes or new features under development.

For example, you can download the jakarta.persistence-api.jar file from the Jakarta Persistence
4.0.0-SNAPSHOT release
[here](https://jakarta.oss.sonatype.org/content/repositories/snapshots/jakarta/persistence/jakarta.persistence-api/4.0.0-SNAPSHOT/).
Be sure to scroll to the bottom and choose the jar file with the most
recent time stamp.

You'll need to add the following configuration to your Maven ~/.m2/settings.xml
to be able to use these with Maven:

```
    <profiles>
        <!-- to allow loading Jakarta snapshot artifacts -->
        <profile>
            <id>jakarta-oss-repo</id>
            <repositories>
                <repository>
                    <id>jakarta-staging</id>
                    <name>Jakarta Staging</name>
                    <url>https://jakarta.oss.sonatype.org/content/repositories/staging/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>jakarta-snapshots</id>
                    <name>Jakarta Snapshots</name>
                    <url>https://jakarta.oss.sonatype.org/content/repositories/snapshots/</url>
                    <releases>
                        <enabled>false</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
        </profile>
    </profiles>
```

And then when you build use `mvn -Pjakarta-oss-repo ...` to get the latest SNAPSHOT or more stable staged version.

If you want the plugin repository to be enabled all the time so you don't need the -P, add:

```
    <activeProfiles>
        <activeProfile>jakarta-oss-repo</activeProfile>
    </activeProfiles>
```

<br/>

By contributing to this project, you agree to these additional terms of
use, described in [CONTRIBUTING](CONTRIBUTING.md).

