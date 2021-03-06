package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.JDBCPurchaseRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * Created by Dell on 2016-06-07.
 */
public class JDBCPurchaseRepositoryTest {

    private PurchaseRepository purchaseRepository;

    @Before
    public void setUp() throws SQLException {
        //given
        Connection c = getConnection();
        dropTables(c);

        createClientsTable(c);
        createStatusesTable(c);
        createProductsTable(c);
        createTagsTable(c);
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
        Purchase purchase = purchaseRepository.load("nr1");
        //then
        Assert.assertEquals("nr1", purchase.getNumber());
        Assert.assertEquals("Test Client", purchase.getOwner().getName());
        Assert.assertEquals(1, purchase.getItems().size());
    }


    @Test
    public void shouldReturnNullWhenPurchaseDoesntExists() throws Exception {
        Purchase purchase = purchaseRepository.load("nr500");
        //then
        assertNull(purchase);
    }

    @Test
    public void shouldSavePurchase() throws SQLException {
        //given
        Connection c = getConnection();
        Client owner = getClient(c);
        Product product = getFistProduct(c);
        Product product2 = getSecondProduct(c);
        List<Product> products = new LinkedList<>();
        products.add(product);
        products.add(product2);
        //when
        Purchase purchase = new Purchase("nr2", owner, products);
        purchaseRepository.save(purchase);
        //then
        Purchase saved = purchaseRepository.load("nr2");
        Assert.assertEquals(2, saved.getItems().size());
        Assert.assertEquals(owner.getNumber(), saved.getOwner().getNumber());
        Assert.assertEquals(new Money(200, Money.CurrencyValues.USD), saved.getItems().get(0).getPrice());

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:photostock", "SA", "");
    }

    private void dropTables(Connection c) throws SQLException {
        c.createStatement().executeUpdate("DROP TABLE PurchasesProducts IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE ProductsTags IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Tags IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Purchases IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Products IF EXISTS");
        c.createStatement().executeUpdate("DROP TABLE Statuses IF EXISTS");
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
                ");");
    }

    private void createPurchasesProductsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE PurchasesProducts (\n" +
                "  purchaseId INTEGER FOREIGN KEY REFERENCES Purchases(id),\n" +
                "  productId INTEGER FOREIGN KEY REFERENCES Products(id),\n" +
                "  PRIMARY KEY (purchaseId, productId)\n" +
                ");");
    }

    private void createTagsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE Tags (\n" +
                "  id INTEGER IDENTITY PRIMARY KEY,\n" +
                "  name VARCHAR(255) NOT NULL\n" +
                ");");
        c.createStatement().executeUpdate("CREATE TABLE ProductsTags (\n" +
                "  productId INTEGER FOREIGN KEY REFERENCES Products(id),\n" +
                "  tagId INTEGER FOREIGN KEY REFERENCES Tags(id),\n" +
                "  PRIMARY KEY (productId, tagId)\n" +
                ");\n");
    }

    private void createStatusesTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("CREATE TABLE Statuses (\n" +
                "  id IDENTITY PRIMARY KEY,\n" +
                "  name VARCHAR(40) NOT NULL\n" +
                ");");
        c.createStatement().executeUpdate("INSERT INTO Statuses (name) VALUES ('standard');\n");
        c.createStatement().executeUpdate("INSERT INTO Statuses (name) VALUES ('vip');\n");
        c.createStatement().executeUpdate("INSERT INTO Statuses (name) VALUES ('silver');\n");
        c.createStatement().executeUpdate("INSERT INTO Statuses (name) VALUES ('gold');\n");
        c.createStatement().executeUpdate("INSERT INTO Statuses (name) VALUES ('platinum');\n");
    }

    private Client getClient(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT number, name FROM Clients WHERE id = 0");
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            return null;
        String number = rs.getString("number");
        String name = rs.getString("name");
        return new Client(number, name);
    }

    private void insertTestClient(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Clients (number, name, address, amountCents, debtCents, creditLimitCents, " +
                "amountCurrency, active, statusId) VALUES ('nr1', 'Test Client', 'ul. Koralowa 10', 20000, 0,0, 'PLN', true, 0);");
    }

    private void insertTestProduct(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Products (number, name, available, priceCents, priceCurrency, length, type) " +
                "VALUES ('nr1','Mazda 3456789', true, 200, 'USD', NULL, 'Picture');");
    }

    private Product getFistProduct(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT number, name FROM Products WHERE id = 0");
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            return null;
        String number = rs.getString("number");
        String name = rs.getString("name");
        return new Picture(number, name);
    }

    private void insertSecondTestProduct(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Products (number, name, available, priceCents, priceCurrency, length, type) " +
                "VALUES ('nr2','BMW', true, 300, 'PLN', NULL, 'Picture');");
    }

    private Product getSecondProduct(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT number, name FROM Products WHERE id = 1");
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            return null;
        String number = rs.getString("number");
        String name = rs.getString("name");
        return new Picture(number, name);
    }

    private void insertTestPurchase(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Purchases (clientId, number) VALUES (0, 'nr1');");
        c.createStatement().executeUpdate("INSERT INTO PurchasesProducts VALUES (0, 0);");
    }
}
