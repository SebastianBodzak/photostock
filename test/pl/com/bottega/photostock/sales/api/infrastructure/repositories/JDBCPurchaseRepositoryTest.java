package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.JDBCPurchaseRepository;
import pl.com.bottega.photostock.sales.model.Purchase;
import pl.com.bottega.photostock.sales.model.PurchaseRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Dell on 2016-06-07.
 */
public class JDBCPurchaseRepositoryTest {

    private PurchaseRepository purchaseRepository;

    @Before
    public void setUp() throws SQLException {
        //given
        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:photostock", "SA", "");
        dropTables(c);
        createClientsTable(c);
        createProductsTable(c);
        createPurchasesTable(c);
        createPurchasesProductsTable(c);

        insertTestClient(c);
        insertTestProduct(c);
        insertSecondTestProduct(c);
        insertTestPurchase(c);
        c.close();
        //when
        purchaseRepository = new JDBCPurchaseRepository("jdbc:hsqldb:mem:photostock", "SA", "");
    }

    @Test
    public void shouldLoadPurchase() {
        Purchase purchase = purchaseRepository.load("nr2");
        //then
        Assert.assertEquals("nr2", purchase.getNumber());
    }

    private void dropTables(Connection c) throws SQLException {
        c.createStatement().executeUpdate("DROP TABLE Purchases IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE PurchasesProducts IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Products IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Clients IF EXISTS");
    }

    private void createClientsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE Clients (\n" +
                "  id IDENTITY PRIMARY KEY,\n" +
                "  number VARCHAR(36) NOT NULL,\n" +
                "  name VARCHAR(255) NOT NULL,\n" +
                "  address VARCHAR(255) NOT NULL ,\n" +
                "  amountCents INTEGER DEFAULT 0 NOT NULL,\n" +
                "  debtCents INTEGER DEFAULT 0 NOT NULL,\n" +
                "  creditLimitCents INTEGER DEFAULT 0 NOT NULL,\n" +
                "  amountCurrency CHAR(3) DEFAULT 'PLN' NOT NULL,\n" +
                "  active BOOLEAN DEFAULT true NOT NULL,\n" +
                "  statusId INTEGER DEFAULT 0 NOT NULL\n" +
//                "  statusId INTEGER FOREIGN KEY REFERENCES Statuses(id)\n" +
                ");");
    }

    private void createProductsTable(Connection c) throws SQLException {
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

    private void createPurchasesTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE Purchases (\n" +
                "  id INTEGER IDENTITY PRIMARY KEY,\n" +
                "  number VARCHAR(36) NOT NULL,\n" +
                "  clientId INTEGER FOREIGN KEY REFERENCES Clients(id),\n" +
                "  createDate TIMESTAMP NOT NULL\n" +
                ");");
    }

    private void createPurchasesProductsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE PurchasesProducts (\n" +
                "  purchaseId INTEGER FOREIGN KEY REFERENCES Purchases(id),\n" +
                "  productId INTEGER FOREIGN KEY REFERENCES Products(id),\n" +
                "  PRIMARY KEY (purchaseId, productId)\n" +
                ");");
    }

    private void insertTestClient(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Clients (number, name, address, amountCents, debtCents, creditLimitCents, " +
                "amountCurrency, active, statusId) VALUES ('nr1', 'Test Client', 'ul. Koralowa 10', 20000, 0,0, 'PLN', true, 0);");
    }

    private void insertTestProduct(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Products (number, name, available, priceCents, priceCurrency, length, type) " +
                "VALUES ('nr1','Mazda 3456789', true, 200, 'USD', NULL, 'Picture');");
    }

    private void insertSecondTestProduct(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Products (number, name, available, priceCents, priceCurrency, length, type) " +
                "VALUES ('nr2','BMW', true, 300, 'PLN', NULL, 'Picture');");
    }

    private void insertTestPurchase(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Purchases (clientId, createDate) VALUES (0, '2016-01-12 10:00:00');");
        c.createStatement().executeUpdate("INSERT INTO PurchasesProducts VALUES (0, 0);");
    }
}
