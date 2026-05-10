@NamedQuery(
        name = DescriptorBook.QUERY,
        query = "SELECT b FROM Jpa40DescriptorBook b WHERE b.title = :title",
        resultClass = DescriptorBook.class)
@NamedNativeQuery(
        name = DescriptorBook.NATIVE_QUERY,
        query = "SELECT ID, TITLE FROM JPA40_DESCRIPTOR_BOOK WHERE TITLE = ?",
        resultClass = DescriptorBook.class)
@NamedStatement(
        name = DescriptorBook.STATEMENT,
        statement = "UPDATE Jpa40DescriptorBook b SET b.title = :title WHERE b.id = :id")
package ee.jakarta.tck.persistence.jpa40.nameddescriptor;

import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedStatement;
