package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.JDBCClientRepository;
import pl.com.bottega.photostock.sales.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Dell on 2016-06-03.
 */
public class JDBCClientRepositoryTest {

    private ClientRepository clientRepository;
    private static final Client TEST_CLIENT = new Client("nr2", "Maria Peszek", "Polska", ClientStatus.STANDARD, new Money(0, Money.CurrencyValues.PLN), new Money(205, Money.CurrencyValues.PLN), new Money(0), true);


    @Before
    public void setUp() throws SQLException {
        //given
        Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:photostock", "SA", "");
        createClientsTable(c);
        insertTestClient(c);
        c.close();
        //when
        clientRepository = new JDBCClientRepository("jdbc:hsqldb:mem:photostock", "SA", "");
    }

    @Test
    public void shouldLoadClient() throws SQLException {

        Client client = clientRepository.load("nr1");
        //then
        Assert.assertEquals("nr1", client.getNumber());
        Assert.assertEquals("Test Client", client.getName());
        Assert.assertEquals(new Money(200, Money.CurrencyValues.PLN), client.getAmount());
    }

    @Test
    public void shouldReturnNullWhenClientDoesntExists() throws Exception {
        Client client = clientRepository.load("nr500");
        //then
        assertNull(client);
    }

    @Test
    public void shouldSaveClient() {
        //given
        Client client = TEST_CLIENT;

        //when
        clientRepository.save(client);

        //then
        Client saved = clientRepository.load("nr2");
        assertEquals("nr2", saved.getNumber());
        assertEquals(new Money(205), saved.getAmount());
        assertEquals(ClientStatus.STANDARD, saved.getStatus());
        assertEquals(true, saved.isActive());
    }

    @Test
    public void shouldUpdateClient() {
        //given
        Client client = TEST_CLIENT;

        //when
        clientRepository.save(client);
        Client updatedClient = new Client("nr2", "Maria Peszek", "Polska", ClientStatus.VIP, new Money(100, Money.CurrencyValues.PLN), new Money(205, Money.CurrencyValues.PLN), new Money(0), false);
        clientRepository.save(updatedClient);

        //then
        Client saved = clientRepository.load("nr2");
        assertEquals("nr2", saved.getNumber());
        assertEquals(new Money(100), saved.getDebt());
        assertEquals(ClientStatus.VIP, saved.getStatus());
        assertEquals(false, saved.isActive());
    }

    private void createClientsTable(Connection c) throws SQLException {
        c.createStatement().executeUpdate("DROP TABLE Clients IF EXISTS");
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

    private void insertTestClient(Connection c) throws SQLException {
        c.createStatement().executeUpdate("INSERT INTO Clients (number, name, address, amountCents, debtCents, creditLimitCents, " +
                "amountCurrency, active, statusId) VALUES ('nr1', 'Test Client', 'ul. Koralowa 10', 20000, 0,0, 'PLN', true, 0);");
    }
}
