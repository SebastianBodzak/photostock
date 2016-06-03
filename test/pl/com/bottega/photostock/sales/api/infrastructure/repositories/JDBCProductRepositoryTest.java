package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.JDBCProductRepository;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Dell on 2016-05-29.
 */
public class JDBCProductRepositoryTest {

    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        //given
        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:photostock", "SA", "");
        createProductsTable(c);
        insertTestProduct(c);
        c.close();
        //when
        productRepository = new JDBCProductRepository("jdbc:hsqldb:mem:photostock", "SA", "");
    }

    @Test
    public void shouldLoadProduct() throws Exception {
        Product product = productRepository.load("nr1");
        //then
        assertEquals("nr1", product.getNumber() );
        assertEquals("Mazda 3456789", product.getTitle());
    }

    @Test
    public void shouldReturnNullWhenProductDoesntExists() throws Exception {
        Product product = productRepository.load("nr500");
        //then
        assertNull(product);
    }

    @Test
    public void shouldSaveProduct() {
        //given
        Product picture = new Picture("nr2", "title", new Money(205, Money.CurrencyValues.PLN), true, null); //todo sprawdzić money

        //when
        productRepository.save(picture);

        //then
        Product saved = productRepository.load("nr2");
        assertEquals("nr2", saved.getNumber());
        assertEquals(new Money(205), saved.getPrice());

    }

    //number,title,price,currency,available,tags,resolution,type
    private void createProductsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("DROP TABLE Products IF EXISTS");
        c.createStatement().executeUpdate("CREATE TABLE Products (\n" +
                "  id INTEGER IDENTITY PRIMARY KEY,\n" +
                "  number VARCHAR(36) NOT NULL,\n" +
                "  type VARCHAR(20) NOT NULL,\n" +
                "  name VARCHAR(255) NOT NULL,\n" +
                "  available BOOLEAN DEFAULT true NOT NULL,\n" +
                "  priceCents INTEGER DEFAULT 0 NOT NULL,\n" +
                "  priceCurrency CHAR(3) DEFAULT 'PLN' NOT NULL,\n" +
                "  length BIGINT\n" +
                ");");
    }

    private void insertTestProduct(Connection c) throws Exception {
        c.createStatement().executeUpdate("INSERT INTO Products (number, name, available, priceCents, priceCurrency, length, type) VALUES ('nr1','Mazda 3456789', true, 200, 'USD', NULL, 'Picture');");
    }
}
